package org.mars.toolkit.demo.jogl;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.mars.toolkit.demo.FrameInfo;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;

public final class GLFrame extends JFrame
{
  private static final long serialVersionUID = 7021807886302712089L;
  
  private GLCanvas canvas;
  private FrameInfo frameInfo;
  
  private Cursor savedCursor;
  private final static Cursor transparentCursor;

  static
  {
    Toolkit tk = Toolkit.getDefaultToolkit();
    BufferedImage cursor = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
    transparentCursor = tk.createCustomCursor(cursor,new Point(0,0),"");
  }

  
  public GLFrame(FrameInfo frameInfo)
  {
    this(frameInfo, (GLEventListener)null);
  }
  
  public GLFrame(FrameInfo frameInfo, GLEventListener listener)
  {
    this(frameInfo, GLBase.makeGLCanvas(listener));
  }
  
  public GLFrame(FrameInfo frameInfo, GLCanvas canvas)
  {
    super();

    if(frameInfo == null) {
      frameInfo = new FrameInfo(null, 640, 480, false);
    }
    
    String title = frameInfo.getTitle();
    if( title != null) {
      setTitle(title);
    }

    this.canvas = canvas;
    getContentPane().add(canvas);
    
    this.frameInfo = frameInfo;
    setSize(frameInfo.getWindowSize());
  }
  
  public void setVisible(boolean visible)
  {
    canvas.setVisible(visible);
    super.setVisible(visible);
  }
  
  
  protected GLCanvas getCanvas()
  {
    return canvas;
  }
  
  protected FrameInfo getFrameInfo()
  {
    return frameInfo;
  }
 
  public void showCursor(boolean cursorVisible)
  {
    if(cursorVisible)
    {
      if(savedCursor != null) {
        setCursor(savedCursor);
      }
    }
    else
    {
      savedCursor = getCursor();
      setCursor(transparentCursor);
    }
  }
  
}
