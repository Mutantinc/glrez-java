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
import java.awt.Component;
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
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.gl2.GLUgl2;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.mars.toolkit.realtime.graph.FrameInfo;
import org.mars.toolkit.realtime.graph.FrameRate;
import org.mars.toolkit.realtime.graph.GraphUtils;
import org.mars.toolkit.time.Timer;

public abstract class GLDemo extends GLBase implements GLEventListener, WindowListener, KeyEventDispatcher {

  private GLFrame frame;

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

  public final GLFrame getFrame() {
    return frame;
  }

  public final FrameInfo getFrameInfo() {
    return frame.getFrameInfo();
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

  public void init() {
    init(new FrameInfo.Windowed(null, null, 640, 480, false));
  }

  public final void init(FrameInfo fi) {
    init(fi, false);
  }

  public final void init(FrameInfo fi, boolean askForFullScreen) {
    if(!isInitialized()) {
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

      try {
        openWindow(fi);
        initSound();
        setInitialized();
      }
      catch(Exception e) {
        setInterrupted();
        closeWindow();
        disposeSound();
        throw new RuntimeException(e);
      }
    }
    else {
      throw new RuntimeException("Already initialized!");
    }
  }

  public final void start() {
    if (isInitialized() && !isInterrupted()) {
      loadSound();
      startSound();
      timer.start();
      active = true;

      while (!isInterrupted()) {
        if (active) {
          frame.getDrawable().display();
        }
      }

      stopSound();
    }

    disposeSound();
    closeWindow();
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

  protected final void openWindow(FrameInfo fi) throws Exception {
    GLCanvas canvas = GLToolkit.makeGLCanvas(this);
    frame = new GLFrame(fi, canvas);
    frame.applyFrameInfo();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.addWindowListener(this);
  }

  protected final void closeWindow() {
    active = false;
    if(frame != null) {
      frame.setVisible(false);
      frame.dispose();
      frame = null;
    }
  }

  protected final void switchFullScreen() {
    FrameInfo frameInfo = frame.getFrameInfo(); // get the frameinfo before the frame is destroyed

    boolean saveActive = active; // saving previous active state (in cas of pause or window reduction)
    pauseSound();
    closeWindow(); // kill current window

    frameInfo.switchFullScreen(); // toggle fullscreen/windowed mode

    // recreate openGL window
    try {
      openWindow(frameInfo);
      active = saveActive; // restart display
      pauseSound();
    }
    catch(Exception e) {
      setInterrupted(); // quit if window not created
      e.printStackTrace();
    }
  }

  protected abstract void drawGLScene();

  protected abstract void reshapeGL();

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

      initGraph();
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

    FrameInfo frameInfo = frame.getFrameInfo();
    frameInfo.setCanvasSize(w, h);

    if (!frameInfo.isFullScreen()) {
      frameInfo.setWindowSize(frame.getSize());
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
      disposeGraph();
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

        Graphics graphics = ((Component)frame.getDrawable()).getGraphics();
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
      GraphUtils.printMessage(graphics, "PAUSED", GraphUtils.getDefaultFont(), Color.gray, Color.lightGray, 20, 20, true); //FIXME doesn't display in fullscreen and doen't use trasnaprency in windowed mode
    }
  }
}