/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.display;

import java.awt.DisplayMode;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.swing.JComboBox;

import org.mars.toolkit.realtime.graph.FrameInfo;

public final class DisplayModeComboBox extends JComboBox<Object> implements DisplayModeSelectionListener {
  private static final long serialVersionUID = 8706710284108590900L;

  private final DisplayModeInfo displayModeInfo;

  private final DisplayModeListModel displayModesListModel;
  private final DisplayModeListModel windowedModesListModel;

  private final Listener itemListener;

  private DisplayMode displayModeSelected;
  private FrameInfo.Windowed windowedModeSelected;

  public DisplayModeComboBox(DisplayModeInfo displayModeInfo) {
    this.displayModeInfo = displayModeInfo;

    setRenderer(new DisplayModeListCellRenderer());

    displayModesListModel = new DisplayModeListModel();
    DisplayMode[] displayModes = displayModeInfo.getDisplayModes();
    if (displayModes != null) {
      displayModesListModel.setListData(displayModes);
    }

    windowedModesListModel = new DisplayModeListModel();
    FrameInfo.Windowed[] windowedModes = displayModeInfo.getWindowedModes();
    if (windowedModes != null) {
      windowedModesListModel.setListData(windowedModes);
    }

    itemListener = new Listener();
    addItemListener(itemListener);
  }
  
  protected DisplayModeInfo getDisplayModeInfo() {
    return displayModeInfo;
  }

  protected void setDisplayModeSelected(DisplayMode displayModeSelected) {
    this.displayModeSelected = displayModeSelected;
  }

  protected void setWindowedModeSelected(FrameInfo.Windowed windowedModeSelected) {
    this.windowedModeSelected = windowedModeSelected;
  }

  private class Listener implements ItemListener {
    protected Listener() {/*def ctor*/}
    
    @Override
    public void itemStateChanged(ItemEvent e) {
      if (e != null && e.getStateChange() == ItemEvent.SELECTED) {
        Object value = getSelectedItem();
        DisplayModeInfo displayModeInfo = getDisplayModeInfo();
        if (displayModeInfo.isFullScreenSelected()) {
          DisplayMode displayMode = (DisplayMode) value;
          setDisplayModeSelected(displayMode);
          displayModeInfo.setDisplayModeSelected(displayMode);
        }
        else {
          FrameInfo.Windowed windowedMode = (FrameInfo.Windowed) value;
          setWindowedModeSelected(windowedMode);
          displayModeInfo.setWindowedModeSelected(windowedMode);
        }
      }
    }
  }

  @Override
  public void fullScreenModeSelected(FrameInfo.FullScreen frameInfo) {
    DisplayMode displayMode = null;
    if (frameInfo != null) {
      displayMode = frameInfo.getDisplayMode();
    }
    displayModeSelected(displayMode);
  }

  protected void displayModeSelected(DisplayMode displayMode) {
    displayModeSelected = displayMode;
    setSelectedItem(displayMode);
  }

  @Override
  public void windowedModeSelected(FrameInfo.Windowed windowedMode) {
    windowedModeSelected = windowedMode;
    setSelectedItem(windowedMode);
  }

  @Override
  public void fullScreenSelected(boolean fullScreen) {
    removeItemListener(itemListener);
    if (fullScreen) {
      setModel(displayModesListModel);
      setSelectedItem(displayModeSelected);
    }
    else {
      setModel(windowedModesListModel);
      setSelectedItem(windowedModeSelected);
    }
    addItemListener(itemListener);
  }

  /**
   * Beware who's listening
   */
  public void selectDefaultModes(boolean fullScreen) {
    fullScreenSelected(fullScreen);

    if (displayModeInfo != null) {
      if(displayModeInfo.isFullScreenSupported()) {
        displayModeInfo.setFullScreenSelected(true); // to stay consistent vs listeners
        DisplayMode currentDisplayMode = displayModeInfo.getGraphicsDevice().getDisplayMode();
        DisplayMode[] displayModes = displayModeInfo.getDisplayModes();
        if (displayModes != null && displayModes.length > 0) {
          if(Arrays.asList(displayModes).contains(currentDisplayMode)) {
            displayModeSelected(currentDisplayMode);
          }
          else {
            displayModeSelected(displayModes[displayModes.length - 1]);
          }
        }
      }

      displayModeInfo.setFullScreenSelected(false); // to stay consistent vs listeners
      FrameInfo.Windowed[] windowedModes = displayModeInfo.getWindowedModes();
      if (windowedModes != null && windowedModes.length > 0) {
        windowedModeSelected(windowedModes[windowedModes.length - 1]);
      }
      
      //back to wished mode
      displayModeInfo.setFullScreenSelected(fullScreen);
    }
  }
}
