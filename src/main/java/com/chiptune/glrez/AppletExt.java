package com.chiptune.glrez;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;

import org.mars.demo.sound.FmodPlayer;
import org.mars.demo.sound.JoalPlayer;
import org.mars.toolkit.display.DisplayModeChoiceListener;
import org.mars.toolkit.realtime.graph.FrameInfo;
import org.mars.toolkit.realtime.sound.ModulePlayer;


public class AppletExt extends Applet implements DisplayModeChoiceListener {
  private static final long serialVersionUID = 1L;
  
  private OptionsDialog options;
  private boolean optionsChosen;
  private GLRezApplet glRez;

  
  @Override
  public synchronized void init() {
    try {
      Dimension appletSize = getSize();
      FrameInfo fi = new FrameInfo.Windowed(null, null,  appletSize);
      glRez = new GLRezApplet();
      glRez.initGraph(fi);

      setLayout( new BorderLayout());
      add(glRez.getDrawableHolder().getDrawable(), BorderLayout.CENTER);
    }
    catch(Exception e) {
      throw new RuntimeException(e);
    }

    if(!optionsChosen) {
      options = new OptionsDialog(false, false, true, this);
      options.setVisible(true);
    }
  }

  @Override
  public void displayModeChosen(FrameInfo frameInfo) {
    optionsChosen = true;
    start();
  }
  
  /**
   * We're starting from this Applet which is a graphic component,
   * so we're using a separate process to avoid locking issues with Swing.
   * (Else, nothing will display)
   */
  @Override
  public void start() {
    if(optionsChosen) {
      try {
        ModulePlayer mp = options.isFmodSelected() ? new FmodPlayer() : new JoalPlayer();
        glRez.initSound(mp);
        new Thread(glRez).start();
      }
      catch(Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void stop() {
    if(glRez != null) {
      glRez.setInterrupted();
    }
  }

  @Override
  public void destroy() {
    // already done with stop()
  }
}
