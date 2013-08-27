/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.display;

import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class DisplayModeListModel implements ComboBoxModel<Object> {
  private List<?> list;
  private Object selectedItem;

  public void setListData(Object[] displayModes) {
    list = Arrays.asList(displayModes);
  }

  @Override
  public Object getElementAt(int index) {
    return list.get(index);
  }

  @Override
  public int getSize() {
    if (list != null) {
      return list.size();
    }
    else {
      return 0;
    }
  }

  @Override
  public Object getSelectedItem() {
    return selectedItem;
  }

  @Override
  public void setSelectedItem(Object selectedItem) {
    this.selectedItem = selectedItem;
  }

  @Override
  public void addListDataListener(ListDataListener listener) {/* nothing */
  }

  @Override
  public void removeListDataListener(ListDataListener listener) {/* nothing */
  }

}
