package com.rez.glrez;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import org.jouvieje.Fmod.Fmod;
import org.jouvieje.Fmod.Init;
import org.jouvieje.Fmod.Exceptions.InitException;
import org.jouvieje.Fmod.Structures.FMUSIC_MODULE;
import org.mars.toolkit.demo.FrameInfo;
import org.mars.toolkit.demo.Texture;
import org.mars.toolkit.demo.jogl.GLTextureLoader;

public final class GLRez extends GLRezVars
{
  // textures
  private final static String resPath = "/com/rez/glrez/resources/";
  private final Texture texPouet = new Texture(resPath + "pouet.gif", false, true);
  private final Texture texCoeur = new Texture(resPath + "coeur.gif", false, true);
  private final Texture texEnvmap = new Texture(resPath + "envmap.gif", true, false);
  private final Texture texParticle = new Texture(resPath + "particle.gif", true, false);
  private final Texture texScreen = new Texture(resPath + "screen.gif", false, true);
  private final Texture texFont = new Texture(resPath + "font.gif", true, true);
  private final Texture texTitle = new Texture(resPath + "title.gif", true, false);

  private final static double PID = Math.PI / 180.0; // pi ratio
  private final static double CR = 0.00390625; // color ratio
  private final static boolean SNG = true; // music flag
  private final static boolean MORE_KEYS = false; // cheatmode keys

  // music stuff
  private FMUSIC_MODULE mod; // music handle

  private final static float /* GLfloat */fov = 80.0f; // field of view angle
  private final static float /* GLfloat */nearplane = 1.0f; // nearplane
  private final static float /* GLfloat */farplane = 200.0f;// farplane

  private String txt;

  private final Particle[] par = new Particle[512];

  private void calcRGB( float r, float g, float b, float l )
  {
    float nr = (float) (CR * r);
    if (nr > 1) nr = 1;
    if (nr < 0) nr = 0;
    float ng = (float) (CR * g);
    if (ng > 1) ng = 1;
    if (ng < 0) nr = 0;
    float nb = (float) (CR * b);
    if (nb > 1) nb = 1;
    if (nb < 0) nr = 0;
    gl.glColor4f(nr, ng, nb, l);
  }

  private void calcRGB( Color c, float l )
  {
    calcRGB(c.getRed(), c.getGreen(), c.getBlue(), l);
  }

