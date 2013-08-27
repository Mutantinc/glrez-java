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

import org.mars.demo.graph.GLFrame;
import org.mars.demo.sound.FmodPlayer;
import org.mars.toolkit.realtime.graph.DrawableHolder;
import org.mars.toolkit.realtime.graph.FrameInfo;
import org.mars.toolkit.realtime.sound.ModulePlayer;

import com.chiptune.glrez.data.Resources;


public class GLRezFrame extends GLRezHeadless {

  public GLRezFrame() {
    super();
  }

  @Override
  protected DrawableHolder openWindow(FrameInfo fi) throws Exception {
    return GLFrame.openWindow(fi, this, this);
  }

  public static void main(String... args) {
    try {
      FrameInfo fi = new FrameInfo.Windowed("GLREZ", Resources.readIcon(), 800, 600, false);
      ModulePlayer mp = new FmodPlayer();      
      GLRezFrame glRez = new GLRezFrame();
      glRez.initGraph(fi);
      glRez.initSound(mp);
      glRez.start();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }    
  }
}
//Rez' asshole is here