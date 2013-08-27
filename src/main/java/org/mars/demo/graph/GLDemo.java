/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the Demo project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.demo.graph;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.gl2.GLUgl2;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.mars.toolkit.realtime.graph.DrawableHolder;
import org.mars.toolkit.realtime.graph.FrameInfo;
import org.mars.toolkit.realtime.graph.FrameRate;
import org.mars.toolkit.realtime.graph.GraphUtils;
import org.mars.toolkit.time.Timer;

public abstract class GLDemo extends GLBase implements GLEventListener, WindowListener, KeyEventDispatcher {

  private DrawableHolder drawableHolder;
  private boolean active;
  private Timer timer;
  private int frameCount;

  private FrameRate frameRate;

  protected GLDemo() {
    KeyboardFocusManager keybFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    keybFocusManager.addKeyEventDispatcher(this);
    
    frameRate = new FrameRate();
    timer = new Timer();
  }

  public final DrawableHolder getDrawableHolder() {
    return drawableHolder;
  }

  public final FrameInfo getFrameInfo() {
    return drawableHolder.getFrameInfo();
  }

  public final boolean isActive() {
    return active;
  }

  public final int getFrameCount() {
    return frameCount;
  }

  public int getFrameRate() {
    return frameRate.getRate();
  }

  public final Timer getTimer() {
    return timer;
  }

  // ==============================================================================
  
  public final void start() {
    if (isInitialized() && !isInterrupted()) {
      loadSound();
      startSound();
      timer.start();
      active = true;

      while (!isInterrupted()) {
        if (active) {
          drawableHolder.display();
        }
      }

      stopSound();
    }

    active = false;
    disposeSound();
    closeWindow();
  }

  public final void stop() {
    setInterrupted();
    active = false;
    closeWindow();
    disposeSound();
  }

  public final synchronized void pause() {
    active = !active;
    pauseSound();
    pauseGraph(); // timer at the end so as to have isPaused() accurate as long as possible
  }

  public final boolean isPaused() {
    return timer.isPaused();
  }

  protected final void pauseGraph() {
    getTimer().pause();
  }

  // ==============================================================================

  protected abstract DrawableHolder openWindow(FrameInfo fi) throws Exception;
  
  protected void closeWindow() {
    if(drawableHolder != null) {
      drawableHolder.close();
      drawableHolder = null;
    }
  }

  protected final void switchFullScreen() {
    FrameInfo frameInfo = drawableHolder.getFrameInfo(); // get the frameinfo before the frame is destroyed

    boolean saveActive = active; // saving previous active state (in cas of pause or window reduction)
    active = false;
    pauseSound();
    closeWindow(); // kill current window

    frameInfo.switchFullScreen(); // toggle fullscreen/windowed mode

    // recreate openGL window
    try {
      drawableHolder = openWindow(frameInfo);
      active = saveActive; // restart display
      pauseSound();
    }
    catch(Exception e) {
      setInterrupted(); // quit if window not created
      e.printStackTrace();
    }
  }

  @Override
  public boolean isInitialized() {
    return isGraphInitialized() && isSoundInitialized();
  }

  public void initGraph() throws Exception {
    initGraph(new FrameInfo.Windowed(null, null, 640, 480, false));
  }

  public final void initGraph(FrameInfo fi) throws Exception {
    initGraph(fi, false);
  }

  public final void initGraph(FrameInfo fi, boolean askForFullScreen) throws Exception {
    // Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

    if (fi != null) {
      boolean canMaximize = Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH);
      if (!canMaximize) {
        fi.setFullScreen(false);
      }
      else if (askForFullScreen) {
        int answer = JOptionPane.showOptionDialog(null, "Run in fullscreen mode ?", "Question", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (answer == JOptionPane.YES_OPTION) {
          fi.setFullScreen(true);
        }
        else if (answer == JOptionPane.NO_OPTION) {
          fi.setFullScreen(false);
        }
        else {
          return;
        }
      }
    }

    drawableHolder = openWindow(fi);
  }

  protected boolean isGraphInitialized() {
    return (drawableHolder != null);
  }

  protected abstract void drawGLScene();

  protected abstract void reshapeGL();

