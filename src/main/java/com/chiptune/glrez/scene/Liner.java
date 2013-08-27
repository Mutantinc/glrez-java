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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

import org.mars.demo.graph.GLDemo;

import com.chiptune.glrez.ColorUtils;
import com.chiptune.glrez.data.Resources;

public class Liner extends Geometry {
  
  private final static String TXT_PRESENT = "REZ IS PROUD\r TO PRESENT\rA LITTLE 20K\rOPENGL INTRO\r";
  private final static String TXT_CREDITS = "\r\r\r\r\r\r\r\r\r\r\r\r\r\r        CREDITS:\r        --------\r\r       CODE BY REZ\r      DESIGN BY REZ\r     CHIPTUNE BY REZ\r\r THANKS TO KEOPS/EQUINOX \rFOR HIS HELP AND SUPPORT!\r\rTHANKS TO RYG/FARBRAUSH\rFOR HIS GREAT KKRUNCHY!\r\r    I HOPE YOU LIKED\rTHIS LITTLE 20KB INTRO!\r";
  private final static String TXT_LOOP = "\r\r\r\r\r\r\r\r\r\r\r\r\r\r    HEI HEI HEI\r\rNOTHING MORE TO SEE\rTHANKS FOR WATCHING\r\r    INTRO LOOP!";
  
  
  private String txt;
  private int l_n; // line number
  private int l_m; // line max length
  private int l_i; // char increment
  
  
  public Liner(GLDemo demo) {
    super(demo);
  }

  public String getTxt() {
    return txt;
  }

  public void setTxt(String txt) {
    this.txt = txt;
    calcTxt();
  }

  public void setTxtPresent() {
    setTxt(TXT_PRESENT);
  }

  public void setTxtCredits() {
    setTxt(TXT_CREDITS);
  }

  public void setTxtLoop() {
    setTxt(TXT_LOOP);
  }

  private void calcTxt() {
    l_n = 0;
    l_m = 0;
    l_i = 0;
    for (int i = 0; i < txt.length(); i++) {
      if (txt.charAt(i) != 13) {
        l_i += 1;
      }
      else {
        if (l_i > l_m)
          l_m = l_i;
        l_n += 1;
        l_i = 0;
      }
    }
  }
  
  
  public void drawLiner(Situation sit, float t_g) {
    GL2 gl = demo.getGL();

    gl.glBindTexture(GL.GL_TEXTURE_2D, Resources.texFont.getId());
    gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);

    final float l_s = 0.025f; // size
    sit.setPosX(-(l_m * l_s * 1.75f) / 2.f);
    sit.setPosY((l_n * l_s * 2.0f) / 2.f);
    sit.setPosZ(-1.125f);
    int l_l = 0;
    l_i = 0;
    for (int i = 0; i < txt.length(); i++) {
      char l_c = txt.charAt(i);
      if (l_c != 13) {
        // compute position
        sit.setPosX(sit.getPosX() + (float) (l_s * 1.625));
        if (l_c != 32) {
          gl.glLoadIdentity();
          sit.setAngleZ((float) (20.0 * Math.cos((24.0 * t_g * 6.0 + l_i + 90.0) * PID)));
          gl.glTranslatef(sit.getPosX(), (float) (sit.getPosY() + 0.1 * Math.cos((24.0 * t_g * 6.0 + l_i) * PID)), sit.getPosZ());
          gl.glRotatef(sit.getAngleZ(), 0.0f, 0.0f, 1.0f);
          // render char
          float l_w = (l_c % 16) * 0.0625f; // size + spacing
          float l_h = (l_c / 16 - 2) * 0.25f;
          final float ox1 = 0.003f;
          final float ox2 = 0.06f;
          final float oy1 = 0.012f;
          final float oy2 = 0.24f;

          ColorUtils.calcRGBA(gl, (int) (144 + 112 * Math.cos((24.0 * t_g * 8.0 + l_i * 1.5) * PID)), 144, (int) (144 + 112 * Math.sin((24.0 * t_g * 8.0 + l_i * 1.5) * PID)), 1.0f);
          
          gl.glBegin(GL2GL3.GL_QUADS);
          gl.glTexCoord2fv(Resources.texFont.img2Tex(l_w + ox1, l_h + oy1), 0);
          gl.glVertex3f(-l_s, l_s, l_s / 2.f);
          gl.glTexCoord2fv(Resources.texFont.img2Tex(l_w + ox2, l_h + oy1), 0);
          gl.glVertex3f(l_s, l_s, l_s / 2.f);
          gl.glTexCoord2fv(Resources.texFont.img2Tex(l_w + ox2, l_h + oy2), 0);
          gl.glVertex3f(l_s, -l_s, l_s / 2.f);
          gl.glTexCoord2fv(Resources.texFont.img2Tex(l_w + ox1, l_h + oy2), 0);
          gl.glVertex3f(-l_s, -l_s, l_s / 2.f);
          gl.glEnd();
        }
        l_i += 10;
      }
      else {
        l_l += 1;
        l_i = 0;
        sit.setPosX((float) (-(l_m * l_s * 1.75) / 2.0));
        sit.setPosY((float) ((l_n * l_s * 2.0) / 2.0 - l_l * l_s * 2.0));
      }
    }
  }
}

