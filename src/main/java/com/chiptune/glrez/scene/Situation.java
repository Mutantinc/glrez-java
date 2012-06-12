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

public class Situation {

  private float p_x; // position x
  private float p_y; // position y
  private float p_z; // position z

  private float a_x; // angle x
  private float a_y; // angle y
  private float a_z; // angle z

  private float r_y; // rotation y


  public float getPosX() {
    return p_x;
  }

  public float getPosY() {
    return p_y;
  }

  public float getPosZ() {
    return p_z;
  }

  public float getAngleX() {
    return a_x;
  }

  public float getAngleY() {
    return a_y;
  }

  public float getAngleZ() {
    return a_z;
  }

  public float getRotationY() {
    return r_y;
  }

  public void setPosX(float p_x) {
    this.p_x = p_x;
  }

  public void setPosY(float p_y) {
    this.p_y = p_y;
  }

  public void setPosZ(float p_z) {
    this.p_z = p_z;
  }

  public void setAngleX(float a_x) {
    this.a_x = a_x;
  }

  public void setAngleY(float a_y) {
    this.a_y = a_y;
  }

  public void setAngleZ(float a_z) {
    this.a_z = a_z;
  }

  public void setRotationY(float r_y) {
    this.r_y = r_y;
  }
}
