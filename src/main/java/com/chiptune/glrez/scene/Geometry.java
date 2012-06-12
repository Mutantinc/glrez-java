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
package com.chiptune.glrez.scene;

import org.mars.demo.graph.GLDemo;


public abstract class Geometry extends Toggle {

  public final static double PID = Math.PI / 180.0; // radians/degrees ratio
  
  protected GLDemo demo;
  
  public Geometry(GLDemo demo) {
    this.demo = demo;
  }
  
  public GLDemo getDemo() {
    return demo;
  }
  
  public void init() { /*dummy*/ }
  public void clear() { /*dummy*/ }
}
