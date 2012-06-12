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
import java.awt.Dimension;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.gl2.GLUgl2;

import org.mars.demo.graph.GLDemo;
import org.mars.demo.graph.GLTextureLoader;
import org.mars.toolkit.realtime.graph.TextureId;

import com.chiptune.glrez.ColorUtils;
import com.chiptune.glrez.data.Resources;

public class Scene {

  private GLDemo demo;
  private Situation sit;
  private Jump jump;
  private Zoom zoom;
  
  private Rez rez;
  private Cube cube;
  private Glenz glenz;
  private Particles particles;
  private Mapping mapping;
  private Ring ring;
  private Title title;
  private Liner liner;
  
  private float m_a; // rotation angle

  private final static float /* GLfloat */fov = 80.0f; // field of view angle
  private final static float /* GLfloat */nearplane = 1.0f; // nearplane
  private final static float /* GLfloat */farplane = 200.0f;// farplane
  
  
  public Scene(GLDemo demo) {
    this.demo = demo;
    
    sit = new Situation();
    jump = new Jump();
    zoom = new Zoom();
    
    rez = new Rez(demo);
    cube = new Cube(demo);
    glenz = new Glenz(demo);
    particles = new Particles(demo);
    mapping = new Mapping();
    ring = new Ring(demo);
    title = new Title(demo);
    liner = new Liner(demo);
    liner.setTxtPresent();
  }
  
  public void init() throws IOException {
    GL2 gl = demo.getGL();
    GLUgl2 glu = demo.getGLU();

    rez.init();
    particles.init();
    ring.init();

    Resources.bindTextures(gl, glu);
  }
  
  public void clear() {
    GL2 gl = demo.getGL();

    rez.clear();
    particles.clear();
    ring.clear();

    GLTextureLoader.deleteAll(gl);
  }

  public GLDemo getDemo() {
    return demo;
  }

  public Situation getSituation() {
    return sit;
  }
  
  public Mapping getMapping() {
    return mapping;
  }

  public Rez getRez() {
    return rez;
  }

  public Cube getCube() {
    return cube;
  }
  
  public Particles getParticles() {
    return particles;
  }

  public Ring getRing() {
    return ring;
  }

  public Title getTitle() {
    return title;
  }

  public Zoom getZoom() {
    return zoom;
  }

  public Glenz getGlenz() {
    return glenz;
  }

  public Liner getLiner() {
    return liner;
  }

  public Jump getJump() {
    return jump;
  }

