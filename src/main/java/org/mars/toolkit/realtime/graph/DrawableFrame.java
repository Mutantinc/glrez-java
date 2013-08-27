/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.realtime.graph;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public abstract class DrawableFrame extends JFrame implements DrawableHolder {
  private static final long serialVersionUID = -3656505155924146982L;
  private FrameInfo frameInfo;
  private Cursor savedCursor;
  private final static Cursor transparentCursor;

  static {
    Toolkit tk = Toolkit.getDefaultToolkit();
    BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    transparentCursor = tk.createCustomCursor(cursor, new Point(0, 0), "");
  }

  /**
   * An override must add its own component that actually displays the stuff.
   * 
   * @param frameInfo
   */
  public DrawableFrame(FrameInfo frameInfo) {
    super();
    this.frameInfo = frameInfo;
  }

  public final Dimension getDrawableSize() {
    if (frameInfo != null) {
      return frameInfo.getDrawableSize();
    }
    else {
      return null;
    }
  }

  public final void setDrawableSize(Dimension dim) {
    setDrawableSize(dim.width, dim.height);
  }

  public final void setDrawableSize(int width, int height) {
    if (frameInfo != null) {
      frameInfo.setDrawableSize(width, height);

      if (!frameInfo.isFullScreen()) {
        frameInfo.setWindowSize(getSize());
      }
    }
  }

  @Override
  public final FrameInfo getFrameInfo() {
    return frameInfo;
  }

  public final void applyFrameInfo() {
    if (frameInfo != null) {
      frameInfo.tryToApply(this);
    }
  }

  public final void showCursor(boolean cursorVisible) {
    if (cursorVisible) {
      if (savedCursor != null) {
        setCursor(savedCursor);
      }
    }
    else {
      savedCursor = getCursor();
      setCursor(transparentCursor);
    }
  }

}
