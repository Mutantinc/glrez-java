/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the Demo project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.demo.graph;

import java.awt.GraphicsDevice;

import javax.media.opengl.DefaultGLCapabilitiesChooser;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

public class GLToolkit {

  public final static GLCanvas makeGLCanvas(GLEventListener listener) {
    GLProfile.initSingleton(); //specific (linux) inits
    GLProfile profile = GLProfile.getDefault(); //best GL version for the current platform

    GLCapabilities capabilities = new GLCapabilities(profile);
    capabilities.setHardwareAccelerated(true); // We want hardware acceleration
    capabilities.setDoubleBuffered(true); // And double buffering
    GLCapabilitiesChooser capabilitiesChooser = new DefaultGLCapabilitiesChooser();

    // GLDrawableFactory factory = getDrawableFactory();
    GLContext glContext = null; // no sharing
    GraphicsDevice graphDevice = null;
    GLCanvas canvas = new GLCanvas(capabilities, capabilitiesChooser, glContext, graphDevice);

    canvas.addGLEventListener(listener);

    return canvas;
  }
}
