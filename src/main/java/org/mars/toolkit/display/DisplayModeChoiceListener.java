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


public interface DisplayModeChoiceListener {
  public void displayModeChosen(FrameInfo frameInfo);
}