  public void init2D() {
    Dimension size = demo.getFrameInfo().getCanvasSize();
    GL2 gl = demo.getGL();
    GLUgl2 glu = demo.getGLU();

    gl.glViewport(0, 0, size.width, size.height); // reset viewport
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION); // select projection matrix
    gl.glLoadIdentity(); // reset projection matrix
    glu.gluOrtho2D(0, size.width, size.height, 0); // init orthographic mode
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW); // select modelview matrix
    gl.glLoadIdentity(); // reset modelview matrix
  }

  public void init3D() {
    Dimension size = demo.getFrameInfo().getCanvasSize();
    GL2 gl = demo.getGL();
    GLUgl2 glu = demo.getGLU();

    gl.glViewport(0, 0, size.width, size.height); // reset viewport
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION); // select projection matrix
    gl.glLoadIdentity(); // reset projection matrix
    glu.gluPerspective(fov, ((float) size.width / (float) size.height), nearplane, farplane); // aspect ratio
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW); // select modelview matrix
    gl.glLoadIdentity(); // reset modelview matrix
  }

  
  public void draw(Color fogColor, float t_g) {
    GL2 gl = demo.getGL();

    // compute color
    final int b_r = 128; // red value
    final int b_g = 128; // green value
    final int b_b = 128; // blue value

    int nr = (int) (b_r + 16.0 * Math.cos(32.0 * t_g * Geometry.PID));
    int ng = (int) (b_g + 16.0 * Math.sin(16.0 * t_g * Geometry.PID));
    int nb = (int) (b_b + 16.0 * Math.cos(48.0 * t_g * Geometry.PID));
    Color n = new Color(nr, ng, nb);

    
    // clear screen and depth buffer
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    // background color
    float[] f_c = fogColor.getRGBComponents(null);
    gl.glClearColor(f_c[0], f_c[1], f_c[2], f_c[3]);
    // light
    fxLight(n);
    // fog
    fxFog(fogColor);
    // disable z-buffer, enable blend
    gl.glDisable(GL.GL_DEPTH_TEST); // disable z-buffer
    gl.glDepthMask(false); // do not write z-buffer
    gl.glEnable(GL.GL_BLEND); // enable blending

    // move scene
    moveScene(t_g);
    // rez
    if (rez.isEnabled()) {
      rez.drawRez(sit, cube, liner, n);
    }
    // 2d mode
    init2D();
    // draw 2d
    gl.glEnable(GL.GL_TEXTURE_2D); // enable texture mode
    if (!rez.isEnabled()) {
      gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texEnvmap.getId());
      gl.glBlendFunc(GL.GL_ONE, GL.GL_DST_COLOR);
      ColorUtils.calcRGBA(gl, n, 0.5f);
    }
    else {
      gl.glBindTexture(GL.GL_TEXTURE_2D, TextureId.ID_NONE);
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_COLOR);
      ColorUtils.calcRGBA(gl, n, 0.375f);
    }
    // background
    drawBackground();
    // 3d mode
    init3D();
    // mapping
    if (mapping.isEnabled()) {
      m_a = (float) (32.0 * (t_g - mapping.getSynchroTime()));
    }      
    // move cube and title
    if (cube.getJump() > 0) {
      cube.setJumpAngle((float) (32.0 * (t_g - cube.getSynchroTime())));
      cube.setJump((float) (0.2 - 0.8 * (t_g - cube.getSynchroTime())));
    }
    if (jump.isEnabled() && (jump.getJumpY() > 0)) {
      jump.setJumpAngle((float) (5.0 * (t_g - jump.getSynchroTime())));
      jump.setJumpY((float) (-cube.getPosY() - 7.0 * (t_g - jump.getSynchroTime())));
    }
    // enable lighting
    gl.glEnable(GLLightingFunc.GL_LIGHTING);
    // cube
    cube.drawCube(sit, jump, zoom, m_a);
    // gl.glenz
    if (glenz.isEnabled()) {
      glenz.drawGlenz(sit, jump, cube, n);
    }
    // disable lighting
    gl.glDisable(GLLightingFunc.GL_LIGHTING);
    // disable fog
    gl.glDisable(GL2ES1.GL_FOG);
    // enable texture mode
    gl.glEnable(GL.GL_TEXTURE_2D); // enable texture mode
    // particles
    if (particles.isEnabled()) {
      particles.drawParticles(sit, jump, rez, cube);
    }
    // disable z-buffer
    gl.glDisable(GL.GL_DEPTH_TEST); // disable z-buffer
    gl.glDepthMask(false); // do not write z-buffer
    // title
    if (title.isEnabled()) {
      title.drawTitle(sit, jump, cube);
      ring.drawRing();
    }
    // liner
    if (liner.isEnabled()) {
      liner.drawLiner(sit, t_g);
    }

    // 2d mode
    init2D();
    // alpha mask
    drawAlphaMask();
    // disable blending & texture mode
    gl.glDisable(GL.GL_TEXTURE_2D); // disable texture mode
    gl.glDisable(GL.GL_BLEND); // disable blending
    // enable z-buffer
    gl.glDepthMask(true); // write z-buffer
    gl.glEnable(GL.GL_DEPTH_TEST); // enable z-buffer
    // compute rotation
    sit.setRotationY((zoom.getMainAngle() * (t_g - zoom.getSynchroTime())));
  }

  private void drawAlphaMask() {
    Dimension size = demo.getFrameInfo().getCanvasSize();
    GL2 gl = demo.getGL();

    // draw 2d
    gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texScreen.getId());
    gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_ALPHA);

    
    //this isn't in the original intro: scaling the screen "grain" accoring to the screen resolution
    final float blocksX = 784/2, blocksY = (562-40)/2; //that was the original resolution where the 2*2 tex was mapped
    final int blockSize = Math.max(2, Math.round( Math.max(size.width / blocksX, size.height / blocksY)));
    final int borderBlocks = 20;
    final int hBorder = borderBlocks * blockSize; //and there are 2 borders
    
    int wTex = (int)Math.ceil(size.width  / (double)blockSize);
    int hBlocks = (int)Math.ceil(size.height / (double)blockSize);
    int hTex = hBlocks - 2 * borderBlocks; 
    final int width =  blockSize * wTex;
    final int height = blockSize * hBlocks;

    ColorUtils.calcRGBA(gl, 255, 255, 255, 0.75f);
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex2i(0, hBorder);
    gl.glTexCoord2f(wTex, 0.0f);
    gl.glVertex2i(width, hBorder);
    gl.glTexCoord2f(wTex, hTex);
    gl.glVertex2i(width, height - hBorder);
    gl.glTexCoord2f(0.0f, hTex);
    gl.glVertex2i(0, height - hBorder);
    gl.glEnd();
  
    //borders
    for (int i = 0; i < borderBlocks; i++) {
      ColorUtils.calcRGBA(gl, 255, 255, 255, (float) (0.75 - 0.75 / borderBlocks * i));
      gl.glBegin(GL2.GL_QUADS);
      gl.glTexCoord2f(0.0f, 0.0f);
      gl.glVertex2i(0, hBorder - (i+1) * blockSize);
      gl.glTexCoord2f(wTex, 0.0f);
      gl.glVertex2i(width, hBorder - (i+1) * blockSize);
      gl.glTexCoord2f(wTex, 1.0f);
      gl.glVertex2i(width, hBorder - i * blockSize);
      gl.glTexCoord2f(0.0f, 1.0f);
      gl.glVertex2i(0, hBorder - i * blockSize);
      gl.glEnd();
      
      gl.glBegin(GL2.GL_QUADS);
      gl.glTexCoord2f(0.0f, 0.0f);
      gl.glVertex2i(0, height - hBorder + i * blockSize);
      gl.glTexCoord2f(wTex, 0.0f);
      gl.glVertex2i(width, height - hBorder + i * blockSize);
      gl.glTexCoord2f(wTex, 1.0f);
      gl.glVertex2i(width, height - hBorder + (i+1) * blockSize);
      gl.glTexCoord2f(0.0f, 1.0f);
      gl.glVertex2i(0, height - hBorder + (i+1) * blockSize);
      gl.glEnd();
    }
  }

  private void moveScene(float t_g) {
    if (zoom.isEnabled()) {
      zoom.setZoomRotation((float) (24.0 * (t_g - zoom.getSynchroTime())));
      if (zoom.getZoom() > 5.0f) {
        zoom.setZoom((float) (10.0 - 1.0 * (t_g - zoom.getSynchroTime())));
      }
      zoom.setMainAngle(24.0f);
    }
    cube.setPosZ((float) (-15.0 + zoom.getZoom() * Math.cos(zoom.getZoomAngle() * 2.0 * Geometry.PID)));
  }

  private void fxFog(Color f_c) {
    GL2 gl = demo.getGL();

    gl.glFogi(GL2ES1.GL_FOG_MODE, GL.GL_LINEAR); // fog mode
    gl.glFogfv(GL2ES1.GL_FOG_COLOR, f_c.getRGBColorComponents(null), 0); // fog color
    gl.glFogf(GL2ES1.GL_FOG_DENSITY, 0.5f); // fog density
    gl.glHint(GL2ES1.GL_FOG_HINT, GL.GL_DONT_CARE); // fog hint value
    gl.glFogf(GL2ES1.GL_FOG_START, 20.0f); // fog start depth
    gl.glFogf(GL2ES1.GL_FOG_END, 65.0f); // fog end depth
    gl.glEnable(GL2ES1.GL_FOG); // enable fog
  }

  private void fxLight(Color n) {
    GL2 gl = demo.getGL();

    float[] l_a = new float[] { (float) (-0.5 + ColorUtils.CR * n.getRed()), (float) (-0.5 + ColorUtils.CR * n.getGreen()), (float) (-0.5 + ColorUtils.CR * n.getBlue()), 1.0f };
    float[] l_d = new float[] { 0.75f, 0.75f, 0.75f, 1.0f };
    float[] l_p = new float[] { 0.0f, -50.0f, (float) (cube.getPosZ() - 75.0), 1.0f };
    gl.glLoadIdentity();
    gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_AMBIENT, l_a, 0); // ambient light color
    gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_DIFFUSE, l_d, 0); // diffuse light color
    gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_POSITION, l_p, 0); // light position
    gl.glEnable(GLLightingFunc.GL_LIGHT1);
  }

  private void drawBackground() {
    Dimension size = demo.getFrameInfo().getCanvasSize();
    GL2 gl = demo.getGL();
    
    gl.glBegin(GL2.GL_QUADS);
    gl.glTexCoord2f(0.0f, 1.0f);
    gl.glVertex2i(0, 0);
    gl.glTexCoord2f(1.0f, 1.0f);
    gl.glVertex2i(size.width, 0);
    gl.glTexCoord2f(1.0f, 0.0f);
    gl.glVertex2i(size.width, size.height);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex2i(0, size.height);
    gl.glEnd();
    // enable z-buffer, disable blend
    gl.glDisable(GL.GL_BLEND); // disable blending
    gl.glDepthMask(true); // write z-buffer
    gl.glEnable(GL.GL_DEPTH_TEST); // enable z-buffer
  }
}
