package org.mars.toolkit.demo;

import java.awt.*;
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
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public final class GraphUtils
{
	public final static ColorModel COLOR_MODEL_RGBA = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] {8,8,8,8},
			true,
			false,
			ComponentColorModel.TRANSLUCENT,
			DataBuffer.TYPE_BYTE);
	
	public final static ColorModel COLOR_MODEL_RGB = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] {8,8,8,0},
			false,
			false,
			ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);
	
	public final static ColorModel COLOR_MODEL_GRAY = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
			new int[] {8},
			false,
			false,
			ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);
	
	
	private static final Font defaultFont = new Font("Arial", 1, 20);
	
	
	public static Font getDefaultFont()
	{
		return defaultFont;
	}
	
	
    public static ByteBuffer convertImageData(BufferedImage sourceImage, Dimension dim, boolean inColor)
    {
    	ColorModel colorModel = null;
    	if(inColor)
    	{
    		if(sourceImage.getColorModel().hasAlpha()) {
    			colorModel = COLOR_MODEL_RGBA;
    		}
    		else {
    			colorModel = COLOR_MODEL_RGB;
    		}
    	}
    	else {
    		colorModel = COLOR_MODEL_GRAY;
    	}
    	
    	BufferedImage destImage = createImageBuffer(colorModel, dim);
        Graphics g = destImage.getGraphics();
        g.drawImage(sourceImage, 0, 0, null);
        
        return getByteBuffer(destImage);
    }
    
    public static BufferedImage createImageBuffer(ColorModel colorModel, Dimension dim)
    {
    	return createImageBuffer(colorModel, dim.width, dim.height);
    }

    public static BufferedImage createImageBuffer(ColorModel colorModel, int width, int height)
    {
    	int numComponents = colorModel.getNumComponents();
        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, numComponents, null);
        BufferedImage bufferedImage = new BufferedImage(colorModel, raster, false, new Hashtable<Object, Object>());
        return bufferedImage;
    }
    
    public static ByteBuffer getByteBuffer(BufferedImage bufferedImage)
    {
    	Raster raster = bufferedImage.getRaster();
    	DataBufferByte dataBufferByte = (DataBufferByte) raster.getDataBuffer(); 
        byte[] data = dataBufferByte.getData(); 

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length); 
        byteBuffer.order(ByteOrder.nativeOrder()); 
        byteBuffer.put(data, 0, data.length);
        byteBuffer.flip();
        
        return byteBuffer;
    }
	
	
    public static BufferedImage loadImage(String ref) throws IOException 
    {
    	InputStream im = null;
    	
    	File file = new File(ref);
    	if (file.isFile())
    	{
    		im = new FileInputStream(file);
    	}
    	else
    	{
    		try
    		{
    			URL url = new URL(ref);
    			im = url.openStream();
    		}
    		catch(MalformedURLException e)
    		{
    			im = GraphUtils.class.getResourceAsStream(ref);    			
    		}
    	}
    	
   		BufferedInputStream bis = new BufferedInputStream(im);
    	return ImageIO.read(bis);
    } 
	
	
	
	
	public static void printMessage(Graphics g, String msg, Font font, Color fgColor, Color bgColor, int x, int y, boolean rect)
	{
		FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
		LineMetrics lm = font.getLineMetrics(msg, fontRenderContext);
		Rectangle2D r2d = font.getStringBounds(msg, fontRenderContext);
		Rectangle r = new Rectangle((int)r2d.getX(), (int)r2d.getY(), (int)r2d.getWidth(), (int)lm.getAscent());
		g.setFont(font);
		if(rect)
		{
			g.setColor(bgColor);
			int arc = font.getSize() / 4;
			g.fillRoundRect(x - 3, y - 3, r.width + 6, r.height + 6, arc, arc);
		}
		g.setColor(fgColor);
		g.drawString(msg, x + 1, (y + r.height) - (int)(lm.getDescent() - 2.0F * lm.getLeading()));
	}
	
}
