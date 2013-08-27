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


public class Jump extends Toggle implements Synchronizable {

  private float j_y = 3.85f;// jump
  private float j_r = 0.0f; // jump angle
  private float j_t; // synchro time


  public float getJumpY() {
    return j_y;
  }

  public float getJumpAngle() {
    return j_r;
  }

  @Override
  public float getSynchroTime() {
    return j_t;
  }
  
  public void setJumpY(float j_y) {
    this.j_y = j_y;
  }

  public void setJumpAngle(float j_r) {
    this.j_r = j_r;
  }

  @Override
  public void setSynchroTime(float j_t) {
    this.j_t = j_t;
  }
}
