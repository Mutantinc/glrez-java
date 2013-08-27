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


public class Zoom extends Toggle implements Synchronizable {

  private float z_z = 10.0f;// zoom
  private float z_r = 0.0f; // zoom angle
  private float z_a = 0.0f; // main angle
  private float z_t; // synchro time


  public float getZoom() {
    return z_z;
  }

  public float getZoomAngle() {
    return z_r;
  }

  public float getMainAngle() {
    return z_a;
  }

  @Override
  public float getSynchroTime() {
    return z_t;
  }

  public void setZoom(float z_z) {
    this.z_z = z_z;
  }

  public void setZoomRotation(float z_r) {
    this.z_r = z_r;
  }

  public void setMainAngle(float z_a) {
    this.z_a = z_a;
  }

  @Override
  public void setSynchroTime(float z_t) {
    this.z_t = z_t;
  }
}
