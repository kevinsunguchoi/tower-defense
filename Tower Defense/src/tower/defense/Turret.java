package tower.defense;

import java.awt.Color;

/*
 * File Name: Turret.java
 *   Created: Feb 17, 2018
 *    Author: 
 */

public class Turret extends Object
{
  private int x, y, width, height, range, damage, price;
  private boolean highlighted, placed, isAttacking;
  private Color color;
  public Turret(int x, int y, int width, int height, int range, int damage, int price)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.range = range;
    this.damage = damage;
    this.price = price;
    this.color = new Color(255,255,255);
  }
  
  public Turret(int x, int y, int width, int height, int range, int damage, int price, Color color)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.range = range;
    this.damage = damage;
    this.price = price;
    this.color = color;
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
  
  public int getRange()
  {
    return range;
  }
  
  public int getDamage()
  {
    return damage;
  }
  
  public boolean isHighlighted()
  {
    return highlighted;
  }
  
  public void setHighlight(boolean h)
  {
    highlighted = h;
  }
  public void setPosition(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public void place()
  {
    placed = true;
  }
  
  public boolean isPlaced()
  {
    return placed;
  }
  
  public int getPrice()
  {
    return price;
  }
  
  public Color getColor()
  {
    return color;
  }
  
  public void setAttacking(boolean a)
  {
    isAttacking = a;
  }
  
  public boolean isAttacking()
  {
    return isAttacking;
  }
}
