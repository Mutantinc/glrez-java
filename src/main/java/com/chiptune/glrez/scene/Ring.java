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

import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

import org.mars.demo.graph.GLDemo;

import com.chiptune.glrez.ColorUtils;

public class Ring extends Geometry {

  private int r_d6; // display list 6 (ring)

  public Ring(GLDemo demo) {
    super(demo);
  }

  @Override
  public void init() { // used to be genRing()
    GL2 gl = demo.getGL();
    
    float min = 0.35f;
    float max = 0.55f;
    float d;

    r_d6 = gl.glGenLists(1);
    gl.glNewList(r_d6, GL2.GL_COMPILE);
    for (int i = 0; i < 360; i += 5) {
      gl.glBegin(GL2GL3.GL_QUADS);
      
      d = max;
      gl.glColor3f(0.0f, 0.0f, 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      d = (float) ((min + max) / 2.0 + (max - min) / 4.0);
      
      gl.glColor3f((float) (96 * ColorUtils.CR), (float) (64 * ColorUtils.CR), (float) (128 * ColorUtils.CR));
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      
      d = (float) ((min + max) / 2.0);
      gl.glColor3f(1.0f, 1.0f, 1.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      
      d = (float) ((min + max) / 2.0 - (max - min) / 4.0);
      gl.glColor3f((float) (96 * ColorUtils.CR), (float) (64 * ColorUtils.CR), (float) (128 * ColorUtils.CR));
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      
      d = min;
      gl.glColor3f(0.0f, 0.0f, 0.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glEnd();
    }
    gl.glEndList();
  }
  
  @Override
  public void clear() {
    GL2 gl = demo.getGL();
    gl.glDeleteLists(r_d6, 1);
  }
  
  public void drawRing() {
    GL2 gl = demo.getGL();
    gl.glCallList(r_d6);
  }
}
