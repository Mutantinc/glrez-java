/**
* Copyright 2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.realtime.sound;

import java.net.URL;


public interface ModulePlayer {

  public void init() throws Exception;
  public void load(URL url);

  public void start();
  public void stream();

  public void pause();
  public boolean isPaused();

  public void stop();
  public void dispose();
  
  public int getOrd();
  public int getRow();
}
