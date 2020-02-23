package tower.defense;

/*
 * File Name: Path.java
 *   Created: Feb 17, 2018
 *    Author: 
 */

public class Path extends Object
{
  private int x, y, width, height;
  public Path(int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public boolean containsCoord(int mouseX, int mouseY)
  {
    return (mouseX > x && mouseX <= x+width) && (mouseY > y && mouseY <= y+height);
  }
  
  public int getX()
  {
    return x;
  }
  
  public int getY()
  {
    return y;
  }
  
  public int getWidth()
  {
    return width;
  }
  
  public int getHeight()
  {
    return height;
  }
}