  private void genRezR( float d )
  {
    gl.glBegin(GL.GL_QUADS);
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

  private void genRezE( float d )
  {
    gl.glBegin(GL.GL_QUADS);
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

  private void genRezZ( float d )
  {
    gl.glBegin(GL.GL_QUADS);
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

  private void genRez()
  {
    float d = 1.0f;

    r_d1 = gl.glGenLists(5);

    gl.glNewList(r_d1, GL.GL_COMPILE);
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
    gl.glNewList(r_d2, GL.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL.GL_QUADS);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, d);
    gl.glVertex3f(4.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, d);
    gl.glVertex3f(3.0f, -2.0f, -d);
    gl.glVertex3f(1.0f, -2.0f, -d);
    gl.glEnd();

    gl.glBegin(GL.GL_QUADS);
    gl.glVertex3f(3.0f, -3.0f, d);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(3.0f, -3.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL.GL_QUADS);
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

    gl.glBegin(GL.GL_QUADS);
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
    gl.glNewList(r_d3, GL.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL.GL_QUADS);
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

    gl.glBegin(GL.GL_QUADS);
    gl.glVertex3f(2.0f, -3.0f, d);
    gl.glVertex3f(2.0f, -3.0f, d);
    gl.glVertex3f(3.0f, -5.0f, -d);
    gl.glVertex3f(2.0f, -3.0f, -d);
    gl.glEnd();

    gl.glTranslatef(5.0f, 0.0f, 0.0f);

    gl.glBegin(GL.GL_QUADS);
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

    gl.glBegin(GL.GL_QUADS);
    gl.glVertex3f(4.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(4.0f, -5.0f, -d);
    gl.glVertex3f(2.75f, -1.0f, d);
    gl.glVertex3f(0.0f, -1.0f, d);
    gl.glVertex3f(0.0f, -1.0f, -d);
    gl.glVertex3f(2.75f, -1.0f, -d);
    gl.glEnd();

    gl.glBegin(GL.GL_QUADS);
    gl.glVertex3f(4.0f, -1.0f, d);
    gl.glVertex3f(1.25f, -4.0f, d);
    gl.glVertex3f(1.25f, -4.0f, -d);
    gl.glVertex3f(4.0f, -1.0f, -d);
    gl.glEnd();

    gl.glEndList();

    r_d4 = r_d3 + 1;
    gl.glNewList(r_d4, GL.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL.GL_QUADS);
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

    gl.glBegin(GL.GL_QUADS);
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

    gl.glBegin(GL.GL_QUADS);
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
    gl.glNewList(r_d5, GL.GL_COMPILE);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);

    gl.glBegin(GL.GL_QUADS);
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

    gl.glBegin(GL.GL_QUADS);
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

    gl.glBegin(GL.GL_QUADS);
    gl.glVertex3f(0.0f, -5.0f, d);
    gl.glVertex3f(0.0f, -4.0f, d);
    gl.glVertex3f(0.0f, -4.0f, -d);
    gl.glVertex3f(0.0f, -5.0f, -d);
    gl.glVertex3f(0.0f, -1.0f, d);
    gl.glVertex3f(0.0f, 0.0f, d);
    gl.glVertex3f(0.0f, 0.0f, -d);
    gl.glVertex3f(0.0f, -1.0f, -d);
    gl.glEnd();

    gl.glBegin(GL.GL_QUADS);
    gl.glVertex3f(0.0f, -4.0f, d);
    gl.glVertex3f(2.75f, -1.0f, d);
    gl.glVertex3f(2.75f, -1.0f, -d);
    gl.glVertex3f(0.0f, -4.0f, -d);
    gl.glEnd();

    gl.glEndList();
  }

  private void dspRez()
  {
    gl.glLoadIdentity();
    gl.glTranslatef(p_x, p_y, p_z);
    gl.glRotatef(a_x, 1.0f, 0.0f, 0.0f);
    gl.glRotatef(a_y, 0.0f, 1.0f, 0.0f);
    gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);
    gl.glTranslatef(-7.0f, 2.5f, 0.0f);

    int r_r = (int) (nr + p_x * 1.5 + 16.0 * Math.cos(a / 8.0));
    int r_g = ng;
    int r_b = (int) (nb + p_y * 1.5 - 16.0 * Math.sin(a / 8.0));
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
    calcRGB(r_r, r_g, r_b, 0.65f);
    gl.glCallList(r_d1);
    calcRGB(r_r + 64, r_g + 64, r_b + 64, 0.65f);
    gl.glCallList(r_d2);
    calcRGB(r_r - 64, r_g - 64, r_b - 64, 0.65f);
    gl.glCallList(r_d3);
    calcRGB(r_r + 32, r_g + 32, r_b + 32, 0.65f);
    gl.glCallList(r_d4);
    calcRGB(r_r - 32, r_g - 32, r_b - 32, 0.65f);
    gl.glCallList(r_d5);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
    calcRGB(r_r - 80, r_g - 80, r_b - 80, 0.65f);
    gl.glTranslatef(-10.0f, 0.0f, 0.0f);
    gl.glCallList(r_d1);
    gl.glCallList(r_d2);
    gl.glCallList(r_d3);
    gl.glCallList(r_d4);
    gl.glCallList(r_d5);
  }

  private void cubeSide( float d, float x, float y, float c, float s )
  {
    gl.glBegin(GL.GL_QUADS);
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

  private void genCube( float d, int r, int g, int b, float z, float l )
  {
    float[] w = new float[] { (float) (CR * r), (float) (CR * g), (float) (CR * b), l };
    float t = (float) (3.99 - 4.0 * Math.cos((m_a * z - 4) * PID));
    float c = (float) (t * Math.cos((m_a * z) * PID));
    float s = (float) (t * Math.sin((m_a * z) * PID));
    float x = (float) (0.48);
    float y = (float) (0.48);

    gl.glLoadIdentity();
    gl.glTranslatef(p_x, p_y, p_z);

    gl.glRotatef(a_x, 1.0f, 0.0f, 0.0f);
    gl.glRotatef(a_y, 0.0f, 1.0f, 0.0f);
    gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);

    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, w, 0);

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

  private void genEnvmap( float d, int r, int g, int b, float l )
  {
    float[] c1 = new float[] { (float) (CR * r), (float) (CR * g), (float) (CR * b), l };
    float[] c2 = new float[] { (float) (CR * (r + 32)), (float) (CR * g), (float) (CR * (b - 32)), l };
    float[] c3 = new float[] { (float) (CR * (r - 32)), (float) (CR * g), (float) (CR * (b + 32)), l };
    float c = 1.0f;
    float s = 1.0f;
    float x = 0.0f;
    float y = 0.0f;

    gl.glLoadIdentity();
    gl.glTranslatef(p_x, p_y, p_z);

    gl.glRotatef(a_x, 1.0f, 0.0f, 0.0f);
    gl.glRotatef(a_y, 0.0f, 1.0f, 0.0f);
    gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);

    gl.glEnable(GL.GL_TEXTURE_GEN_S);
    gl.glEnable(GL.GL_TEXTURE_GEN_T);
    gl.glPushMatrix();

    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, c1, 0);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, c2, 0);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, c3, 0);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
    cubeSide(d, x, y, c, s);

    gl.glPopMatrix();
    gl.glDisable(GL.GL_TEXTURE_GEN_S);
    gl.glDisable(GL.GL_TEXTURE_GEN_T);
  }

  private void glenzSide( int r, int g, int b, float l )
  {
    float d = 0.5f;
    float[] w = new float[] { 1.0f, 1.0f, 1.0f, l };
    float[] c = new float[] { (float) (CR * r), (float) (CR * g), (float) (CR * b), l };

    gl.glBegin(GL.GL_TRIANGLES);
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, w, 0);
    gl.glNormal3f(0.25f, -0.25f, -0.75f);
    gl.glVertex3f(-d, d, d);
    gl.glVertex3f(d, d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, c, 0);
    gl.glNormal3f(-0.25f, -0.25f, -0.75f);
    gl.glVertex3f(d, d, d);
    gl.glVertex3f(d, -d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, w, 0);
    gl.glNormal3f(-0.25f, 0.25f, -0.75f);
    gl.glVertex3f(d, -d, d);
    gl.glVertex3f(-d, -d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, c, 0);
    gl.glNormal3f(0.25f, 0.25f, -0.75f);
    gl.glVertex3f(-d, -d, d);
    gl.glVertex3f(-d, d, d);
    gl.glVertex3f(0.f, 0.f, (float) (d * 1.75));
    gl.glEnd();
  }

  private void genGlenz( int r, int g, int b, float l )
  {
    gl.glLoadIdentity();
    gl.glTranslatef(p_x, p_y, p_z);

    gl.glRotatef(a_x, 1.0f, 0.0f, 0.0f);
    gl.glRotatef(a_y, 0.0f, 1.0f, 0.0f);
    gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);

    glenzSide(r, g, b, l);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
    glenzSide(r, g, b, l);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    glenzSide(r, g, b, l);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
    glenzSide(r, g, b, l);
    gl.glRotatef(90, 0.0f, 0.0f, 1.0f);
    gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
    glenzSide(r, g, b, l);
    gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
    glenzSide(r, g, b, l);
  }

  private void genQuad( float d, int r, int g, int b, float l )
  {
    gl.glLoadIdentity();
    gl.glTranslatef(p_x, p_y, p_z);

    gl.glRotatef(a_x, 1.0f, 0.0f, 0.0f);
    gl.glRotatef(a_y, 0.0f, 1.0f, 0.0f);
    gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);

    calcRGB(r, g, b, l);

    gl.glBegin(GL.GL_QUADS);
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

  private void genParticle()
  {
    float d = 1.5f;
    float a = 0.025f;
    float b = 0.975f;

    r_d7 = gl.glGenLists(1);
    gl.glNewList(r_d7, GL.GL_COMPILE);
    gl.glBegin(GL.GL_QUADS);
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

  private void genRing()
  {
    float min = 0.35f;
    float max = 0.55f;
    float d;

    r_d6 = gl.glGenLists(1);
    gl.glNewList(r_d6, GL.GL_COMPILE);
    for (int i = 0; i < 360; i += 5)
    {
      gl.glBegin(GL.GL_QUADS);
      d = max;
      gl.glColor3f(0.0f, 0.0f, 0.0f);
      gl.glVertex3f((float) (d * Math.cos(i * PID)), (float) (d * Math.sin(i * PID)), 0.0f);
      gl.glVertex3f((float) (d * Math.cos((i + 10.0) * PID)), (float) (d * Math.sin((i + 10.0) * PID)), 0.0f);
      d = (float) ((min + max) / 2.0 + (max - min) / 4.0);
      gl.glColor3f((float) (96 * CR), (float) (64 * CR), (float) (128 * CR));
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
      gl.glColor3f((float) (96 * CR), (float) (64 * CR), (float) (128 * CR));
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

  private void calcTxt()
  {
    l_n = 0;
    l_m = 0;
    l_i = 0;
    for (int i = 0; i < txt.length(); i++)
    {
      if (txt.charAt(i) != 13)
      {
        l_i += 1;
      }
      else
      {
        if (l_i > l_m) l_m = l_i;
        l_n += 1;
        l_i = 0;
      }
    }
  }

  private void init3D( FrameInfo fi )
  {
    Dimension size = fi.getCanvasSize();

    gl.glViewport(0, 0, size.width, size.height); // reset viewport
    gl.glMatrixMode(GL.GL_PROJECTION); // select projection matrix
    gl.glLoadIdentity(); // reset projection matrix
    glu.gluPerspective(fov, ((float) size.width / (float) size.height), nearplane, farplane); // aspect ratio
    gl.glMatrixMode(GL.GL_MODELVIEW); // select modelview matrix
    gl.glLoadIdentity(); // reset modelview matrix
  }

  private void init2D( FrameInfo fi )
  {
    Dimension size = fi.getCanvasSize();

    gl.glViewport(0, 0, size.width, size.height); // reset viewport
    gl.glMatrixMode(GL.GL_PROJECTION); // select projection matrix
    gl.glLoadIdentity(); // reset projection matrix
    glu.gluOrtho2D(0, size.width, size.height, 0); // init orthographic mode
    gl.glMatrixMode(GL.GL_MODELVIEW); // select modelview matrix
    gl.glLoadIdentity(); // reset modelview matrix
  }

  protected void initGL() throws Exception // openGL setup
  {
    gl.glShadeModel(GL.GL_SMOOTH); // enable smooth shading
    gl.glClearDepth(1.0f); // set depth buffer
    gl.glEnable(GL.GL_DEPTH_TEST); // enable depth testing
    gl.glDepthFunc(GL.GL_LEQUAL); // depth testing mode
    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // perspective calculations mode
    gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);// set mode S to sphere mapping
    gl.glTexGeni(GL.GL_T, GL.GL_TEXTURE_GEN_MODE, GL.GL_SPHERE_MAP);// set mode T to sphere mapping
    gl.glLineWidth(1.0f); // set line width

    // load texture
    GLTextureLoader.bind(gl, glu, texPouet);
    GLTextureLoader.bind(gl, glu, texCoeur);
    GLTextureLoader.bind(gl, glu, texEnvmap);
    GLTextureLoader.bind(gl, glu, texParticle);
    GLTextureLoader.bind(gl, glu, texScreen);
    GLTextureLoader.bind(gl, glu, texFont);
    GLTextureLoader.bind(gl, glu, texTitle);

    // init display list
    genRez();
    genRing();
    genParticle();

    // init particles
    for (int i = 0; i < par.length; i++)
    {
      float a = (float) (cRand() % 3600 / 10.0);
      float d = (float) ((1700 + cRand() % 1000) / 100.0);
      float y = (float) ((-1700 + cRand() % 3400) / 100.0);
      int z = cRand() % 360;

      int r = 128 + cRand() % 96;
      int g = 128 + cRand() % 96;
      int b = 128 + cRand() % 96;
      Color c = new Color(r, g, b);

      par[i] = new Particle(d, y, a, z, c);
    }
    txt = "REZ IS PROUD\r TO PRESENT\rA LITTLE 20K\rOPENGL INTRO\r";
    calcTxt();
  }

  protected boolean initSound()
  {
    try
    {
      Init.loadLibraries();
    }
    catch (InitException e)
    {
      e.printStackTrace();
      return false;
    }

    Fmod.FSOUND_Init(44100, 32, 0);

    mod = Fmod.FMUSIC_LoadSong("kenzalol.xm");

    if (mod != null)
    {
      Fmod.FMUSIC_PlaySong(mod);
      // Fmod.FMUSIC_SetMasterVolume(mod, 0);
      return true; // TODO
    }
    else
    {
      System.err.println(Fmod.FMOD_ErrorString(Fmod.FSOUND_GetError()));
      return false;
    }
  }

  protected void killSound()
  {
    Fmod.FMUSIC_StopSong(mod);
    Fmod.FSOUND_Close();
  }

  protected void pauseSound()
  {
    Fmod.FMUSIC_SetPaused(mod, !isPaused());
  }

  protected void drawGLScene() // draw scene
  {
    FrameInfo fi = getFrameInfo();
    Dimension size = fi.getCanvasSize();

    float t_g = getTime();

    // synchro
    if (SNG)
    {
      int ord = Fmod.FMUSIC_GetOrder(mod);
      // System.out.println("order="+ord);
      int row = Fmod.FMUSIC_GetRow(mod);
      // System.out.println("row="+row);

      if (row == 0)
      {
        switch (ord)
        {
          case 0:
            if (!m_f) m_t = t_g;
            m_f = true;
            break;
          case 1:
            l_f = true;
            break;
          case 2:
            g_f = true;
            if (!j_f)
              r_f = false;
            else
              r_f = true;
            p_f = false;
            l_f = false;
            t_f = true;
            if (!z_f) z_t = t_g;
            z_f = true;
            break;
          case 4:
            r_f = true;
            p_f = true;
            l_f = true;
            if (!j_f) j_t = t_g;
            j_f = true;
            txt = "\r\r\r\r\r\r\r\r\r\r\r\r\r\r        CREDITS:\r        --------\r\r       CODE BY REZ\r      DESIGN BY REZ\r     CHIPTUNE BY REZ\r\r THANKS TO KEOPS/EQUINOX \rFOR HIS HELP AND SUPPORT!\r\rTHANKS TO RYG/FARBRAUSH\rFOR HIS GREAT KKRUNCHY!\r\r    I HOPE YOU LIKED\rTHIS LITTLE 20KB INTRO!\r";
            calcTxt();
            break;
          case 6:
            r_f = false;
            g_f = false;
            txt = "\r\r\r\r\r\r\r\r\r\r\r\r\r\r    HEI HEI HEI\r\rNOTHING MORE TO SEE\rTHANKS FOR WATCHING\r\r    INTRO LOOP!";
            calcTxt();
            break;
        }
      }
      if (row % 8 == 0)
      {
        f_n = 0.25f;
        f_v2 = f_n;
        f_t2 = t_g;
      }
      if (row == 0)
      {
        f_n = 0.5f;
        f_v1 = f_n;
        f_t1 = t_g;
        f_v2 = 0;
      }
      if ((((row % 8 - 4) == 0) || (row == 60) || (row == 62)) && (ord > 1))
      {
        c_r = 0.0f;
        c_j = 0.5f;
        c_t = t_g;
      }
    }

    // compute color
    final int /* GLint */b_r = 128; // red value
    final int /* GLint */b_g = 128; // green value
    final int /* GLint */b_b = 128; // blue value

    nr = (int) (b_r + 16.0 * Math.cos(32.0 * t_g * PID));
    ng = (int) (b_g + 16.0 * Math.sin(16.0 * t_g * PID));
    nb = (int) (b_b + 16.0 * Math.cos(48.0 * t_g * PID));
    float[] f_c = new float[] { f_v1 + f_v2, f_v1 + f_v2, f_v1 + f_v2, 1.0f }; // fog color
    if (f_v1 > 0) f_v1 = (float) (f_n - f_n * 2.0 * (t_g - f_t1));
    if (f_v2 > 0) f_v2 = (float) (f_n - f_n * 2.0 * (t_g - f_t2));
    // 3d mode
    init3D(fi);
    // clear screen and depth buffer
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    // background color
    gl.glClearColor(f_c[0], f_c[1], f_c[2], 1.0f);
    // light
    float[] l_a = new float[] { (float) (-0.5 + CR * nr), (float) (-0.5 + CR * ng), (float) (-0.5 + CR * nb), 1.0f };
    float[] l_d = new float[] { 0.75f, 0.75f, 0.75f, 1.0f };
    float[] l_p = new float[] { 0.0f, -50.0f, (float) (c_z - 75.0), 1.0f };
    gl.glLoadIdentity();
    gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, l_a, 0); // ambient light color
    gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, l_d, 0); // diffuse light color
    gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, l_p, 0); // light position
    gl.glEnable(GL.GL_LIGHT1);
    // fog
    gl.glFogi(GL.GL_FOG_MODE, GL.GL_LINEAR); // fog mode
    gl.glFogfv(GL.GL_FOG_COLOR, f_c, 0); // fog color
    gl.glFogf(GL.GL_FOG_DENSITY, 0.5f); // fog density
    gl.glHint(GL.GL_FOG_HINT, GL.GL_DONT_CARE); // fog hint value
    gl.glFogf(GL.GL_FOG_START, 20.0f); // fog start depth
    gl.glFogf(GL.GL_FOG_END, 65.0f); // fog end depth
    gl.glEnable(GL.GL_FOG); // enable fog
    // disable z-buffer, enable blend
    gl.glDisable(GL.GL_DEPTH_TEST); // disable z-buffer
    gl.glDepthMask(false); // do not write z-buffer
    gl.glEnable(GL.GL_BLEND); // enable blending

    // move scene
    if (z_f)
    {
      z_r = (float) (24.0 * (t_g - z_t));
      if (z_z > 5.0f) z_z = (float) (10.0 - 1.0 * (t_g - z_t));
      z_a = 24.0f;
    }
    c_z = (float) (-15.0 + z_z * Math.cos(z_r * 2.0 * PID));
    // rez
    if (r_f)
    {
      gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_SRC_ALPHA);
      if (l_f)
      {
        final int /* GLint */r_l = 12; // loop number
        final int /* GLint */r_n = 8; // ammount by loop

        for (int i = 0; i < r_n * r_l; i++)
        {
          // compute values
          a = (float) (i * 270.0 / r_n + r_y);
          p_x = (float) (30.0 * Math.cos(a * PID));
          p_y = (float) (-(r_l / 2) * 6 + (6.0 / r_n) * i);
          p_z = (float) (c_z + 30.0 * Math.sin(a * PID));
          a_x = (float) (0.0);
          a_y = (float) (-90.0 - a);
          a_z = (float) (2.5);
          // display list
          dspRez();
        }
      }
      else
      {
        for (int i = 0; i < 360; i += 12)
        {
          for (int j = 0; j < 3; j++)
          {
            // compute values
            p_x = (float) (30.0 * Math.cos((i + r_y / 2.0) * PID));
            p_y = (float) (30.0 * Math.sin((i + r_y / 2.0) * PID));
            p_z = (float) (c_z - 36.0 + 18.0 * j);
            a_x = (float) (90.0);
            a_y = (float) (-90.0 + i + r_y / 2.0);
            a_z = (float) (90.0);
            // display list
            dspRez();
          }
        }
      }
      gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
    }
    // 2d mode
    init2D(fi);
    // draw 2d
    gl.glEnable(GL.GL_TEXTURE_2D); // enable texture mode
    if (!r_f)
    {
      gl.glBindTexture(GL.GL_TEXTURE_2D, texEnvmap.getId());
      gl.glBlendFunc(GL.GL_ONE, GL.GL_DST_COLOR);
      calcRGB(nr, ng, nb, 0.5f);
    }
    else
    {
      gl.glBindTexture(GL.GL_TEXTURE_2D, Texture.ID_NONE);
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_COLOR);
      calcRGB(nr, ng, nb, 0.375f);
    }
    // background
    gl.glBegin(GL.GL_QUADS);
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
    // 3d mode
    init3D(fi);
    // mapping
    if (m_f) m_a = (float) (32.0 * (t_g - m_t));
    // move cube and title
    if (c_j > 0)
    {
      c_r = (float) (32.0 * (t_g - c_t));
      c_j = (float) (0.2 - 0.8 * (t_g - c_t));
    }
    if ((j_f) && (j_y > 0))
    {
      j_r = (float) (5.0 * (t_g - j_t));
      j_y = (float) (-c_y - 7.0 * (t_g - j_t));
    }
    // enable lighting
    gl.glEnable(GL.GL_LIGHTING);
    // cube
    p_x = 0.0f;
    p_y = (float) (c_y + j_y * Math.cos(j_r) + c_j * Math.cos(c_r));
    p_z = c_z;
    a_x = r_y;
    a_y = (float) (r_y * 2.0);
    a_z = (float) (r_y / 2.0);
    gl.glEnable(GL.GL_CULL_FACE); // enable cull face
    gl.glCullFace(GL.GL_FRONT); // cull mode front
    gl.glBindTexture(GL.GL_TEXTURE_2D, texPouet.getId());
    gl.glBlendFunc(GL.GL_ZERO, GL.GL_ONE);
    genCube(3.0f, 160, 160, 160, 1.0f, 1.0f);
    gl.glDisable(GL.GL_DEPTH_TEST); // disable z-buffer
    gl.glDepthMask(false); // do not write z-buffer
    gl.glEnable(GL.GL_BLEND); // enable blending
    gl.glBindTexture(GL.GL_TEXTURE_2D, texCoeur.getId());
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
    genCube(3.0f, 160, 160, 160, 0.75f, 1.0f);
    gl.glBindTexture(GL.GL_TEXTURE_2D, texEnvmap.getId());
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
    genEnvmap(3.0f, (int) (96 + z_a * 6), (int) (96 + z_a * 6), (int) (96 + z_a * 6), 0.75f);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_COLOR);
    genEnvmap(3.0f, 64, 64, 64, 0.75f);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
    gl.glDisable(GL.GL_CULL_FACE); // disable cull face
    gl.glDisable(GL.GL_TEXTURE_2D); // disable texture mode
    gl.glDepthMask(true); // write z-buffer
    gl.glEnable(GL.GL_DEPTH_TEST); // enable z-buffer
    // gl.glenz
    if (g_f)
    {
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
      final int /* GLint */g_n = 15; // ammount
      for (int i = 0; i < g_n; i++)
      {
        p_x = (float) ((8.0 + 3.0 * Math.cos(r_y * 4.0 * PID)) * Math.cos((360 / g_n * i + r_y * 2.0) * PID));
        p_y = (float) (c_y + j_y * Math.cos(j_r) + 3.0 * Math.cos((360 / g_n * i + r_y * 4.0) * PID)); // -c_j*Math.cos(c_r)
        p_z = (float) (c_z + (8.0 + 3.0 * Math.cos(r_y * 4.0 * PID)) * Math.sin((360 / g_n * i + r_y * 2.0) * PID));
        a_x = (float) (r_y * 3.0 + i * 24.0);
        a_y = (float) (r_y * 4.0 + i * 24.0);
        a_z = (float) (r_y * 2.0 + i * 24.0);
        genGlenz((int) (nr / 2.0 + p_x * 6), (int) (nr / 2.0), (int) (nr / 2.0 - p_x * 6), (float) ((-p_z - 2.0) / 6.0));
      }
    }
    // disable lighting
    gl.glDisable(GL.GL_LIGHTING);
    // disable fog
    gl.glDisable(GL.GL_FOG);
    // enable texture mode
    gl.glEnable(GL.GL_TEXTURE_2D); // enable texture mode
    // particles
    if (p_f)
    {
      gl.glDepthMask(false); // do not write z-buffer
      gl.glBindTexture(GL.GL_TEXTURE_2D, texParticle.getId());
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
      a_x = 0.0f;
      a_y = 0.0f;
      for (int i = 0; i < par.length; i++)
      {
        Particle p = par[i];
        if (r_f)
          p_y = (float) (p.getY() + 0.25 * Math.cos((p.getA() + r_y) * 16.0 * PID));
        else
          p_y = (float) (c_y + j_y * Math.cos(j_r) + ((27.0 - p.getD()) / 16.0) * Math.cos((r_y * 6.0 + p.getD() * 16.0) * PID));
        p_x = (float) (p.getD() * Math.cos((p.getA() + r_y) * PID));
        p_z = (float) (c_z + p.getD() * Math.sin((p.getA() + r_y) * PID));
        a_z = p.getZ();
        gl.glLoadIdentity();
        gl.glTranslatef(p_x, p_y, p_z);
        gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);
        calcRGB(p.getColor(), (float) ((-p_z - 2.0) / 6.0));
        gl.glCallList(r_d7);
      }
      gl.glDepthMask(true); // write z-buffer
    }
    // disable z-buffer
    gl.glDisable(GL.GL_DEPTH_TEST); // disable z-buffer
    gl.glDepthMask(false); // do not write z-buffer
    // title
    if (t_f)
    {
      gl.glBindTexture(GL.GL_TEXTURE_2D, texTitle.getId());
      gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
      a_x = (float) (30.0 * Math.cos((r_y * 2) * PID));
      a_y = (float) (30.0 * Math.cos((r_y * 2) * PID));
      a_z = 0.0f;
      p_x = 0.0f;
      p_y = (float) (-(c_y + j_y * Math.cos(j_r)) * 0.2);
      p_z = -2.0f;
      genQuad(0.7f, 224, 224, 224, 0.5f);
      gl.glBindTexture(GL.GL_TEXTURE_2D, Texture.ID_NONE);
      a_x += 30.0f;
      a_y += 30.0f;
      gl.glLoadIdentity();
      gl.glTranslatef(p_x, p_y, p_z);
      gl.glRotatef(a_x, 1.0f, 0.0f, 0.0f);
      gl.glRotatef(a_y, 0.0f, 1.0f, 0.0f);
      gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);
      gl.glCallList(r_d6);
    }
    // liner
    if (l_f)
    {
      gl.glBindTexture(GL.GL_TEXTURE_2D, texFont.getId());
      gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);

      final float /* GLfloat */l_s = 0.025f; // size
      p_x = -(l_m * l_s * 1.75f) / 2.f;
      p_y = (l_n * l_s * 2.0f) / 2.f;
      p_z = -1.125f;
      int l_l = 0;
      l_i = 0;
      for (int i = 0; i < txt.length(); i++)
      {
        char l_c = txt.charAt(i);
        if (l_c != 13)
        {
          // compute position
          p_x += (float) (l_s * 1.625);
          if (l_c != 32)
          {
            gl.glLoadIdentity();
            a_z = (float) (20.0 * Math.cos((24.0 * t_g * 6.0 + l_i + 90.0) * PID));
            gl.glTranslatef(p_x, (float) (p_y + 0.1 * Math.cos((24.0 * t_g * 6.0 + l_i) * PID)), p_z);
            gl.glRotatef(a_z, 0.0f, 0.0f, 1.0f);
            // render char
            float l_w = (l_c % 16) * 0.0625f; // taille+espacement
            float l_h = (l_c / 16 - 2) * 0.25f;
            final float ox1 = 0.003f;
            final float ox2 = 0.06f;
            final float oy1 = 0.012f;
            final float oy2 = 0.24f;

            calcRGB((int) (144 + 112 * Math.cos((24.0 * t_g * 8.0 + l_i * 1.5) * PID)), 144, (int) (144 + 112 * Math.sin((24.0 * t_g * 8.0 + l_i * 1.5) * PID)), 1.0f);
            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2fv(texFont.img2Tex(l_w + ox1, l_h + oy1), 0);
            gl.glVertex3f(-l_s, l_s, l_s / 2.f);
            gl.glTexCoord2fv(texFont.img2Tex(l_w + ox2, l_h + oy1), 0);
            gl.glVertex3f(l_s, l_s, l_s / 2.f);
            gl.glTexCoord2fv(texFont.img2Tex(l_w + ox2, l_h + oy2), 0);
            gl.glVertex3f(l_s, -l_s, l_s / 2.f);
            gl.glTexCoord2fv(texFont.img2Tex(l_w + ox1, l_h + oy2), 0);
            gl.glVertex3f(-l_s, -l_s, l_s / 2.f);
            gl.glEnd();
          }
          l_i += 10;
        }
        else
        {
          l_l += 1;
          l_i = 0;
          p_x = (float) (-(l_m * l_s * 1.75) / 2.0);
          p_y = (float) ((l_n * l_s * 2.0) / 2.0 - l_l * l_s * 2.0);
        }
      }
    }
    // 2d mode
    init2D(fi);
    // draw 2d
    gl.glBindTexture(GL.GL_TEXTURE_2D, texScreen.getId());
    gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_SRC_ALPHA);
    // alpha mask
    calcRGB(255, 255, 255, 0.75f);
    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex2i(0, 40);
    gl.glTexCoord2f((size.width / 2), 0.0f);
    gl.glVertex2i(size.width, 40);
    gl.glTexCoord2f((size.width / 2), (size.height / 2 - 40));
    gl.glVertex2i(size.width, (size.height - 40));
    gl.glTexCoord2f(0.0f, (size.height / 2 - 40));
    gl.glVertex2i(0, (size.height - 40));
    gl.glEnd();
    // borders
    for (int i = 0; i < 20; i++)
    {
      calcRGB(255, 255, 255, (float) (0.75 - 0.75 / 20 * i));
      gl.glBegin(GL.GL_QUADS);
      gl.glTexCoord2f(0.0f, 0.0f);
      gl.glVertex2i(0, (38 - i * 2));
      gl.glTexCoord2f((size.width / 2), 0.0f);
      gl.glVertex2i(size.width, (38 - i * 2));
      gl.glTexCoord2f((size.width / 2), 1.0f);
      gl.glVertex2i(size.width, (40 - i * 2));
      gl.glTexCoord2f(0.0f, 1.0f);
      gl.glVertex2i(0, (40 - i * 2));
      gl.glEnd();
      gl.glBegin(GL.GL_QUADS);
      gl.glTexCoord2f(0.0f, 0.0f);
      gl.glVertex2i(0, (size.height - 40 + i * 2));
      gl.glTexCoord2f((size.width / 2), 0.0f);
      gl.glVertex2i(size.width, (size.height - 40 + i * 2));
      gl.glTexCoord2f((size.width / 2), 1.0f);
      gl.glVertex2i(size.width, (size.height - 38 + i * 2));
      gl.glTexCoord2f(0.0f, 1.0f);
      gl.glVertex2i(0, (size.height - 38 + i * 2));
      gl.glEnd();
    }
    // disable blending & texture mode
    gl.glDisable(GL.GL_TEXTURE_2D); // disable texture mode
    gl.glDisable(GL.GL_BLEND); // disable blending
    // enable z-buffer
    gl.glDepthMask(true); // write z-buffer
    gl.glEnable(GL.GL_DEPTH_TEST); // enable z-buffer
    // compute rotation
    r_y = (z_a * (t_g - z_t));
  }

  protected void reshapeGL() throws Exception
  {
    init3D(getFrameInfo());
  }

  public boolean dispatchKeyEvent( KeyEvent e )
  {
    if (!super.dispatchKeyEvent(e))
    {
      int code = e.getKeyCode();
      float t_g = getTime();

      if (MORE_KEYS)
      {
        if (code == KeyEvent.VK_F2) // F2 pressed ?
        {
          r_f = !r_f; // toggle on/off REZ
        }
        else if (code == KeyEvent.VK_F3) // F3 pressed ?
        {
          p_f = !p_f; // toggle on/off particles
        }
        else if (code == KeyEvent.VK_F4) // F4 pressed ?
        {
          g_f = !g_f; // toggle on/off gl.glenz
        }
        else if (code == KeyEvent.VK_F5) // F5 pressed ?
        {
          l_f = !l_f; // toggle on/off liner
        }
        else if (code == KeyEvent.VK_F6) // F6 pressed ?
        {
          t_f = !t_f; // toggle on/off title
        }
        else if (code == KeyEvent.VK_F7) // F7 pressed ?
        {
          m_f = !m_f; // toggle on/off mapping
          m_t = t_g;
        }
        else if (code == KeyEvent.VK_SPACE) // space pressed ?
        {
          c_r = 0.0f;
          c_j = 0.5f;
          c_t = t_g;
          f_n = 0.25f;
          f_v2 = f_n;
          f_t2 = t_g;
        }
        else if (code == KeyEvent.VK_ENTER) // return pressed ?
        {
          j_f = true; // move cube and title
          f_n = 0.5f;
          f_v1 = f_n;
          f_t1 = t_g;
          c_y = -3.85f;
          j_y = 3.85f;
          j_t = t_g;
        }
        else if (code == KeyEvent.VK_TAB) // tab pressed ?
        {
          z_f = true; // move scene
          z_z = 10.0f;
          z_a = 0.0f;
          z_t = t_g;
        }
        else
        {
          return false;
        }
      }
    }
    
    return true;
  }

  /**
   * Emulated stuff
   */
  private float getTime()
  {
    return getTimer().getElapsed() / 819.2f; // trying to emulate ticks shit
  }

  protected final static int cRand()
  {
    return (int) (32768 * (Math.random() - 0.5));
  }

  public static void main( String... args )
  {
    try
    {
      FrameInfo fi = new FrameInfo("GLREZ", 832, 625, false);
      new GLRez().start(fi, false);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  // Rez asshole is here
}