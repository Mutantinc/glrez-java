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
import org.mars.toolkit.realtime.graph.TextureId;

import com.chiptune.glrez.ColorUtils;
import com.chiptune.glrez.data.Resources;

public class Title extends Geometry {

  public Title(GLDemo demo) {
    super(demo);
  }

  public void genQuad(Situation sit, float d, Color c) {
    GL2 gl = demo.getGL();

    gl.glLoadIdentity();
    gl.glTranslatef(sit.getPosX(), sit.getPosY(), sit.getPosZ());

    gl.glRotatef(sit.getAngleX(), 1.0f, 0.0f, 0.0f);
    gl.glRotatef(sit.getAngleY(), 0.0f, 1.0f, 0.0f);
    gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);

    ColorUtils.calcRGBA(gl, c);

    gl.glBegin(GL2GL3.GL_QUADS);
    gl.glTexCoord2f(0.0f, 1.0f);
    gl.glVertex3f(-d, d, 0.0f);
    gl.glTexCoord2f(1.0f, 1.0f);
    gl.glVertex3f(d, d, 0.0f);
    gl.glTexCoord2f(1.0f, 0.0f);
    gl.glVertex3f(d, -d, 0.0f);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex3f(-d, -d, 0.0f);
    gl.glEnd();
  }


  public void drawTitle(Situation sit, Jump jump, Cube cube) {
    GL2 gl = demo.getGL();

    gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texTitle.getId());
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
    sit.setAngleX((float) (30.0 * Math.cos((sit.getRotationY() * 2) * PID)));
    sit.setAngleY((float) (30.0 * Math.cos((sit.getRotationY() * 2) * PID)));
    sit.setAngleZ(0.0f);
    sit.setPosX(0.0f);
    sit.setPosY((float) (-(cube.getPosY() + jump.getJumpY() * Math.cos(jump.getJumpAngle())) * 0.2));
    sit.setPosZ(-2.0f);
    genQuad(sit, 0.7f, ColorUtils.get(224, 224, 224, 0.5f));
    gl.glBindTexture(GL.GL_TEXTURE_2D, TextureId.ID_NONE);
    sit.setAngleX(sit.getAngleX() + 30.0f);
    sit.setAngleY(sit.getAngleY() + 30.0f);
    gl.glLoadIdentity();
    gl.glTranslatef(sit.getPosX(), sit.getPosY(), sit.getPosZ());
    gl.glRotatef(sit.getAngleX(), 1.0f, 0.0f, 0.0f);
    gl.glRotatef(sit.getAngleY(), 0.0f, 1.0f, 0.0f);
    gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);
  }
}
