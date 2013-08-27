/**
* Copyright 2012 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.demo.sound;

import ibxm.Module;
import ibxm.edit.IBXM;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.mars.toolkit.realtime.sound.ModulePlayer;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALConstants;
import com.jogamp.openal.ALException;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

/**
 * This class streams an IBXM-decoded module through OpenAL
 * 
 * @see http://enigma-dev.org/forums/index.php?topic=730.0
 * @see http://kcat.strangesoft.net/openal-tutorial.html
 */
public class JoalPlayer implements ModulePlayer {

  private final static int sampleRate = 44100;
  private final static int sampleFormat = ALConstants.AL_FORMAT_STEREO16;

  // OpenAL stuff
  private AL al;
  private int source;
  private final static int NUM_SOUND_BUFFERS = 4; //if too high value, the module row at start will already be > 0 and some display will be missing. if too low value there will be glitches
  private int[] buffers = new int[NUM_SOUND_BUFFERS]; //holding sound data alternatively, like a framebuffer
  private ByteBuffer bufferData; // buffer to read from ibxm

  // module specifics
  private IBXM ibxm;

  private boolean paused;

  
  @Override
  public void init() throws Exception {
    
    //init openAL
    try {
      ALut.alutInit();
      al = ALFactory.getAL();

      // Create a source.
      int[] aSources = new int[1];
      al.alGenSources(1, aSources, 0);
      checkError("Error generating OpenAL source");
      source = aSources[0];

      // Generate buffers
      al.alGenBuffers(NUM_SOUND_BUFFERS, buffers, 0);
      checkError("Error generating OpenAL buffers");
    }
    catch(ExceptionInInitializerError e) {
      throw new RuntimeException("OpenAL lib isn't installed.\nGet it at http://connect.creativelabs.com/openal/", e);
    }
  }

  
  @Override
  public void load(URL url) {
    Module module = loadModule(url);
    ibxm = new IBXM(module, sampleRate);
  }

  @Override
  public void stream() {
    rollAudioBuffers();
    ensurePlaying(); // in case all buffers got read since latest app loop
  }
  
  @Override
  public void start() {
    try {
      bufferData = ByteBuffer.allocateDirect(ibxm.getMixBufferLength() * 2);
      bufferData.order(ByteOrder.LITTLE_ENDIAN);

      for(int i = 0 ; i < NUM_SOUND_BUFFERS; i++) {
        fillAudioBuffer();
        al.alBufferData(buffers[i], sampleFormat, bufferData, bufferData.limit(), sampleRate);
        checkError("Error filling buffer " + i);
      }
      
      //bind our buffers with a source
      al.alSourceQueueBuffers(source, NUM_SOUND_BUFFERS, buffers, 0);
      checkError("Error binding source to buffers");
    }
    catch (ALException e) {
      e.printStackTrace();
    }

    ensurePlaying();
  }

  private void ensurePlaying() {
    int[] aPlaying = new int[1];
    al.alGetSourcei(source, ALConstants.AL_SOURCE_STATE, aPlaying, 0);
    if(aPlaying[0] != ALConstants.AL_PLAYING) {
      al.alSourcePlay(source);
    }
    checkError("Error when starting playing");
  }
  
  @Override
  public void stop() {
    al.alSourceStop(source);
  }

  @Override
  public void dispose() {
    if(al != null) {
      al.alDeleteBuffers(buffers.length, buffers, 0);
      al.alDeleteSources(1, new int[]{source}, 0);
      ALut.alutExit();
    }
  }

  @Override
  public synchronized void pause() {
    if(paused) {
      al.alSourcePlay(source);
    }
    else {
      al.alSourcePause(source);
    }
    paused = !paused;
  }

  @Override
  public boolean isPaused() {
    return paused;
  }

  private static Module loadModule(URL url) {
    try {
      URLConnection modCon = url.openConnection();
      int modSize = modCon.getContentLength(); //we will know the size, the file is local
      byte[] modData = new byte[modSize];
      DataInputStream modStream = new DataInputStream( modCon.getInputStream());
      modStream.readFully(modData);
      modStream.close();
      
      return new Module(modData);
    }
    catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private void fillAudioBuffer() {
    bufferData.clear();
    
    int[] mixBuf = new int[ibxm.getMixBufferLength()];
    int mEnd = ibxm.getAudio(mixBuf) * 2;

    for( int mIdx = 0; mIdx < mEnd; mIdx++ ) {
      int ampl = mixBuf[ mIdx ];
      if( ampl > 32767 ) ampl = 32767;
      if( ampl < -32768 ) ampl = -32768;
      
      bufferData.put((byte)ampl);
      bufferData.put((byte)(ampl >> 8));
    }
    bufferData.flip();
  }

  private synchronized void rollAudioBuffers() {
    int[] aProcessed = new int[1];
    al.alGetSourcei(source, ALConstants.AL_BUFFERS_PROCESSED, aProcessed, 0);
    int processed = aProcessed[0];

    while(processed-- > 0) {
      int[] recycledBuffer = new int[1];
      al.alSourceUnqueueBuffers(source, 1, recycledBuffer, 0);
      checkError("Error unqueuing");

      fillAudioBuffer();
      al.alBufferData(recycledBuffer[0], sampleFormat, bufferData, bufferData.limit(), sampleRate);
      checkError("Error re-buffering");

      al.alSourceQueueBuffers(source, 1, recycledBuffer, 0);
      checkError("Error queuing");
    }
  }
  
  private void checkError(String s) {
    int error = al.alGetError(); 
    if(error != ALConstants.AL_NO_ERROR) {
        throw new ALException(s + ": " + error);
    }
  }

  
  @Override
  public int getOrd() {
    return ibxm.getSeqPos();
  }

  @Override
  public int getRow() {
    return ibxm.getRow();
  }

  @Override
  public String toString() {
    return "ord:" + getOrd() + ", row:" + getRow();
  }
}
