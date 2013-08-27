/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.realtime.graph;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

public abstract class FrameInfo {
  private String title;
  private Image icon;
  private Dimension windowSize;
  private Dimension drawableSize;
  private boolean fullScreen;

  public FrameInfo(String title) {
    setTitle(title);
  }

  public final String getTitle() {
    return title;
  }

  public final void setTitle(String title) {
    this.title = title;
  }

  public Image getIcon() {
    return icon;
  }

  public void setIcon(Image icon) {
    this.icon = icon;
  }

  public final Dimension getWindowSize() {
    return new Dimension(windowSize);
  }

  public void setWindowSize(int width, int height) {
    windowSize = new Dimension(width, height);
  }

  public final void setWindowSize(Dimension windowSize) {
    setWindowSize(windowSize.width, windowSize.height);
  }

  public final Dimension getDrawableSize() {
    return new Dimension(drawableSize);
  }

  public final void setDrawableSize(Dimension drawableSize) {
    this.drawableSize = new Dimension(drawableSize);
  }

  public final void setDrawableSize(int width, int height) {
    drawableSize = new Dimension(width, height);
  }

  public final boolean isFullScreen() {
    return fullScreen;
  }

  public final void setFullScreen(boolean fullScreen) {
    this.fullScreen = fullScreen;
  }

  public final void switchFullScreen() {
    fullScreen = !fullScreen;
    
    if(!fullScreen && windowSize == null) { //a FullScreen has no windowSize by default
      windowSize = new Dimension(drawableSize);
    }
  }

  public abstract void tryToApply(Window window);

  public final static void tryToApply(Window window, Dimension windowSize, boolean fullScreen, String title, Image icon) {
    if (fullScreen) {
      tryToUndecorate(window, true);
      tryToMaximize(window);
    }
    else {
      tryToUndecorate(window, false);
      window.setSize(windowSize);
    }

    if (window instanceof Dialog) {
      ((Dialog) window).setTitle(title);
    }

    if (window instanceof Frame) {
      ((Frame) window).setTitle(title);
      ((Frame) window).setIconImage(icon);
    }

    if (window instanceof DrawableFrame) {
      ((DrawableFrame) window).showCursor(fullScreen);
    }
  }

  public final static void tryToUndecorate(Window window, boolean undecorate) {
    if (window instanceof Frame) {
      ((Frame) window).setUndecorated(undecorate);
    }
    else if (window instanceof Frame) {
      ((Dialog) window).setUndecorated(undecorate);
    }
  }

  public final static void tryToMaximize(Window window) {
    Toolkit tk = Toolkit.getDefaultToolkit();

    if (window instanceof Frame && tk.isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
      Frame frame = (Frame) window;
      frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    else {
      window.setSize(tk.getScreenSize());
    }
  }

  public static String toString(DisplayMode displayMode) {
    return toString(displayMode.getWidth(), displayMode.getHeight()) + " x " + displayMode.getBitDepth() + "b @ " + displayMode.getRefreshRate() + "hz";
  }

  protected final static String toString(Dimension dim) {
    return String.valueOf(dim.width) + " x " + dim.height;
  }

  protected final static String toString(int width, int height) {
    return String.valueOf(width) + " x " + height;
  }

  // ===========================================================================

  public final static class Windowed extends FrameInfo {

    public Windowed(String title, Image icon, Dimension dim) {
      this(title, icon, dim.width, dim.height, false);
    }

    public Windowed(String title, Image icon, Dimension dim, boolean fullScreen) {
      this(title, icon, dim.width, dim.height, fullScreen);
    }

    public Windowed(String title, Image icon, int width, int height, boolean fullScreen) {
      super(title);
      setIcon(icon);
      setWindowSize(width, height);
      setFullScreen(fullScreen);
    }

    @Override
    public final void tryToApply(Window window) {
      tryToApply(window, getWindowSize(), isFullScreen(), getTitle(), getIcon());
    }

    @Override
    public String toString() {
      return toString(getWindowSize());
    }

    // end of FrameInfo.Windowed
  }

  // ===========================================================================

  public final static class FullScreen extends FrameInfo {
    private GraphicsDevice graphicsDevice;
    private DisplayMode displayMode;

    public FullScreen(GraphicsDevice graphicsDevice, DisplayMode displayMode) {
      this(graphicsDevice, displayMode, true);
    }

    public FullScreen(GraphicsDevice graphicsDevice, DisplayMode displayMode, boolean fullScreen) {
      super(null);
      this.graphicsDevice = graphicsDevice;
      this.displayMode = displayMode;
      setFullScreen(fullScreen);
    }

    public final DisplayMode getDisplayMode() {
      return displayMode;
    }

    public final GraphicsDevice getGraphicsDevice() {
      return graphicsDevice;
    }

    @Override
    public final void setWindowSize(int width, int height) {
      if (!isFullScreen()) {
        super.setWindowSize(width, height);
      }
      // else is discarded to keep displayMode/windowSize consistency.
    }

    /**
     * Caution: mixing swing with heavyweight components on a Win32 system may cause nothing to be drawn on the canvas/drawable in fullscreen mode.
     * This is due to some interactions with DirectDraw. Disable it via the command-line -Dsun.java2d.noddraw=true
     */
    @Override
    public final void tryToApply(Window window) {
      if (isFullScreen()) {
        if (graphicsDevice.isFullScreenSupported()) {
          tryToUndecorate(window, true);
          graphicsDevice.setFullScreenWindow(window);

          // Now good luck if the window isn't in fullScreen here...
          if (graphicsDevice.isDisplayChangeSupported()) {
            graphicsDevice.setDisplayMode(displayMode);
            return;
          }
        }
      }

      // fallback behaviour
      Dimension windowSize = new Dimension(displayMode.getWidth(), displayMode.getHeight());
      tryToApply(window, windowSize, isFullScreen(), getTitle(), getIcon());
    }

    @Override
    public String toString() {
      return toString(displayMode);
    }

    // end of FrameInfo.FullScreen
  }

  // end of FrameInfo
}
