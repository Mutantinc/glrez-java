/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.display;

import java.awt.Component;
import java.awt.DisplayMode;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.mars.toolkit.realtime.graph.FrameInfo;

public class DisplayModeListCellRenderer extends DefaultListCellRenderer {
  private static final long serialVersionUID = 1L;

  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    if (value instanceof DisplayMode) {
      value = FrameInfo.toString((DisplayMode) value);
    }
    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
  }
}
