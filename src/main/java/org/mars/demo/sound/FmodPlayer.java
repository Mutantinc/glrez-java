/**
* Copyright 2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.demo.sound;

import static org.jouvieje.fmodex.defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_HARDWARE;
import static org.jouvieje.fmodex.defines.FMOD_MODE.FMOD_OPENMEMORY;
import static org.jouvieje.fmodex.defines.FMOD_TIMEUNIT.FMOD_TIMEUNIT_MODORDER;
import static org.jouvieje.fmodex.defines.FMOD_TIMEUNIT.FMOD_TIMEUNIT_MODROW;
import static org.jouvieje.fmodex.enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;
import static org.jouvieje.fmodex.utils.SizeOfPrimitive.SIZEOF_INT;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.jouvieje.fmodex.Channel;
import org.jouvieje.fmodex.FmodEx;
import org.jouvieje.fmodex.Init;
import org.jouvieje.fmodex.Sound;
import org.jouvieje.fmodex.System;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.structures.FMOD_CREATESOUNDEXINFO;
import org.jouvieje.fmodex.utils.BufferUtils;
import org.mars.toolkit.realtime.sound.ModulePlayer;

/**
 * Module player using the NativeFmodEx projecft by Jerôme Jouvie
 * @see http://jerome.jouvie.free.fr
 */
public class FmodPlayer implements ModulePlayer {

  //FMod stuff
  private System system;

  private Sound sound; // music handle
  private Channel channel;

  private boolean paused;

  
  @Override
  public void init() throws Exception {
    try {
      Init.loadLibraries();

      system = new System();
      
      FMOD_RESULT result = FmodEx.System_Create(system);
      checkError(result, "Fmod System creation");
    
      system.init(32, FMOD_INIT_NORMAL, null);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  
  @Override
  public void load(URL url) {

    ByteBuffer modBuffer = loadModule(url);
    
    FMOD_CREATESOUNDEXINFO exinfo;
    exinfo = FMOD_CREATESOUNDEXINFO.allocate();
    exinfo.setLength(modBuffer.capacity());
    exinfo.setDecodeBufferSize(44100*8/125); //more than 4096 will cause more than 1 row to be buffered at once.
    //exinfo.setSuggestedSoundType(FMOD_SOUND_TYPE_XM);

    sound = new Sound();
    FMOD_RESULT result = system.createSound(modBuffer, FMOD_HARDWARE | FMOD_OPENMEMORY, exinfo, sound);
    checkError(result, "Module loading");
    
    exinfo.release();
  }


  @Override
  public void start() {
    channel = new Channel();
    system.playSound(FMOD_CHANNEL_FREE, sound, false, channel);
    //channel.setVolume(0.f);
  }
  
  @Override
  public void stream() {
    // FModEx does it alone
  }

  @Override
  public void stop() {
    if(channel != null) {
      channel.stop();
      channel = null;
    }
  }

  @Override
  public void dispose() {
    FMOD_RESULT result;

    if(sound != null) {
      result = sound.release();
      checkError(result, "sound release");
    }

    if(system != null) {
      result = system.close();
      checkError(result, "system close");
      result = system.release();
      checkError(result, "system release");
    }
  }

  @Override
  public synchronized void pause() {
    ByteBuffer buffer = BufferUtils.newByteBuffer(SIZEOF_INT);
    channel.getPaused(buffer);
    paused = (buffer.get(0) == 0); //0 means it's playing
    channel.setPaused(paused);
  }

  @Override
  public boolean isPaused() {
    return paused;
  }

  private static ByteBuffer loadModule(URL url) {
    try {
      URLConnection modCon = url.openConnection();
      int modSize = modCon.getContentLength(); //we will know the size, the file is local
      byte[] modData = new byte[modSize];

      DataInputStream modStream = new DataInputStream( modCon.getInputStream());
      modStream.readFully(modData);
      modStream.close();

      ByteBuffer bb = ByteBuffer.allocateDirect(modSize);
      bb.order(ByteOrder.LITTLE_ENDIAN);
      bb.put(modData);
      bb.clear();
      
      return bb;
    }
    catch(IOException e) {
      throw new RuntimeException(e);
    }
  }

  
  private void checkError(FMOD_RESULT result, String s) {
    if (result != FMOD_RESULT.FMOD_OK) {
      throw new RuntimeException("FMOD error: " + result.asInt() + " " + FmodEx.FMOD_ErrorString(result));
    }
  }
  

  @Override
  public int getOrd() {
    IntBuffer ib = BufferUtils.newIntBuffer(1);
    channel.getPosition(ib, FMOD_TIMEUNIT_MODORDER);
    return ib.get(0);
  }

  @Override
  public int getRow() {
    IntBuffer ib = BufferUtils.newIntBuffer(1);
    channel.getPosition(ib, FMOD_TIMEUNIT_MODROW);
    return ib.get(0);
  }

  @Override
  public String toString() {
    return "ord:" + getOrd() + ", row:" + getRow();
  }
}
