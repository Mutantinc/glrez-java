/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a> and <a href="mailto:rez@chiptune.com">Christophe Résigné</a>
* 
* Java port of the GLRez intro released at the Breakpoint 2005
* @see http://www.pouet.net/prod.php?which=16327
* @see http://www.chiptune.com
* 
* Original C source code on GitHub
* @see https://github.com/chiptune/glrez
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package com.chiptune.glrez.scene;


public class Mapping extends Toggle implements Synchronizable {

  private float m_t; // synchro time

  @Override
  public float getSynchroTime() {
    return m_t;
  }

  @Override
  public void setSynchroTime(float m_t) {
    this.m_t = m_t;
  }
}
