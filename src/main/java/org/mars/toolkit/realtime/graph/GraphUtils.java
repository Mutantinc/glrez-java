/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.realtime.graph;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

public final class GraphUtils {

  public final static ColorModel COLOR_MODEL_RGBA = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
  public final static ColorModel COLOR_MODEL_RGB = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
  public final static ColorModel COLOR_MODEL_GRAY = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8 }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
  public final static ColorModel COLOR_MODEL_ALPHA = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 0, 0, 0, 8 }, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

  private static final ImageObserver defaultObserver = new Container();
  private static final Font defaultFont = new Font("Arial", 1, 20);

  public static Font getDefaultFont() {
    return defaultFont;
  }

  public static ColorModel findLegacyColorModel(boolean inColor, boolean hasAlpha) {
    ColorModel colorModel = null;


    return colorModel;
  }

  public static ColorModel findLegacyColorModel(BufferedImage image) {
    ColorModel sourceColorModel = image.getColorModel();
    boolean inColor = (sourceColorModel.getColorSpace().getType() != ColorSpace.TYPE_GRAY);
    boolean hasAlpha = sourceColorModel.hasAlpha();

    if (inColor) {
      if (hasAlpha) {
        return COLOR_MODEL_RGBA;
      }
      else {
        return COLOR_MODEL_RGB;
      }
    }
    else {
      if (hasAlpha) {
        return COLOR_MODEL_ALPHA;
      }
      else {
        return COLOR_MODEL_GRAY;
      }
    }
  }
  
  
  public static ByteBuffer convertImageData(BufferedImage sourceImage, Dimension dim, boolean inColor) {
    ColorModel colorModel = findLegacyColorModel(sourceImage);
    BufferedImage destImage = createImageBuffer(colorModel, dim);
    Graphics g = destImage.getGraphics();
    g.drawImage(sourceImage, 0, 0, defaultObserver);

    return getByteBuffer(destImage);
  }

  public static BufferedImage createImageBuffer(ColorModel colorModel, Dimension dim) {
    return createImageBuffer(colorModel, dim.width, dim.height);
  }

  public static BufferedImage createImageBuffer(ColorModel colorModel, int width, int height) {
    int numComponents = colorModel.getNumComponents();
    WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, numComponents, null);
    BufferedImage bufferedImage = new BufferedImage(colorModel, raster, false, new Hashtable<Object, Object>());
    return bufferedImage;
  }

  public static ByteBuffer getByteBuffer(BufferedImage bufferedImage) {
    Raster raster = bufferedImage.getRaster();
    DataBufferByte dataBufferByte = (DataBufferByte) raster.getDataBuffer();
    byte[] data = dataBufferByte.getData();

    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length);
    byteBuffer.order(ByteOrder.nativeOrder());
    byteBuffer.put(data, 0, data.length);
    byteBuffer.flip();

    return byteBuffer;
  }

  public static void printMessage(Graphics g, String msg, Font font, Color fgColor, Color bgColor, int x, int y, boolean rect) {
    FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
    LineMetrics lm = font.getLineMetrics(msg, fontRenderContext);
    Rectangle2D r2d = font.getStringBounds(msg, fontRenderContext);
    Rectangle r = new Rectangle((int) r2d.getX(), (int) r2d.getY(), (int) r2d.getWidth(), (int) lm.getAscent());
    g.setFont(font);
    if (rect) {
      g.setColor(bgColor);
      int arc = font.getSize() / 4;
      g.fillRoundRect(x - 3, y - 3, r.width + 6, r.height + 6, arc, arc);
    }
    g.setColor(fgColor);
    g.drawString(msg, x + 1, (y + r.height) - (int) (lm.getDescent() - 2.0F * lm.getLeading()));
  }

}
