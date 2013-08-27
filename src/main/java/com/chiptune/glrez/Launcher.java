/*
/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a> and <a href="mailto:rez@chiptune.com">Christophe Résigné</a>
* 
* Java port of the GLRez intro released at the Breakpoint 2005
* @see http://www.pouet.net/prod.php?which=16327
* @see http://www.chiptune.com
* 
* Original C source code on GitHub
* @see https://github.com/chiptune/glrez
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package com.chiptune.glrez;

import org.mars.demo.sound.FmodPlayer;
import org.mars.demo.sound.JoalPlayer;
import org.mars.toolkit.display.DisplayModeChoiceListener;
import org.mars.toolkit.realtime.graph.FrameInfo;
import org.mars.toolkit.realtime.sound.ModulePlayer;

import com.chiptune.glrez.data.Resources;


public final class Launcher extends Thread implements DisplayModeChoiceListener {

  private final OptionsDialog options;
  
  protected Launcher() {
    options = new OptionsDialog(this);
    options.setVisible(true);
  }
  
  @Override
  public void displayModeChosen(final FrameInfo fi) {
    if(fi != null) {
      fi.setTitle("GLREZ");
      fi.setIcon(Resources.readIcon());
      
      ModulePlayer mp = options.isFmodSelected() ? new FmodPlayer() : new JoalPlayer();
      
      try {
        GLRezFrame glRez = new GLRezFrame();
        glRez.initGraphAndSound(fi, mp);
        new Thread(glRez).start();
      }
      catch(Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void main(String... args) {
    new Launcher();
  }
}