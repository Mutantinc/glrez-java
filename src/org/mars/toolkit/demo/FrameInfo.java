package org.mars.toolkit.demo;

import java.awt.Dimension;

public class FrameInfo
{
  private String title;
  private Dimension windowSize;
  private Dimension canvasSize;
  private boolean fullscreen;

  
  public FrameInfo(String name, Dimension dim, boolean fullscreen)
  {
    this(name, dim.width, dim.height, fullscreen);
  }

  public FrameInfo(String name, int width, int height, boolean fullscreen)
  {
    setName(name);
    setWindowSize(width, height);
    setFullscreen(fullscreen);
  }

  public boolean isFullscreen()
  {
    return fullscreen;
  }

  public void setFullscreen( boolean fullscreen )
  {
    this.fullscreen = fullscreen;
  }

  public void switchFullScreen()
  {
    fullscreen = !fullscreen;
  }
  
  public void setWindowSize( int width, int height )
  {
    windowSize = new Dimension(width, height);
  }

  public String getTitle()
  {
    return title;
  }

  public void setName( String name )
  {
    this.title = name;
  }

  public void setCanvasSize( int width, int height )
  {
    canvasSize = new Dimension(width, height);
  }

  public Dimension getWindowSize()
  {
    return new Dimension(windowSize);
  }

  public void setWindowSize( Dimension windowSize )
  {
    this.windowSize = new Dimension(windowSize);
  }

  public Dimension getCanvasSize()
  {
    return new Dimension(canvasSize);
  }

  public void setCanvasSize( Dimension canvasSize )
  {
    this.canvasSize = new Dimension(canvasSize);
  }

}
