/**
* Copyright 2005-2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.realtime.graph;

public final class FrameRate {
  private boolean active;

  private int frequency;
  private long timeRef;
  private int count;

  private int rate;

  public FrameRate() {
    this(1000);
  }

  public FrameRate(int frequency) {
    setFrequency(frequency);
    setActive(active);
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;

    if (!active) {
      count = 0;
      rate = 0;
    }
  }

  public int getRate() {
    return rate;
  }

  public boolean checkPoint(long timePoint) {
    if (active) {
      count++;

      long elapsed = (timePoint - timeRef);

      if (elapsed > frequency) // time to compute the framerate once again
      {
        rate = (count * 1000) / (int) elapsed;
        count = 0;
        timeRef = timePoint;
        return true;
      }
    }
    return false;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  @Override
  public String toString() {
    return (rate > 0 ? Integer.toString(rate) : "??") + ".fps";
  }
}
