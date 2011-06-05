package org.mars.toolkit.demo;

import java.awt.Dimension;


public final class Texture extends TextureId
{
  private String path;
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

  
  public Texture(String path)
  {
    this(path, true);
  }

  public Texture(String path, boolean linear)
  {
    this(path, linear, false);
  }
  
  public Texture(String path, boolean linear, boolean repeat)
  {
    this(path, linear, linear, repeat);
  }
  
  public Texture(String path, boolean minLinear, boolean magLinear, boolean repeat)
  {
    this(path, false, minLinear, magLinear, repeat);
  }

  public Texture(String path, boolean dstHasAlpha, boolean minLinear, boolean magLinear, boolean repeat)
  {
    this(path, dstHasAlpha, minLinear, magLinear, repeat, true);
  }

  public Texture(String path, boolean dstHasAlpha, boolean minLinear, boolean magLinear, boolean repeat, boolean mipmapped)
  {
    this(path, false, dstHasAlpha, minLinear, magLinear, repeat, mipmapped);
  }

  public Texture(String path, boolean proxy, boolean dstHasAlpha, boolean minLinear, boolean magLinear, boolean repeat, boolean mipmapped)
  {
    this.path = path;
    this.proxy = proxy;
    this.dstHasAlpha = dstHasAlpha;
    this.minLinear = minLinear;
    this.magLinear = magLinear;
    this.repeat = repeat;
    this.mipmapped = mipmapped;
  }

  
  public boolean isDstHasAlpha()
  {
    return dstHasAlpha;
  }

  public void setDstHasAlpha( boolean dstHasAlpha )
  {
    this.dstHasAlpha = dstHasAlpha;
  }

  public boolean isMagLinear()
  {
    return magLinear;
  }

  public void setMagLinear( boolean magLinear )
  {
    this.magLinear = magLinear;
  }

  public boolean isMinLinear()
  {
    return minLinear;
  }

  public void setMinLinear( boolean minLinear )
  {
    this.minLinear = minLinear;
  }

  public boolean isMipmapped()
  {
    return mipmapped;
  }

  public void setMipmapped( boolean mipmapped )
  {
    this.mipmapped = mipmapped;
  }

  public String getPath()
  {
    return path;
  }

  public void setPath( String path )
  {
    this.path = path;
  }

  public boolean isRepeat()
  {
    return repeat;
  }

  public void setRepeat( boolean repeat )
  {
    this.repeat = repeat;
  }


  public boolean isProxy()
  {
    return proxy;
  }


  public void setProxy( boolean proxy )
  {
    this.proxy = proxy;
  }

  public Dimension getImgSize()
  {
    return new Dimension(imgSize);
  }

  public void setImgSize( Dimension imgSize )
  {
    setImgSize(imgSize.width, imgSize.height);
  }

  public void setImgSize( int imgWidth, int imgHeight )
  {
    this.imgSize = new Dimension(imgWidth, imgHeight);
    computeRatios();
  }

  public float getWidthRatio()
  {
    return widthRatio;
  }

  public float getHeightRatio()
  {
    return heightRatio;
  }

  public Dimension getTexSize()
  {
    return new Dimension(texSize);
  }

  public void setTexSize( Dimension texSize )
  {
    setTexSize(texSize.width, texSize.height);
  }

  public void setTexSize( int texWidth, int texHeight )
  {
    this.texSize = new Dimension(texWidth, texHeight);
    computeRatios();
  }

  private void computeRatios()
  {
    boolean doable = imgSize != null && texSize != null;
    
    if (doable && texSize.width != 0) {
      widthRatio = ((float) imgSize.width)/texSize.width;
    }
    else {
      widthRatio = 0.f;
    }

    if (doable && texSize.height != 0) {
      heightRatio = ((float) imgSize.height)/texSize.height;
    }    
    else {
      heightRatio = 0.f;
    }
  }

  public int[] img2Tex(int x, int y)
  {
    return new int[]{(int)(x*widthRatio), (int)(y*heightRatio)};
  }

  public float[] img2Tex(float x, float y)
  {
    return new float[]{x*widthRatio, y*heightRatio};
  }
  
}
