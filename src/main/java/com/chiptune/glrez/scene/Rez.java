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

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

import org.mars.demo.graph.GLDemo;

import com.chiptune.glrez.ColorUtils;

public class Rez extends Geometry {
  
  private float a;
  
  private int r_d1; // display list 1
  private int r_d2; // display list 2
  private int r_d3; // display list 3
  private int r_d4; // display list 4
  private int r_d5; // display list 5

  public Rez(GLDemo demo) {
    super(demo);
  }

  @Override
  public void init() { // used to be genRez()
    GL2 gl = demo.getGL();
    
    float d = 1.0f;

    r_d1 = gl.glGenLists(5);

    gl.glNewList(r_d1, GL2.GL_COMPILE);
    genRezR(-d);
    genRezR(d);
    gl.glTranslatef(5.0f, 0.0f, 0.0f);
    genRezE(-d);
    genRezE(d);
    gl.glTranslatef(5.0f, 0.0f, 0.0f);
    genRezZ(-d);
    genRezZ(d);
    gl.glEndList();

    r_d2 = r_d1 + 1;
    gl.glNewList(r_d2, GL2.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, -d);
    gl.glEnd();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(3.0f, -3.0f, d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(3.0f, -3.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(1.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -4.0f, -d);
    gl.glVertex3f(1.0f, -4.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(1.25f, -4.0f, d);
    gl.glVertex3f(4.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -4.0f, -d);
    gl.glVertex3f(1.25f, -4.0f, -d);
    gl.glEnd();

    gl.glEndList();

    r_d3 = r_d2 + 1;
    gl.glNewList(r_d3, GL2.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(3.0f, -3.0f, d);
    gl.glVertex3f(3.0f, -3.0f, -d);
    gl.glVertex3f(4.0f, -3.0f, -d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(3.0f, -5.0f, d);
    gl.glVertex3f(3.0f, -5.0f, -d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(2.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -3.0f, -d);
    gl.glVertex3f(2.0f, -3.0f, -d);
    gl.glVertex3f(1.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(1.0f, -5.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, -d);
    gl.glEnd();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(2.0f, -3.0f, d);
    gl.glVertex3f(2.0f, -3.0f, d);
    gl.glVertex3f(3.0f, -5.0f, -d);
    gl.glVertex3f(2.0f, -3.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -3.0f, -d);
    gl.glVertex3f(4.0f, -3.0f, -d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(2.75f, -1.0f, d);
    gl.glVertex3f(0.0f, -1.0f, d);
    gl.glVertex3f(0.0f, -1.0f, -d);
    gl.glVertex3f(2.75f, -1.0f, -d);
    gl.glEnd();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(4.0f, -1.0f, d);
    gl.glVertex3f(1.25f, -4.0f, d);
    gl.glVertex3f(1.25f, -4.0f, -d);
    gl.glVertex3f(4.0f, -1.0f, -d);
    gl.glEnd();

    gl.glEndList();

    r_d4 = r_d3 + 1;
    gl.glNewList(r_d4, GL2.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(4.0f, -3.0f, -d);
    gl.glVertex3f(4.0f, 0.0f, -d);
    gl.glVertex3f(1.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -5.0f, d);
    gl.glVertex3f(1.0f, -5.0f, -d);
    gl.glVertex3f(1.0f, -3.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(4.0f, -3.0f, -d);
    gl.glVertex3f(4.0f, 0.0f, -d);
    gl.glVertex3f(1.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -4.0f, d);
    gl.glVertex3f(1.0f, -4.0f, -d);
    gl.glVertex3f(1.0f, -3.0f, -d);
    gl.glVertex3f(4.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(4.0f, -4.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, -1.0f, d);
    gl.glVertex3f(4.0f, -1.0f, -d);
    gl.glVertex3f(4.0f, 0.0f, -d);
    gl.glVertex3f(4.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(4.0f, -4.0f, -d);
    gl.glEnd();

    gl.glEndList();

    r_d5 = r_d4 + 1;
    gl.glNewList(r_d5, GL2.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, -d);
    gl.glVertex3f(3.0f, -1.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -4.0f, d);
    gl.glVertex3f(0.0f, -4.0f, -d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(0.0f, -1.0f, d);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, -1.0f, -d);
    gl.glEnd();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, -4.0f, d);
    gl.glVertex3f(2.75f, -1.0f, d);
    gl.glVertex3f(2.75f, -1.0f, -d);
    gl.glVertex3f(0.0f, -4.0f, -d);
    gl.glEnd();

    gl.glEndList();
  }

  @Override
  public void clear() {
    demo.getGL().glDeleteLists(r_d1, 5);
  }

  private void genRezR(float d) {
    GL2 gl = demo.getGL();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(0.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(0.0f, -3.0f, d);
    gl.glVertex3f(0.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(2.0f, -3.0f, d);
    gl.glVertex3f(3.0f, -3.0f, d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(3.0f, -5.0f, d);
    gl.glEnd();
  }

  private void genRezE(float d) {
    GL2 gl = demo.getGL();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -1.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(4.0f, -3.0f, d);
    gl.glVertex3f(0.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(1.0f, -1.0f, d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(0.0f, -3.0f, d);
    gl.glVertex3f(0.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -3.0f, d);
    gl.glVertex3f(1.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(1.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(1.0f, -5.0f, d);
    gl.glEnd();
  }

  private void genRezZ(float d) {
    GL2 gl = demo.getGL();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glVertex3f(0.0f, -0.0f, d);
    gl.glVertex3f(4.0f, -0.0f, d);
    gl.glVertex3f(4.0f, -1.0f, d);
    gl.glVertex3f(0.0f, -1.0f, d);
    gl.glVertex3f(2.75f, -1.0f, d);
    gl.glVertex3f(4.0f, -1.0f, d);
    gl.glVertex3f(1.25f, -4.0f, d);
    gl.glVertex3f(0.0f, -4.0f, d);
    gl.glVertex3f(0.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -4.0f, d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glEnd();
  }
  
  public void dspRez(Situation sit, Color n) {
    GL2 gl = demo.getGL();

    gl.glLoadIdentity();
    gl.glTranslatef(sit.getPosX(), sit.getPosY(), sit.getPosZ());
    gl.glRotatef(sit.getAngleX(), 1.0f, 0.0f, 0.0f);
    gl.glRotatef(sit.getAngleY(), 0.0f, 1.0f, 0.0f);
    gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);
    gl.glTranslatef(-7.0f, 2.5f, 0.0f);

    int r_r = (int) (n.getRed() + sit.getPosX() * 1.5 + 16.0 * Math.cos(a / 8.0));
    int r_g = n.getGreen();
    int r_b = (int) (n.getBlue() + sit.getPosY() * 1.5 - 16.0 * Math.sin(a / 8.0));
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
    ColorUtils.calcRGBA(gl, r_r, r_g, r_b, 0.65f);
    gl.glCallList(r_d1);
    ColorUtils.calcRGBA(gl, r_r + 64, r_g + 64, r_b + 64, 0.65f);
    gl.glCallList(r_d2);
    ColorUtils.calcRGBA(gl, r_r - 64, r_g - 64, r_b - 64, 0.65f);
    gl.glCallList(r_d3);
    ColorUtils.calcRGBA(gl, r_r + 32, r_g + 32, r_b + 32, 0.65f);
    gl.glCallList(r_d4);
    ColorUtils.calcRGBA(gl, r_r - 32, r_g - 32, r_b - 32, 0.65f);
    gl.glCallList(r_d5);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
    ColorUtils.calcRGBA(gl, r_r - 80, r_g - 80, r_b - 80, 0.65f);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);
    gl.glCallList(r_d1);
    gl.glCallList(r_d2);
    gl.glCallList(r_d3);
    gl.glCallList(r_d4);
    gl.glCallList(r_d5);
  }
  
  public void drawRez(Situation sit, Cube cube, Liner liner, Color n) {
    GL2 gl = demo.getGL();

    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_SRC_ALPHA);
    if (liner.isEnabled()) {
      final int r_l = 12; // loop number
      final int r_n = 8; // amount by loop

      for (int i = 0; i < r_n * r_l; i++) {
        // compute values
        a = (float) (i * 270.0 / r_n + sit.getRotationY());
        sit.setPosX((float) (30.0 * Math.cos(a * PID)));
        sit.setPosY((float) (-(r_l / 2) * 6 + (6.0 / r_n) * i));
        sit.setPosZ((float) (cube.getPosZ() + 30.0 * Math.sin(a * PID)));
        sit.setAngleX((float) (0.0));
        sit.setAngleY((float) (-90.0 - a));
        sit.setAngleZ((float) (2.5));
        // display list
        dspRez(sit, n);
      }
    }
    else {
      for (int i = 0; i < 360; i += 12) {
        for (int j = 0; j < 3; j++) {
          // compute values
          sit.setPosX((float) (30.0 * Math.cos((i + sit.getRotationY() / 2.0) * PID)));
          sit.setPosY((float) (30.0 * Math.sin((i + sit.getRotationY() / 2.0) * PID)));
          sit.setPosZ((float) (cube.getPosZ() - 36.0 + 18.0 * j));
          sit.setAngleX((float) (90.0));
          sit.setAngleY((float) (-90.0 + i + sit.getRotationY() / 2.0));
          sit.setAngleZ((float) (90.0));
          // display list
          dspRez(sit, n);
        }
      }
    }
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
  }
}
