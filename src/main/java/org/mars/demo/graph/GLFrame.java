/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the Demo project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.demo.graph;

import java.awt.Component;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import org.mars.toolkit.realtime.graph.CanvasFrame;
import org.mars.toolkit.realtime.graph.FrameInfo;


public final class GLFrame extends CanvasFrame {
  private static final long serialVersionUID = -4295429455267636983L;

  private GLAutoDrawable drawable;

  public GLFrame(FrameInfo frameInfo) {
    this(frameInfo, (GLEventListener) null);
  }

  public GLFrame(FrameInfo frameInfo, GLEventListener listener) {
    this(frameInfo, GLToolkit.makeGLCanvas(listener));
  }

  public GLFrame(FrameInfo frameInfo, GLAutoDrawable drawable) {
    super(frameInfo);
    this.drawable = drawable;
    getContentPane().add((Component) drawable);
  }

  @Override
  public final void setVisible(boolean visible) {
    if (drawable != null) {
      ((Component) drawable).setVisible(visible);
    }
    super.setVisible(visible);
  }

  public final GLAutoDrawable getDrawable() {
    return drawable;
  }

}
