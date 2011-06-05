package org.mars.toolkit.demo.jogl;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.mars.toolkit.demo.GraphUtils;
import org.mars.toolkit.demo.Texture;
import org.mars.toolkit.demo.TextureId;

public final class GLTextureLoader
{
  
  private static int createId(GL gl) 
  { 
    int[] tmp = new int[1]; 
    gl.glGenTextures(1, tmp, 0);
    return tmp[0]; 
  } 


  public static void bind(GL gl, GLU glu, Texture... textures) throws IOException
  {
    for(Texture texture : textures)
    {
      bind(gl, glu, texture);
    }
  }

  public static void bind(GL gl, GLU glu, Texture texture) throws IOException 
  { 
    //Try to load the image
    BufferedImage bufferedImage = GraphUtils.loadImage(texture.getPath()); 

    // create the texture ID for this texture
    int texId = createId(gl);
    texture.setId(texId);
    
    // bind this texture
    int target;
    if(texture.isProxy()) {
      target = GL.GL_PROXY_TEXTURE_2D;
    }
    else {
      target = GL.GL_TEXTURE_2D;
    }
    gl.glBindTexture(target, texId); 
    
    // Getting the real Width/Height of the Texture in the Memory
    texture.setImgSize(bufferedImage.getWidth(), bufferedImage.getHeight()); 
    
    Dimension texDim = get2Fold(texture.getImgSize()); 
    texture.setTexSize(texDim);
    
    int srcPixelFormat;
    if (bufferedImage.getColorModel().hasAlpha()) {
      srcPixelFormat = GL.GL_RGBA;
    }
    else {
      srcPixelFormat = GL.GL_RGB;
    }
    
    // convert that image into a byte buffer of texture data 
    ByteBuffer textureBuffer = GraphUtils.convertImageData(bufferedImage, texDim, true); 
    int wrapMode = texture.isRepeat() ? GL.GL_REPEAT : GL.GL_CLAMP; 
    boolean mipmappable = (texture.isMipmapped() && texDim.width > 2 && texDim.height > 2);
    
    if (target == GL.GL_TEXTURE_2D) 
    { 
      gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_S, wrapMode); 
      gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_T, wrapMode);

