package tower.defense;

/*
 * File Name: TurretButton.java
 *   Created: Feb 17, 2018
 *    Author: 
 */

public class TurretButton extends Object
{
  private int x, y, size, extraHeight;
  private boolean isHighlighted;
  private Turret t;
  public TurretButton(int x, int y)
  {
    this.x = x;
    this.y = y;
    this.size = 90;
    this.extraHeight = 20;
    this.t = new Turret(0,0,0,0,0,0,0);
  }
  
  public TurretButton(int x, int y, Turret t)
  {
    this.x = x;
    this.y = y;
    this.size = 90;
    this.extraHeight = 20;
    this.t = t;
  }
  
  public boolean containsCoord(int mouseX, int mouseY)
  {
    return (mouseX > x && mouseX <= x+size) && (mouseY > y && mouseY <= y+size + extraHeight);
  }
  
  public int getX()
  {
    return x;
  }
  
  public int getY()
  {
    return y;
  }
  
  public int getSize()
  {
    return size;
  }
  
  public int getExtraHeight()
  {
    return extraHeight;
  }
  
  public boolean isHighlighted()
  {
    return isHighlighted;
  }
  
  public void setHighlight(boolean b)
  {
    isHighlighted = b;
  }
  
  public Turret getTurret()
  {
    Turret copy = new Turret(t.getX(), t.getY(), t.getWidth(), t.getHeight(), t.getRange(), t.getDamage(), t.getPrice(), t.getColor());
    return copy;
  }
}
