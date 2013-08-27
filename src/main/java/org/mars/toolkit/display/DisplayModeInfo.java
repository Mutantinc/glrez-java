/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.display;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import org.mars.toolkit.realtime.graph.FrameInfo;

public final class DisplayModeInfo {

  private GraphicsDevice graphicsDevice;
  private List<FrameInfo.Windowed> windowedModes = new ArrayList<FrameInfo.Windowed>();

  private DisplayMode displayModeSelected;
  private FrameInfo.FullScreen fullScreenModeSelected;
  private FrameInfo.Windowed windowedModeSelected;
  private boolean fullScreenSelected;

  private List<DisplayModeSelectionListener> selectionListeners = new ArrayList<DisplayModeSelectionListener>();
  private List<DisplayModeChoiceListener> choiceListeners = new ArrayList<DisplayModeChoiceListener>();

  public DisplayModeInfo() {
    this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
  }

  public DisplayModeInfo(GraphicsDevice graphicsDevice) {
    init(graphicsDevice);
  }

  public GraphicsDevice getGraphicsDevice() {
    return graphicsDevice;
  }

  protected void setGraphicsDevice(GraphicsDevice graphicsDevice) {
    if (graphicsDevice == null) {
      throw new NullPointerException("GraphicsDevice null");
    }
    this.graphicsDevice = graphicsDevice;
  }

  private void init(GraphicsDevice graphicsDevice) {
    setGraphicsDevice(graphicsDevice);

    DisplayMode displayMode = graphicsDevice.getDisplayMode();
    int dispWidth = displayMode.getWidth();
    int dispHeight = displayMode.getHeight();

    final Dimension dimensions[] = new Dimension[] { new Dimension(320, 200), // CGA
        new Dimension(640, 480), // VGA
        new Dimension(800, 600), // SVGA
        new Dimension(1024, 768), // XGA
        new Dimension(1280, 1024), // SXGA
        new Dimension(1600, 1200), // UXGA
        new Dimension(2048, 1536), // QXGA
        new Dimension(2560, 2048), // QSXGA
        new Dimension(3200, 2400), // QUXGA
        new Dimension(5120, 4096), // HSXGA
        new Dimension(6400, 4800) // HUXGA my ass
    };

    for (Dimension dimension : dimensions) {
      if (dimension.getWidth() <= dispWidth && dimension.height <= dispHeight) {
        windowedModes.add(new FrameInfo.Windowed(null, null, dimension));
      }
    }
  }

  public boolean isDisplayChangeSupported() {
    if (graphicsDevice != null) {
      return graphicsDevice.isDisplayChangeSupported();
    }
    else {
      return false;
    }
  }

  /**
   * Caution: it will return false in a sandboxed mode (eg. Applet running without all persmissions)
   */
  public boolean isFullScreenSupported() {
    if (graphicsDevice != null) {
      return graphicsDevice.isFullScreenSupported();
    }
    else {
      return false;
    }
  }

  public DisplayMode[] getDisplayModes() {
    if (graphicsDevice != null) {
      return graphicsDevice.getDisplayModes();
    }
    else {
      return null;
    }
  }

  public FrameInfo.Windowed[] getWindowedModes() {
    return windowedModes.toArray(new FrameInfo.Windowed[windowedModes.size()]);
  }

  public FrameInfo getDisplayModeSelected() {
    if (graphicsDevice != null) {
      if (isFullScreenSelected()) {
        return getFullScreenModeSelected();
      }
      else {
        return getWindowedModeSelected();
      }
    }
    else {
      return null;
    }
  }

  public FrameInfo.FullScreen getFullScreenModeSelected() {
    return fullScreenModeSelected;
  }

  public FrameInfo.Windowed getWindowedModeSelected() {
    return windowedModeSelected;
  }

  public void setDisplayModeSelected(DisplayMode mode) {
    if (displayModeSelected != mode) {
      displayModeSelected = mode;

      if (displayModeSelected != null) {
        fullScreenModeSelected = new FrameInfo.FullScreen(graphicsDevice, displayModeSelected);
      }
      else {
        fullScreenModeSelected = null;
      }

      fireDisplayModeSelected();
    }
  }

  public void setWindowedModeSelected(FrameInfo.Windowed mode) {
    if (windowedModeSelected != mode) {
      windowedModeSelected = mode;
      fireWindowedModeSelected();
    }
  }

  /**
   * Caution: fullScreenSelected will remain false if the graphicsDevice doesn't support fullscreen.
   */
  public void setFullScreenSelected(boolean fullScreen) {
    boolean oldSelection = fullScreenSelected;
    fullScreenSelected = fullScreen && isFullScreenSupported();
    if (fullScreenSelected != oldSelection) {
      fireFullScreenWindowedSelection();
    }
  }

  public boolean isFullScreenSelected() {
    return fullScreenSelected;
  }

  public void addChoiceListener(DisplayModeChoiceListener listener) {
    if (listener != null) {
      choiceListeners.add(listener);
    }
  }

  public void removeChoiceListener(DisplayModeChoiceListener listener) {
    if (listener != null) {
      choiceListeners.remove(listener);
    }
  }

  public void addSelectionListener(DisplayModeSelectionListener listener) {
    if (listener != null) {
      selectionListeners.add(listener);
    }
  }

  public void removeSelectionListener(DisplayModeSelectionListener listener) {
    if (listener != null) {
      selectionListeners.remove(listener);
    }
  }

  public void fireFullScreenWindowedSelection() {
    for (DisplayModeSelectionListener listener : selectionListeners) {
      listener.fullScreenSelected(fullScreenSelected);
    }
  }

  public void fireDisplayModeSelected() {
    FrameInfo.FullScreen frameInfo = getFullScreenModeSelected();
    for (DisplayModeSelectionListener listener : selectionListeners) {
      listener.fullScreenModeSelected(frameInfo);
    }
  }

  public void fireWindowedModeSelected() {
    FrameInfo.Windowed frameInfo = getWindowedModeSelected();
    for (DisplayModeSelectionListener listener : selectionListeners) {
      listener.windowedModeSelected(frameInfo);
    }
  }

  public void fireDisplayModeChosen() {
    FrameInfo frameInfo = getDisplayModeSelected();
    for (DisplayModeChoiceListener listener : choiceListeners) {
      listener.displayModeChosen(frameInfo);
    }
  }

  public void fireDisplayModeUnchosen() {
    for (DisplayModeChoiceListener listener : choiceListeners) {
      listener.displayModeChosen(null);
    }
  }

}