  protected abstract boolean isSoundInitialized();

  protected abstract void initSound() throws Exception;

  protected abstract void loadSound();

  protected abstract void startSound();
  
  protected abstract void stopSound();

  protected abstract void disposeSound();

  protected abstract void pauseSound();

  // ==============================================================================

  /**
   * GLEventListener#init(GLDrawable) Called on each GLDrawable creation
   */
  @Override
  public void init(GLAutoDrawable drawable) {
    try {
      // no if(!isInitialized()) test here because init can be recalled on full screen switch for example.
      gl = (GL2) drawable.getGL();
      glu = new GLUgl2();

      drawableIntialized();
    }
    catch (Exception e) {
      e.printStackTrace();
      setInterrupted();
    }
  }

  @Override
  public final void display(GLAutoDrawable drawable) {
    if (isInitialized() && !isInterrupted()) {
      if (active) {
        try {
          // render the scene
          drawGLScene();
          frameCount++;

          // compute framerate
          long elapsed = timer.getElapsed();
          if (frameRate.checkPoint(elapsed)) {
            System.out.println(frameRate);
          }

          // System.out.println("image " + frameCount);
        }
        catch (Exception e) {
          e.printStackTrace();
          setInterrupted(); // stop here
        }
      }
    }
  }

  public final void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    // not implemented
  }

  @Override
  public final void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    // System.out.println("reshape");

    if (h == 0) {
      h = 1;
    }

    FrameInfo frameInfo = drawableHolder.getFrameInfo();
    frameInfo.setDrawableSize(w, h);

    if (!frameInfo.isFullScreen()) {
      frameInfo.setWindowSize(drawableHolder.getSize());
    }

    try {
      reshapeGL();
    }
    catch (Exception e) {
      e.printStackTrace();
      setInterrupted(); // stop here
    }
  }

  @Override
  public void dispose(GLAutoDrawable glAutoDrawable) {
    try {
      drawableDisposed();
    }
    catch (Exception e) {
      e.printStackTrace();
      setInterrupted(); // stop here
    }
  }

  // ==============================================================================

  @Override
  public boolean dispatchKeyEvent(KeyEvent e) {
    int type = e.getID();

    if (isInitialized() && !isInterrupted() && type == KeyEvent.KEY_RELEASED) {
      int code = e.getKeyCode();
      if (code == getKeyQuit()) {
        setInterrupted();
      }
      else if (code == getKeyFullScreen()) {
        switchFullScreen();
      }
      else if (code == getKeyPause()) {
        pause();

        Graphics graphics = (drawableHolder.getDrawable()).getGraphics();
        SwingUtilities.invokeLater(new PausePrinter(graphics));
      }
      else if (code == getKeyFrameRate()) {
        frameRate.setActive(!frameRate.isActive());
      }
      else {
        return false;
      }
    }
    return true;
  }

  protected int getKeyQuit() {
    return KeyEvent.VK_ESCAPE;
  }

  protected int getKeyFullScreen() {
    return KeyEvent.VK_F1;
  }

  protected int getKeyPause() {
    return KeyEvent.VK_SPACE;
  }

  protected int getKeyFrameRate() {
    return KeyEvent.VK_F;
  }

  // ==============================================================================

  @Override
  public void windowIconified(WindowEvent e) {
    if (!isPaused()) {
      pause();
    }
  }

  @Override
  public void windowActivated(WindowEvent e) {
    // nothing
  }

  @Override
  public void windowClosed(WindowEvent e) {
    // nothing
  }

  @Override
  public void windowClosing(WindowEvent e) {
    // nothing
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
    // nothing
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
    // nothing
  }

  @Override
  public void windowOpened(WindowEvent e) {
    // nothing
  }
  
  private final class PausePrinter implements Runnable {
    private Graphics graphics;
    
    public PausePrinter(Graphics graphics) {
      this.graphics = graphics;
    }
    
    @Override
    public void run() {
      GraphUtils.printMessage(graphics, "PAUSED", GraphUtils.getDefaultFont(), Color.gray, Color.lightGray, 20, 20, true); //FIXME doesn't always display in fullscreen and doesn't use trasnaprency in windowed mode
    }
  }
}