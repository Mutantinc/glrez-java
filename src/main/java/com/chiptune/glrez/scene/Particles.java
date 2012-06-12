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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import org.mars.demo.graph.GLDemo;

import com.chiptune.glrez.ColorUtils;
import com.chiptune.glrez.data.Resources;

public class Particles extends Geometry implements Iterable<Particle> {

  private int r_d7; // display list 7 (particle)
  private final static int count = 512;
  private final List<Particle> particles = new ArrayList<Particle>();

  public Particles(GLDemo demo) {
    super(demo);
  }
  
  /**
   * Emulation of C's rand()
   */
  private final static int cRand() {
    return (int) (32768 * (Math.random() - 0.5));
  }

  
  @Override
  public void init() { // used to be genParticles
    GL2 gl = demo.getGL();
    
    genParticle(gl);
    
    for (int i = 0; i < count; i++) {
      float anlge = (float) (cRand() % 3600 / 10.0);
      float dist = (float) ((1700 + cRand() % 1000) / 100.0);
      float y = (float) ((-1700 + cRand() % 3400) / 100.0);
      int z = cRand() % 360;

      int r = 128 + cRand() % 96;
      int g = 128 + cRand() % 96;
      int b = 128 + cRand() % 96;
      Color c = new Color(r, g, b);

      particles.add( new Particle(dist, y, anlge, z, c));
    }
  }
  
  @Override
  public void clear() {
    GL2 gl = demo.getGL();
    gl.glDeleteLists(r_d7, 5);
    particles.clear();
  }

  
  private void genParticle(GL2 gl) {
    float d = 1.5f;
    float a = 0.025f;
    float b = 0.975f;

    r_d7 = gl.glGenLists(1);
    gl.glNewList(r_d7, GL2.GL_COMPILE);
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(a, b);
    gl.glVertex3f(-d, -d, 0.0f);
    gl.glTexCoord2f(b, b);
    gl.glVertex3f(0.0f, -d, 0.0f);
    gl.glTexCoord2f(b, a);
    gl.glVertex3f(0.0f, 0.0f, 0.0f);
    gl.glTexCoord2f(a, a);
    gl.glVertex3f(-d, 0.0f, 0.0f);
    gl.glTexCoord2f(b, b);
    gl.glVertex3f(0.0f, -d, 0.0f);
    gl.glTexCoord2f(a, b);
    gl.glVertex3f(d, -d, 0.0f);
    gl.glTexCoord2f(a, a);
    gl.glVertex3f(d, 0.0f, 0.0f);
    gl.glTexCoord2f(b, a);
    gl.glVertex3f(0.0f, 0.0f, 0.0f);
    gl.glTexCoord2f(a, b);
    gl.glVertex3f(-d, d, 0.0f);
    gl.glTexCoord2f(b, b);
    gl.glVertex3f(0.0f, d, 0.0f);
    gl.glTexCoord2f(b, a);
    gl.glVertex3f(0.0f, 0.0f, 0.0f);
    gl.glTexCoord2f(a, a);
    gl.glVertex3f(-d, 0.0f, 0.0f);
    gl.glTexCoord2f(b, b);
    gl.glVertex3f(0.0f, d, 0.0f);
    gl.glTexCoord2f(a, b);
    gl.glVertex3f(d, d, 0.0f);
    gl.glTexCoord2f(a, a);
    gl.glVertex3f(d, 0.0f, 0.0f);
    gl.glTexCoord2f(b, a);
    gl.glVertex3f(0.0f, 0.0f, 0.0f);
    gl.glEnd();
    gl.glEndList();
  }

  public void drawParticles(Situation sit, Jump jump, Rez rez, Cube cube) {
    GL2 gl = demo.getGL();

    gl.glDepthMask(false); // do not write z-buffer
    gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texParticle.getId());
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
    sit.setAngleX(0.0f);
    sit.setAngleY(0.0f);
    for (Particle p : particles) {
      if (rez.isEnabled()) {
        sit.setPosY((float) (p.getPosY() + 0.25 * Math.cos((p.getAngle() + sit.getRotationY()) * 16.0 * PID)));
      }
      else {
        sit.setPosY((float) (cube.getPosY() + jump.getJumpY() * Math.cos(jump.getJumpAngle()) + ((27.0 - p.getDistance()) / 16.0) * Math.cos((sit.getRotationY() * 6.0 + p.getDistance() * 16.0) * PID)));
      }
      sit.setPosX((float) (p.getDistance() * Math.cos((p.getAngle() + sit.getRotationY()) * PID)));
      sit.setPosZ((float) (cube.getPosZ() + p.getDistance() * Math.sin((p.getAngle() + sit.getRotationY()) * PID)));
      sit.setAngleZ(p.getPosZ());
      gl.glLoadIdentity();
      gl.glTranslatef(sit.getPosX(), sit.getPosY(), sit.getPosZ());
      gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);
      ColorUtils.calcRGBA(gl, p.getColor(), (float) ((-sit.getPosZ() - 2.0) / 6.0));
      gl.glCallList(r_d7);
    }
    gl.glDepthMask(true); // write z-buffer
  }


  @Override
  public Iterator<Particle> iterator() {
    return particles.iterator();
  }
}
