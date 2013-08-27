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
import javax.media.opengl.GL2GL3;
import javax.media.opengl.fixedfunc.GLLightingFunc;

import org.mars.demo.graph.GLDemo;

import com.chiptune.glrez.ColorUtils;
import com.chiptune.glrez.data.Resources;

public class Cube extends Geometry implements Synchronizable {

  public Cube(GLDemo demo) {
    super(demo);
  }

  /* cube variable */
  //private int m_c; // cube texture (unused in the C version)
  private float c_y = -3.85f; // cube position y
  private float c_z = -15.0f; // cube position z
  private float c_j; // jump
  private float c_r; // jump angle
  private float c_t; // synchro time

  
  
  public float getPosY() {
    return c_y;
  }

  public float getPosZ() {
    return c_z;
  }

  public float getJump() {
    return c_j;
  }

  public float getJumpAngle() {
    return c_r;
  }

  public void setPosY(float c_y) {
    this.c_y = c_y;
  }

  public void setPosZ(float c_z) {
    this.c_z = c_z;
  }

  public void setJump(float c_j) {
    this.c_j = c_j;
  }

  public void setJumpAngle(float c_r) {
    this.c_r = c_r;
  }

  public void genCube(Situation sit, float d, float z, Color k, float m_a) {
    GL2 gl = demo.getGL();
    
    float[] w = k.getRGBComponents(null);
    float t = (float) (3.99 - 4.0 * Math.cos((m_a * z - 4) * PID));
    float c = (float) (t * Math.cos((m_a * z) * PID));
    float s = (float) (t * Math.sin((m_a * z) * PID));
    float x = (float) (0.48);
    float y = (float) (0.48);

    gl.glLoadIdentity();
    gl.glTranslatef(sit.getPosX(), sit.getPosY(), sit.getPosZ());

    gl.glRotatef(sit.getAngleX(), 1.0f, 0.0f, 0.0f);
    gl.glRotatef(sit.getAngleY(), 0.0f, 1.0f, 0.0f);
    gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);

    gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, w, 0);

    cubeSide(d, x, y, c, s);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
  }

  protected void cubeSide(float d, float x, float y, float c, float s) {
    GL2 gl = demo.getGL();

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glNormal3f(0.0f, 0.0f, -1.0f);
    gl.glTexCoord2f(x + c, y - s);
    gl.glVertex3f(-d, d, d);
    gl.glTexCoord2f(x - s, y - c);
    gl.glVertex3f(d, d, d);
    gl.glTexCoord2f(x - c, y + s);
    gl.glVertex3f(d, -d, d);
    gl.glTexCoord2f(x + s, y + c);
    gl.glVertex3f(-d, -d, d);
    gl.glEnd();
  }

  public void drawCube(Situation sit, Jump jump, Zoom zoom, float m_a) {
    GL2 gl = demo.getGL();

    sit.setPosX(0.0f);
    sit.setPosY((float) (c_y + jump.getJumpY() * Math.cos(jump.getJumpAngle()) + c_j * Math.cos(c_r)));
    sit.setPosZ(c_z);
    sit.setAngleX(sit.getRotationY());
    sit.setAngleY((float) (sit.getRotationY() * 2.0));
    sit.setAngleZ((float) (sit.getRotationY() / 2.0));
    gl.glEnable(GL.GL_CULL_FACE); // enable cull face
    gl.glCullFace(GL.GL_FRONT); // cull mode front
    gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texPouet.getId());
    gl.glBlendFunc(GL.GL_ZERO, GL.GL_ONE);
    genCube(sit, 3.0f, 1.0f, ColorUtils.get(160, 160, 160, 1.0f), m_a);
    gl.glDisable(GL.GL_DEPTH_TEST); // disable z-buffer
    gl.glDepthMask(false); // do not write z-buffer
    gl.glEnable(GL.GL_BLEND); // enable blending
    gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texCoeur.getId());
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
    genCube(sit, 3.0f, 0.75f, ColorUtils.get(160, 160, 160, 1.0f), m_a);
    gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texEnvmap.getId());
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
    genEnvmap(sit, 3.0f, ColorUtils.get((int) (96 + zoom.getMainAngle() * 6), (int) (96 + zoom.getMainAngle() * 6), (int) (96 + zoom.getMainAngle() * 6), 0.75f));
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_COLOR);
    genEnvmap(sit, 3.0f, ColorUtils.get(64, 64, 64, 0.75f));
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
    gl.glDisable(GL.GL_CULL_FACE); // disable cull face
    gl.glDisable(GL.GL_TEXTURE_2D); // disable texture mode
    gl.glDepthMask(true); // write z-buffer
    gl.glEnable(GL.GL_DEPTH_TEST); // enable z-buffer
  }

  public void genEnvmap(Situation sit, float d, Color c1) {
    GL2 gl = demo.getGL();

    Color c2 = ColorUtils.safe(c1.getRed() + 32, c1.getGreen(), c1.getBlue() - 32, c1.getAlpha());
    Color c3 = ColorUtils.safe(c1.getRed() - 32, c1.getGreen(), c1.getBlue() + 32, c1.getAlpha());
    float c = 1.0f;
    float s = 1.0f;
    float x = 0.0f;
    float y = 0.0f;

    gl.glLoadIdentity();
    gl.glTranslatef(sit.getPosX(), sit.getPosY(), sit.getPosZ());

    gl.glRotatef(sit.getAngleX(), 1.0f, 0.0f, 0.0f);
    gl.glRotatef(sit.getAngleY(), 0.0f, 1.0f, 0.0f);
    gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);

    gl.glEnable(GL2.GL_TEXTURE_GEN_S);
    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
    gl.glPushMatrix();

    gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, c1.getRGBComponents(null), 0);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, c2.getRGBComponents(null), 0);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, c3.getRGBComponents(null), 0);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);

    gl.glPopMatrix();
    gl.glDisable(GL2.GL_TEXTURE_GEN_S);
    gl.glDisable(GL2.GL_TEXTURE_GEN_T);
  }

  @Override
  public float getSynchroTime() {
    return c_t;
  }

  @Override
  public void setSynchroTime(float c_t) {
    this.c_t = c_t;
  }
}
