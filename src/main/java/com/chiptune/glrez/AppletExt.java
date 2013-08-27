package com.chiptune.glrez;

import java.applet.Applet;



public class AppletExt extends Applet {
  private static final long serialVersionUID = 1L;
  
  @Override
  public void init() {
    // nothing
  }

  @Override
  public void start() {
    new Launcher();
  }

  @Override
  public void stop() {
    // nothing
  }

  @Override
  public void destroy() {
    // already done with stop()
  }
}
