package org.mars.toolkit.demo;

public class TextureId
{
  public final static int ID_NONE = 0;
  
  private int id;

  public TextureId()
  {
    this(-1);
  }

  public TextureId(int id)
  {
    this.id = id;
  }

  public int getId()
  {
    return id;
  }

  public void setId( int id )
  {
    this.id = id;
  }
  
}
