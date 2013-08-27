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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLLightingFunc;

import org.mars.demo.graph.GLDemo;

import com.chiptune.glrez.ColorUtils;

public class Glenz extends Geometry {
  
  public Glenz(GLDemo demo) {
    super(demo);
  }

  private void genGlenz(Situation sit, Color k) {
    GL2 gl = demo.getGL();

    gl.glLoadIdentity();
    gl.glTranslatef(sit.getPosX(), sit.getPosY(), sit.getPosZ());

    gl.glRotatef(sit.getAngleX(), 1.0f, 0.0f, 0.0f);
    gl.glRotatef(sit.getAngleY(), 0.0f, 1.0f, 0.0f);
    gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);

    glenzSide(k);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
    glenzSide(k);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    glenzSide(k);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
    glenzSide(k);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
    glenzSide(k);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    glenzSide(k);
  }
  
  private void glenzSide(Color k) {
    GL2 gl = demo.getGL();

    float d = 0.5f;
    float[] w = new float[] { 1.0f, 1.0f, 1.0f, k.getAlpha() };
    float[] c = k.getRGBComponents(null);

    gl.glBegin(GL.GL_TRIANGLES);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, w, 0);
    gl.glNormal3f(0.25f, -0.25f, -0.75f);
    gl.glVertex3f(-d, d, d);
    gl.glVertex3f(d, d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, c, 0);
    gl.glNormal3f(-0.25f, -0.25f, -0.75f);
    gl.glVertex3f(d, d, d);
    gl.glVertex3f(d, -d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, w, 0);
    gl.glNormal3f(-0.25f, 0.25f, -0.75f);
    gl.glVertex3f(d, -d, d);
    gl.glVertex3f(-d, -d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, c, 0);
    gl.glNormal3f(0.25f, 0.25f, -0.75f);
    gl.glVertex3f(-d, -d, d);
    gl.glVertex3f(-d, d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glEnd();
  }
  

  public void drawGlenz(Situation sit, Jump jump, Cube cube, Color n) {
    GL2 gl = demo.getGL();
    
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
    final int g_n = 15; // amount
    for (int i = 0; i < g_n; i++) {
      sit.setPosX((float) ((8.0 + 3.0 * Math.cos(sit.getRotationY() * 4.0 * PID)) * Math.cos((360 / g_n * i + sit.getRotationY() * 2.0) * PID)));
      sit.setPosY((float) (cube.getPosY() + jump.getJumpY() * Math.cos(jump.getJumpAngle()) + 3.0 * Math.cos((360 / g_n * i + sit.getRotationY() * 4.0) * PID))); // -cube.getJump()*Math.cos(c_r)
      sit.setPosZ((float) (cube.getPosZ() + (8.0 + 3.0 * Math.cos(sit.getRotationY() * 4.0 * PID)) * Math.sin((360 / g_n * i + sit.getRotationY() * 2.0) * PID)));
      sit.setAngleX((float) (sit.getRotationY() * 3.0 + i * 24.0));
      sit.setAngleY((float) (sit.getRotationY() * 4.0 + i * 24.0));
      sit.setAngleZ((float) (sit.getRotationY() * 2.0 + i * 24.0));
      
      Color k = ColorUtils.safe((int)(n.getRed() / 2.0 + sit.getPosX() * 6),
                                (int)(n.getRed() / 2.0),
                                (int)(n.getRed() / 2.0 - sit.getPosX() * 6),
                                (float) ((-sit.getPosZ() - 2.0) / 6.0));
      genGlenz(sit, k);
    }
  }
}
