package com.rez.glrez;

import java.awt.Color;

public final class Particle
{
  private float d; //distance/center
  private float y;  //height

  private float a; //angle
  private int z; //angle
  
  private Color color;

 
  public Particle(float d, float y, float a, int z, Color color)
  {
    setD(d);
    setY(y);
    
    setA(a);
    setZ(z);
    
    setColor(color);
  }
  
  
  public float getA()
  {
    return a;
  }

  public void setA( float a )
  {
    this.a = a;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor( Color color )
  {
    this.color = color;
  }

  public float getD()
  {
    return d;
  }

  public void setD( float d )
  {
    this.d = d;
  }

  public float getY()
  {
    return y;
  }

  public void setY( float y )
  {
    this.y = y;
  }

  public int getZ()
  {
    return z;
  }

  public void setZ( int z )
  {
    this.z = z;
  }
  
}
