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

import java.awt.Color;

public final class Particle {
  private float d; // distance/center
  private float y; // height

  private float a; // angle
  private int z; // angle

  private Color color;

  public Particle(float d, float y, float a, int z, Color color) {
    setDistance(d);
    setPosY(y);

    setAngle(a);
    setPosZ(z);

    setColor(color);
  }

  public float getAngle() {
    return a;
  }

  public void setAngle(float a) {
    this.a = a;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public float getDistance() {
    return d;
  }

  public void setDistance(float d) {
    this.d = d;
  }

  public float getPosY() {
    return y;
  }

  public void setPosY(float y) {
    this.y = y;
  }

  public int getPosZ() {
    return z;
  }

  public void setPosZ(int z) {
    this.z = z;
  }
}
