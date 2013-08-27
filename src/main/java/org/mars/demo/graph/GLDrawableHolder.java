package org.mars.demo.graph;

import java.awt.Component;
import java.awt.Dimension;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import org.mars.toolkit.realtime.graph.DrawableHolder;
import org.mars.toolkit.realtime.graph.FrameInfo;

public class GLDrawableHolder implements DrawableHolder {

  private GLAutoDrawable drawable;
  private FrameInfo frameInfo;
  
  public GLDrawableHolder(FrameInfo frameInfo) {
    this(frameInfo, (GLEventListener) null);
  }

  public GLDrawableHolder(FrameInfo frameInfo, GLEventListener glEventListener) {
    this(frameInfo, GLToolkit.makeGLCanvas(glEventListener));
  }

  public GLDrawableHolder(FrameInfo frameInfo, GLAutoDrawable drawable) {
    this.frameInfo = frameInfo;
    this.drawable = drawable;
  }

  
  @Override
  public FrameInfo getFrameInfo() {
    return frameInfo;
  }

  @Override
  public final Dimension getSize() {
    return frameInfo.getDrawableSize(); //should be automatically adjusted by some event listener's reshape() method.
  }

  @Override
  public Component getDrawable() {
    return (Component)drawable;
  }

  @Override
  public void display() {
    drawable.display();
  }

  @Override
  public void close() {
    //nothing
  }
}
