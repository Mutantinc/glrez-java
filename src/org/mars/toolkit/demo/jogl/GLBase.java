package org.mars.toolkit.demo.jogl;

import java.awt.GraphicsDevice;

import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


public abstract class GLBase
{
  protected GL gl;
  protected GLU glu;

  private boolean initialized;
  private boolean interrupted;


  /**
   * GLEventListener#init(GLDrawable)
   * Called on each GLDrawable creation
   * @param drawable
   */
  public void init( GLAutoDrawable drawable )
  {
    //System.out.println("init");
    
    try
    {
      //no if(!isInitialized()) test here because init can be recalled on full screen switch for example.
      gl = drawable.getGL();
      glu = new GLU();

      initGL();
      initialized = true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      interrupted = true;
    }
  }

  protected abstract void initGL() throws Exception;

  
  public final boolean isInitialized()
  {
    return initialized;
  }
  
  public final boolean isInterrupted()
  {
    return interrupted;
  }

  public final void setInterrupted()
  {
    interrupted = true;
  }
  
  public final GL getGL()
  {
    return gl;
  }
  
  public final GLU getGLU()
  {
    return glu;
  }
  
  
  public final static GLCanvas makeGLCanvas(GLEventListener listener)
  {
    GLCapabilities capabilities = new GLCapabilities();
    capabilities.setHardwareAccelerated(true); // We want hardware acceleration
    capabilities.setDoubleBuffered(true); // And double buffering
    GLCapabilitiesChooser capabilitiesChooser = new DefaultGLCapabilitiesChooser();

    //GLDrawableFactory factory = getDrawableFactory();
    GLContext glContext = null; //no sharing 
    GraphicsDevice graphDevice = null;
    GLCanvas canvas = new GLCanvas(capabilities, capabilitiesChooser, glContext, graphDevice);
    
    canvas.addGLEventListener(listener);
    
    
    return canvas;
  }
  
}