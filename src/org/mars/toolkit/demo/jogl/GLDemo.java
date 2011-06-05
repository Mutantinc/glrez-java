package org.mars.toolkit.demo.jogl;

import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.mars.toolkit.demo.FrameInfo;
import org.mars.toolkit.demo.FrameRate;
import org.mars.toolkit.demo.GraphUtils;
import org.mars.toolkit.demo.Timer;


public abstract class GLDemo extends GLBase implements GLEventListener, WindowListener, KeyEventDispatcher
{
  private GLFrame frame;
  
  private boolean active;
  private Timer timer;
  private int frameCount;

  private FrameRate frameRate = new FrameRate();

  protected GLDemo()
  {
    KeyboardFocusManager keybFocusManager = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager();
    keybFocusManager.addKeyEventDispatcher(this);
  }
  
  public final GLFrame getFrame()
  {
    return frame;
  }
  
  public final FrameInfo getFrameInfo()
  {
    return frame.getFrameInfo();
  }
  
  public final GLCanvas getGLCanvas()
  {
    return frame.getCanvas();
  }
  
  public final boolean isActive()
  {
    return active;
  }

  public final int getFrameCount()
  {
    return frameCount;
  }

  public int getFrameRate()
  {
    return frameRate.getRate();
  }
  
  public final Timer getTimer()
  {
    return timer;
  }
  
//==============================================================================

  protected void start()
  {
    start( new FrameInfo(null, 800, 600, false));
  }

  protected final void start(FrameInfo fi)
  {
    start(fi, false);
  }
  
  protected final void start(FrameInfo fi, boolean askForFullScreen)
  {
    //Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

    if(fi != null)
    {
      boolean canMaximize = Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH);
      if(!canMaximize) {
        fi.setFullscreen(false);
      }
      else if(askForFullScreen)
      {
        int answer = JOptionPane.showOptionDialog(null, "Run in fullscreen mode ?", "Question", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if(answer == JOptionPane.YES_OPTION) {
          fi.setFullscreen(true);
        }
        else if(answer == JOptionPane.NO_OPTION) {
          fi.setFullscreen(false);
        }
        else {
          return;
        }
      }
    }

    if(!createGLWindow(fi)) { // quit if window not created
      setInterrupted();
    }

    if(!initSound()) {
      setInterrupted();
    }
    
    if(isInitialized() && !isInterrupted())
    {
      timer = new Timer();
      timer.start();
      active = true;

      while(!isInterrupted())
      {
        if(active) {
          frame.getCanvas().display();
        }
      }

      GLTextureLoader.deleteAll(getGL());
      killSound();
      killGLWindow();
    }
  }
  
  public final synchronized void pause()
  {
    active = !active;
    pauseSound();
    pauseGraph(); //timer at the end so as to have isPaused() accurate as long as possible
  }
  
  public final boolean isPaused()
  {
    return timer.isPaused();
  }
  
  protected final void pauseGraph()
  {
    getTimer().pause();
  }
  
  //==============================================================================
  
  protected final boolean createGLWindow(FrameInfo fi)
  {
    GLCanvas canvas = GLBase.makeGLCanvas(this);
    frame = new GLFrame(fi, canvas);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Toolkit tk = Toolkit.getDefaultToolkit();
    boolean canMaximize = tk.isFrameStateSupported(Frame.MAXIMIZED_BOTH);
    if(fi.isFullscreen() && canMaximize)
    {
      frame.setUndecorated(true);
      frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      frame.showCursor(false);
    }
    else
    {
      frame.setUndecorated(false);
      frame.setSize(fi.getWindowSize());
      frame.showCursor(true);
    }

    frame.setVisible(true);
    frame.addWindowListener(this);
    
    return true; //TODO
  }

  
  protected final void killGLWindow()
  {
    active = false;
    frame.setVisible(false);
    frame.dispose();
    frame = null;
  }
  
  protected final void switchFullSCreen()
  {
    FrameInfo frameInfo = frame.getFrameInfo(); //get the frameinfo before the frame is destroyed

    boolean saveActive = active; //saving previous active state (in cas of pause or window reduction)
    killGLWindow();                       // kill current window
    
    frameInfo.switchFullScreen();               // toggle fullscreen/windowed mode

    // recreate openGL window
    if(!createGLWindow(frameInfo)) {
      setInterrupted(); // quit if window not created
    }
    else {
      active = saveActive; //restart display
    }
  }

  protected abstract void drawGLScene() throws Exception;
  protected abstract void reshapeGL() throws Exception;

  protected abstract boolean initSound();
  protected abstract void killSound();
  protected abstract void pauseSound();
   
//==============================================================================

  public final  void display(GLAutoDrawable drawable)
  {
    if(isInitialized() && !isInterrupted())
    {
      if(active)
      {
        try
        {
          //render the scene
          drawGLScene();
          frameCount++;

          //compute framerate
          long elapsed = timer.getElapsed();
          if(frameRate.checkPoint(elapsed)) {
            System.out.println(frameRate);
          }

          //System.out.println("image " + imgage);
        }
        catch(Exception e)
        {
          e.printStackTrace();
          setInterrupted(); //stop here
        }
      }
    }
  }

  public final void displayChanged( GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
  {
    //not implemented
  }

  public final void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) 
  {
    //System.out.println("reshape");
    
    if(h == 0) {
      h = 1;
    }

    FrameInfo frameInfo = frame.getFrameInfo();
    frameInfo.setCanvasSize(w, h);
    
    if(!frameInfo.isFullscreen()) {
      frameInfo.setWindowSize(frame.getSize());
    }
    
    try {
      reshapeGL();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      setInterrupted(); //stop here
    }
  }

//==============================================================================

  public boolean dispatchKeyEvent( KeyEvent e )
  {
    int type = e.getID();

    if(isInitialized() && !isInterrupted() && type == KeyEvent.KEY_RELEASED)
    {
      int code = e.getKeyCode();
      if(code == getKeyQuit())
      {
        setInterrupted();
      }
      else if(code == getKeyFullScreen())
      {
        switchFullSCreen();
      }
      else if(code == getKeyPause())
      {
        pause();
        
        SwingUtilities.invokeLater( new Runnable() {
          public void run() {
            GraphUtils.printMessage(frame.getCanvas().getGraphics(), "PAUSED", GraphUtils.getDefaultFont(), Color.gray, Color.lightGray, 20, 20, true);
          }
        });

      }
      else if(code == getKeyFrameRate())
      {
        frameRate.setActive(!frameRate.isActive());
      }
      else {
        return false;
      }
    }
    return true;
  }
  
  protected int getKeyQuit()
  {
    return KeyEvent.VK_ESCAPE;
  }
  
  protected int getKeyFullScreen()
  {
    return KeyEvent.VK_F1;
  }
  
  protected int getKeyPause()
  {
    return KeyEvent.VK_SPACE;
  }

  protected int getKeyFrameRate()
  {
    return KeyEvent.VK_F;
  }

 //==============================================================================

  public void windowIconified(WindowEvent e)
  {
    if(!isPaused()) {
      pause();
    }
  }

  public void windowActivated( WindowEvent e) {}
  public void windowClosed( WindowEvent e) {}
  public void windowClosing( WindowEvent e) {}
  public void windowDeactivated( WindowEvent e) {}
  public void windowDeiconified( WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}
  
}