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

import org.mars.demo.graph.GLDrawableHolder;
import org.mars.toolkit.realtime.graph.DrawableHolder;
import org.mars.toolkit.realtime.graph.FrameInfo;

public class GLRezApplet extends GLRezHeadless {

  private GLDrawableHolder drawableHolder;
  
  public GLRezApplet() {
    super();
  }

  /**
   * The "window" is already open, it's the Applet itself, so we're just creating a DrawableHolder containing a GLCanvas
   * In case the user would try to switch to fullscreen, just return the same object.
   */
  @Override
  protected DrawableHolder openWindow(FrameInfo fi) throws Exception {
    if(drawableHolder == null) {
      drawableHolder = new GLDrawableHolder(fi, this);
    }
    return drawableHolder;
  }
}
//Rez' asshole is here