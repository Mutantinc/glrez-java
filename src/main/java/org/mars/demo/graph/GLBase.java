/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the Demo project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.demo.graph;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

public abstract class GLBase {
  protected GL2 gl;
  protected GLUgl2 glu;

  private boolean interrupted;

  protected abstract void drawableIntialized() throws Exception;
  protected abstract void drawableDisposed() throws Exception;

  public abstract boolean isInitialized();

  public final boolean isInterrupted() {
    return interrupted;
  }

  public final void setInterrupted() {
    interrupted = true;
  }

  public final GL2 getGL() {
    return gl;
  }

  public final GLUgl2 getGLU() {
    return glu;
  }
}