/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.display;

import org.mars.toolkit.realtime.graph.FrameInfo;

public interface DisplayModeSelectionListener {
  public void fullScreenModeSelected(FrameInfo.FullScreen frameInfo);

  public void windowedModeSelected(FrameInfo.Windowed frameInfo);

  public void fullScreenSelected(boolean fullScreen);
}
