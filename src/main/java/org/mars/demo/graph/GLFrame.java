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
import java.awt.event.WindowListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import org.mars.toolkit.realtime.graph.DrawableFrame;
import org.mars.toolkit.realtime.graph.FrameInfo;


public final class GLFrame extends DrawableFrame {
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

  
  @Override
  public Component getDrawable() {
    return (Component)drawable;
  }

  @Override
  public void display() {
    drawable.display();
  }

  public final static GLFrame openWindow(FrameInfo fi, GLEventListener glEventListener, WindowListener windowListener) throws Exception {
    GLCanvas canvas = GLToolkit.makeGLCanvas(glEventListener);
    GLFrame frame = new GLFrame(fi, canvas);
    frame.applyFrameInfo();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.addWindowListener(windowListener);
    return frame;
  }

  @Override
  public final void close() {
    setVisible(false);
    dispose();
  }
}