      int minFilter;
      if(texture.isMinLinear()) {
        minFilter = mipmappable ? GL.GL_LINEAR_MIPMAP_LINEAR : GL.GL_LINEAR;
      }
      else {
        minFilter = mipmappable ? GL.GL_LINEAR_MIPMAP_NEAREST : GL.GL_NEAREST;
      }
      gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter); 

      
      int magFilter;
      if(texture.isMagLinear()) {
        magFilter = GL.GL_LINEAR;
      }
      else {
        magFilter =  GL.GL_NEAREST;
      }
      gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, magFilter);
    }
    
    // create either a series of mipmaps of a single texture image based on what's loaded
    int dstPixelFormat;
    if (texture.isDstHasAlpha()) {
      dstPixelFormat = GL.GL_RGBA;
    }
    else {
      dstPixelFormat = GL.GL_RGB;
    }

    if (mipmappable) 
    { 
      glu.gluBuild2DMipmaps(target,
                            dstPixelFormat,
                            texDim.width,
                            texDim.height,
                            srcPixelFormat,
                            GL.GL_UNSIGNED_BYTE,
                            textureBuffer);
    } 
    else
    { 
      gl.glTexImage2D(target,
                      0,
                      dstPixelFormat, 
                      texDim.width, 
                      texDim.height, 
                      0,
                      srcPixelFormat,
                      GL.GL_UNSIGNED_BYTE,
                      textureBuffer );
    } 
  } 

  
  public void delete(GL gl, TextureId... textures)
  {
    if(textures != null)
    {
      int[] ids = new int[textures.length];
      
      for(int i = 0; i < textures.length; i++)
      {
        TextureId texture = textures[i];
        if(texture != null) {
          ids[i] = texture.getId();
        }
      }
      
      gl.glDeleteTextures(textures.length, ids, 0);
    }
  }
  
  public final static void deleteAll(GL gl)
  {
    int i = 0;
    while(gl.glIsTexture(++i))
    {
      gl.glDeleteTextures(1, new int[]{i}, 0);
    }
    
  }
  

  private static Dimension get2Fold(Dimension dim)
  {
    return new Dimension(get2Fold(dim.width), get2Fold(dim.height));
  }
  
  private static int get2Fold(int fold)
  {
    int ret = 2;
    while (ret < fold) {
      ret *= 2;
    }
    return ret;
  }
  
  
  
  public static int bindFont(GL gl, Font font)
  {
    final int glyphb2w = 32;              //width of a texture
    final int glyphb2h = glyphb2w;        //height of a texture
    float boxX = 0.5f;              //for the aspect ratio
    float boxY = 1.0f;              //initializing for the space [ ]
    float cellX = 0.5f;             //width of the char + spacing with next char
    float origX = 0.0f;             //width to slide to have centered letters
    float origY = 0.0f;             //height to slide to have chars at the good height

    final int nbGlyphs = 96;
    int listBase = gl.glGenLists(nbGlyphs);           //generating 96 rendering macros
    int[] ids = new int[nbGlyphs];
    gl.glGenTextures(nbGlyphs, ids, 0);           //for 96 textures
    
    gl.glPixelTransferi(GL.GL_INDEX_SHIFT, 0);
    gl.glPixelTransferi(GL.GL_INDEX_OFFSET, 0);
    gl.glPixelStorei(GL.GL_UNPACK_LSB_FIRST, 0);

    //on trouve la hauteur maxi d'un char (on a choisi ! pour ça)
    FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
    Rectangle2D maxCharBounds = font.getMaxCharBounds(fontRenderContext);
    float maxCharHeight = (float)maxCharBounds.getHeight();
    
    BufferedImage glyphBuffer = GraphUtils.createImageBuffer(GraphUtils.COLOR_MODEL_GRAY, glyphb2w, glyphb2h);
    Graphics2D g2 = glyphBuffer.createGraphics();
    g2.setFont(font);
    g2.drawChars(new char[]{' '}, 0, 1, 0, 0);
    ByteBuffer byteBuffer = GraphUtils.getByteBuffer(glyphBuffer);
    
    for(int i = 0; i < nbGlyphs; i++)
    {
      int id = ids[i];

      //paramètres de la texture
      gl.glBindTexture(GL.GL_TEXTURE_2D, id);
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);

      //fabrication de la texture
      gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_ALPHA, glyphb2w, glyphb2h, 0, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE, byteBuffer);
      
      //compilation de l'affichage de la texture
      gl.glNewList(listBase+i, GL.GL_COMPILE);
      gl.glBindTexture(GL.GL_TEXTURE_2D, id);
      gl.glTranslatef(origX, origY, 0.0f);

      gl.glBegin(GL.GL_QUADS);
      gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 0.0f, 0.0f, 0.0f);
      gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 0.0f, boxY, 0.0f);
      gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( boxX, boxY, 0.0f);
      gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( boxX, 0.0f, 0.0f);
      gl.glEnd();

      gl.glTranslatef(-origX+cellX, -origY, 0.0f);
      gl.glEndList();

      //génération du caractère suivant (+33 au lieu de +32). on met la charue avant les boeufs mais c'est pour optimiser
      char c = (char)(i+33);
      char[] cc = new char[]{c};

      GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, cc);
      GlyphMetrics metrics = glyphVector.getGlyphMetrics(0); //0: to get the metrics of the first char of cc, that is c
      Rectangle2D r2d = metrics.getBounds2D();
      int glyphWidth = (int)r2d.getWidth();
      int glyphHeight = (int)r2d.getHeight();
      
      BufferedImage glyphBufferTmp = GraphUtils.createImageBuffer(GraphUtils.COLOR_MODEL_GRAY, glyphWidth, glyphHeight);
      g2 = glyphBufferTmp.createGraphics();
      g2.setFont(font);
      g2.drawChars(cc, 0, 1, 0, 0);

      glyphBuffer = GraphUtils.createImageBuffer(GraphUtils.COLOR_MODEL_GRAY, glyphb2w, glyphb2h);
      glyphBuffer.createGraphics().drawImage(glyphBufferTmp, 0, 0, glyphWidth, glyphHeight, 0, 0, glyphb2w, glyphb2h, null);
      byteBuffer = GraphUtils.getByteBuffer(glyphBuffer);
      
      //also possible
      //gluScaleImage(GL.GL_ALPHA, glyphWidth, glyphHeight, GL.GL_UNSIGNED_BYTE, glyphbuf1, glyphb2w, glyphb2h, GL.GL_UNSIGNED_BYTE, glyphbuf2);
      
      //calcul des décalages et ratios suivants (il le fera pour i=96 aussi mais c'est pas grave)
      boxX = (float)r2d.getWidth()/maxCharHeight;
      boxY = (float)r2d.getHeight()/maxCharHeight;
      cellX = metrics.getAdvanceX()/maxCharHeight;
      origX = (float)r2d.getX()/maxCharHeight;
      origY = 1.0f - (float)r2d.getY()/maxCharHeight;
    }
    
    return listBase;
  }
  
}
