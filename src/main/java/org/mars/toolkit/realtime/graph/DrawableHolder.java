/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.realtime.graph;

import java.awt.Component;
import java.awt.Dimension;

public interface DrawableHolder {

  public FrameInfo getFrameInfo();
  public Component getDrawable();
  public Dimension getSize();
  public void display();
  public void close();
}
