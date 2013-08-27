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
