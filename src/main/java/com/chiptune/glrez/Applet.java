package com.chiptune.glrez;

import java.awt.BorderLayout;
import java.awt.Dimension;

import org.mars.demo.sound.FmodPlayer;
import org.mars.toolkit.realtime.graph.FrameInfo;
import org.mars.toolkit.realtime.sound.ModulePlayer;


public class Applet extends java.applet.Applet {

  private static final long serialVersionUID = 1L;
  private GLRezApplet glRez;
  
  
  @Override
  public void init() {
    Dimension appletSize = getSize();
    FrameInfo fi = new FrameInfo.Windowed(null, null,  appletSize);
    ModulePlayer mp = new FmodPlayer();
    glRez = new GLRezApplet(fi, mp);
    
    setLayout( new BorderLayout());
    add(glRez.getDrawableHolder().getDrawable(), BorderLayout.CENTER);
  }

  /**
   * We're starting from this Applet which is a graphic component,
   * so we're using a separate process to avoid locking issues with Swing.
   * (Else, nothing will display)
   */
  @Override
  public void start() {
    new Thread(glRez).start();
  }

  @Override
  public void stop() {
    glRez.setInterrupted();
  }

  @Override
  public void destroy() {
    // already done with stop()
  }
}
