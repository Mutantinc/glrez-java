/**
* Copyright 2005-2013 <a href="mailto:fabmars@gmail.com">Fabien Marsaud</a>
*
* Stripped version from the ToolAudioVisual project
* 
* This software is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/
package org.mars.toolkit.realtime.graph;

import java.awt.Dimension;
import java.net.URL;

public final class Texture extends TextureId {
  private URL url;
  private boolean proxy;
  private boolean dstHasAlpha;
  private boolean minLinear;
  private boolean magLinear;
  private boolean repeat;
  private boolean mipmapped;

  private Dimension imgSize;
  private Dimension texSize;
  private float widthRatio;
  private float heightRatio;

  public Texture(URL url) {
    this(url, true);
  }

  public Texture(URL url, boolean linear) {
    this(url, linear, false);
  }

  public Texture(URL url, boolean linear, boolean repeat) {
    this(url, linear, linear, repeat);
  }

  public Texture(URL url, boolean minLinear, boolean magLinear, boolean repeat) {
    this(url, false, minLinear, magLinear, repeat);
  }

  public Texture(URL url, boolean dstHasAlpha, boolean minLinear, boolean magLinear, boolean repeat) {
    this(url, dstHasAlpha, minLinear, magLinear, repeat, true);
  }

  public Texture(URL url, boolean dstHasAlpha, boolean minLinear, boolean magLinear, boolean repeat, boolean mipmapped) {
    this(url, false, dstHasAlpha, minLinear, magLinear, repeat, mipmapped);
  }

  public Texture(URL url, boolean proxy, boolean dstHasAlpha, boolean minLinear, boolean magLinear, boolean repeat, boolean mipmapped) {
    this.url = url;
    this.proxy = proxy;
    this.dstHasAlpha = dstHasAlpha;
    this.minLinear = minLinear;
    this.magLinear = magLinear;
    this.repeat = repeat;
    this.mipmapped = mipmapped;
  }

  public boolean isDstHasAlpha() {
    return dstHasAlpha;
  }

  public void setDstHasAlpha(boolean dstHasAlpha) {
    this.dstHasAlpha = dstHasAlpha;
  }

  public boolean isMagLinear() {
    return magLinear;
  }

  public void setMagLinear(boolean magLinear) {
    this.magLinear = magLinear;
  }

  public boolean isMinLinear() {
    return minLinear;
  }

  public void setMinLinear(boolean minLinear) {
    this.minLinear = minLinear;
  }

  public boolean isMipmapped() {
    return mipmapped;
  }

  public void setMipmapped(boolean mipmapped) {
    this.mipmapped = mipmapped;
  }

  public URL getURL() {
    return url;
  }

  public void setURL(URL url) {
    this.url = url;
  }

  public boolean isRepeat() {
    return repeat;
  }

  public void setRepeat(boolean repeat) {
    this.repeat = repeat;
  }

  public boolean isProxy() {
    return proxy;
  }

  public void setProxy(boolean proxy) {
    this.proxy = proxy;
  }

  public Dimension getImgSize() {
    return new Dimension(imgSize);
  }

  public void setImgSize(Dimension imgSize) {
    setImgSize(imgSize.width, imgSize.height);
  }

  public void setImgSize(int imgWidth, int imgHeight) {
    this.imgSize = new Dimension(imgWidth, imgHeight);
    computeRatios();
  }

  public float getWidthRatio() {
    return widthRatio;
  }

  public float getHeightRatio() {
    return heightRatio;
  }

  public Dimension getTexSize() {
    return new Dimension(texSize);
  }

  public void setTexSize(Dimension texSize) {
    setTexSize(texSize.width, texSize.height);
  }

  public void setTexSize(int texWidth, int texHeight) {
    this.texSize = new Dimension(texWidth, texHeight);
    computeRatios();
  }

  private void computeRatios() {
    boolean doable = imgSize != null && texSize != null;

    if (doable && texSize.width != 0) {
      widthRatio = ((float) imgSize.width) / texSize.width;
    }
    else {
      widthRatio = 0.f;
    }

    if (doable && texSize.height != 0) {
      heightRatio = ((float) imgSize.height) / texSize.height;
    }
    else {
      heightRatio = 0.f;
    }
  }

  public int[] img2Tex(int x, int y) {
    return new int[] { (int) (x * widthRatio), (int) (y * heightRatio) };
  }

  public float[] img2Tex(float x, float y) {
    return new float[] { x * widthRatio, y * heightRatio };
  }

}
