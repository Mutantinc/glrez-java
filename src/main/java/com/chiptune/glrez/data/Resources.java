/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a> and <a href="mailto:rez@chiptune.com">Christophe Résigné</a>
* 
* Java port of the GLRez intro released at the Breakpoint 2005
* @see http://www.pouet.net/prod.php?which=16327
* @see http://www.chiptune.com
* 
* Original C source code on GitHub
* @see https://github.com/chiptune/glrez
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package com.chiptune.glrez.data;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

import org.mars.demo.graph.GLTextureLoader;
import org.mars.toolkit.realtime.graph.Texture;

public class Resources {
  
  public final static Texture texPouet    = new Texture( getURL("pouet.gif"), false, true);
  public final static Texture texCoeur    = new Texture( getURL("coeur.gif"), false, true);
  public final static Texture texEnvmap   = new Texture( getURL("envmap.gif"), true, false);
  public final static Texture texParticle = new Texture( getURL("particle.gif"), true, false);
  public final static Texture texScreen   = new Texture( getURL("screen.gif"), false, true);
  public final static Texture texFont     = new Texture( getURL("font.gif"), true, true);
  public final static Texture texTitle    = new Texture( getURL("title.gif"), true, false);

  public final static URL modUrl = getURL("kenzalol.xm");
  public final static URL iconURL = getURL("icon.gif");
  
  
  public static URL getURL(String ref) {
    return Resources.class.getResource(ref); 
  }

  public static Image readIcon() {
    try {
      return ImageIO.read(iconURL);
    }
    catch(IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  // load textures
  public static void bindTextures(GL2 gl, GLUgl2 glu) throws IOException {
    GLTextureLoader.bind(gl, glu, texPouet);
    GLTextureLoader.bind(gl, glu, texCoeur);
    GLTextureLoader.bind(gl, glu, texEnvmap);
    GLTextureLoader.bind(gl, glu, texParticle);
    GLTextureLoader.bind(gl, glu, texScreen);
    GLTextureLoader.bind(gl, glu, texFont);
    GLTextureLoader.bind(gl, glu, texTitle);
  }
}
