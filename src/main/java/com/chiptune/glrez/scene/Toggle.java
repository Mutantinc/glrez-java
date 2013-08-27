/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a> and <a href="mailto:rez@chiptune.com">Christophe Résigné</a>
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
package com.chiptune.glrez.scene;

public abstract class Toggle {
  
  private boolean enabled;
  
  public boolean isEnabled() {
    return enabled;
  }
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  public void toggle() {
    this.enabled = !enabled;
  }
}
