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
package com.chiptune.glrez;

import java.awt.Color;

import javax.media.opengl.GL2;

public class ColorUtils {
  public final static double CR = 1.0/255.0; // Float/RGB color ratio
  
  public static void calcRGBA(GL2 gl, int r, int g, int b, float l) {
    float fr = safeVal((float) (CR * r));
    float fg = safeVal((float) (CR * g));
    float fb = safeVal((float) (CR * b));
    gl.glColor4f(fr, fg, fb, l);
  }

  public static void calcRGBA(GL2 gl, Color c, float l) {
    calcRGBA(gl, c.getRed(), c.getGreen(), c.getBlue(), l);
  }
  
  public static void calcRGBA(GL2 gl, Color c) {
    gl.glColor4fv(c.getRGBComponents(null), 0);
  }

  public static Color get(int r, int g, int b, float a) {
    return new Color((float)(CR * r), (float)(CR * g), (float)(CR * b), a);
  }

  public static Color safe(float r, float g, float b, float a) {
    return new Color( safeVal(r), safeVal(g), safeVal(b), safeVal(a));
  }

  public static Color safe(int r, int g, int b, float a) {
    return new Color( safeVal((float)(CR*r)), safeVal((float)(CR*g)), safeVal((float)(CR*b)), safeVal(a));
  }

  public static float safeVal(float f) {
    if(f < 0.f) {
      f = 0.f;
    }
    else if(f > 1.f) {
      f = 1.f;
    }
    return f;
  }

  public static int safeVal(int i) {
    if(i < 0) {
      i = 0;
    }
    else if(i > 255) {
      i = 255;
    }
    return i;
  }
}