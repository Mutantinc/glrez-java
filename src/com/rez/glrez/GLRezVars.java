package com.rez.glrez;

import org.mars.toolkit.demo.jogl.GLDemo;

public abstract class GLRezVars extends GLDemo
{
  /* object variable */
  protected float /* GLfloat */p_x; // position x

  protected float /* GLfloat */p_y; // position y

  protected float /* GLfloat */p_z; // position z

  protected float /* GLfloat */a_x; // angle x

  protected float /* GLfloat */a_y; // angle y

  protected float /* GLfloat */a_z; // angle z

  protected float /* GLfloat */r_y; // rotation y

  /* color variable */
  protected int /* GLint */nr, ng, nb; // color values

  /* fog variable */
  protected float /* GLfloat */f_v1; // level 1

  protected float /* GLfloat */f_v2; // level 2

  protected float /* GLfloat */f_n; // level (new)

  protected float /* GLfloat */f_t1; // synchro time 1

  protected float /* GLfloat */f_t2; // synchro time 2

  /* REZ variable */
  protected boolean r_f = false; // flag

  protected int /* GLuint */r_d1; // display list 1

  protected int /* GLuint */r_d2; // display list 2

  protected int /* GLuint */r_d3; // display list 3

  protected int /* GLuint */r_d4; // display list 4

  protected int /* GLuint */r_d5; // display list 5

  protected int /* GLuint */r_d6; // display list 6 (ring)

  protected int /* GLuint */r_d7; // display list 7 (particle)

  /* cube variable */
  protected float /* GLfloat */c_y = -3.85f; // cube position y

  protected float /* GLfloat */c_z = -15.0f; // cube position z

  protected float /* GLfloat */c_j; // jump

  protected float /* GLfloat */c_r; // jump angle

  protected float /* GLfloat */c_t; // synchro time

  /* zoom variable */
  protected boolean z_f = false; // flag

  protected float /* GLfloat */z_z = 10.0f;// zoom

  protected float /* GLfloat */z_r = 0.0f; // zoom angle

  protected float /* GLfloat */z_a = 0.0f; // main angle

  protected float /* GLfloat */z_t; // synchro time

  /* jump variable */
  protected boolean j_f = false; // flag

  protected float /* GLfloat */j_y = 3.85f;// jump

  protected float /* GLfloat */j_r = 0.0f; // jump angle

  protected float /* GLfloat */j_t; // synchro time

  /* glenz variable */
  protected boolean g_f = false; // flag

  /* mapping */
  protected boolean m_f = false; // flag

  protected float /* GLfloat */m_r; // repeat ratio

  protected float /* GLfloat */m_a; // rotation angle

  protected float /* GLfloat */m_t; // synchro time

  protected int /* GLuint */m_c; // cube texture

  /* particle variable */
  protected boolean p_f = false; // flag

  /* title */
  protected boolean t_f = false; // flag

  /* liner variable */
  protected boolean l_f = false; // flag

  protected int l_n; // line number

  protected int l_m; // line max length

  protected int l_i; // char increment

  protected float /* GLfloat */a;


}
