package tower.defense;

/*
 * File Name: Minion.java
 *   Created: Feb 17, 2018
 *    Author: 
 */

public class Minion extends Object
{
  private int hp, speed, state, x, y, dist, totalhp;
  public static String[] dirs = new String[] {"r", "d", "l", "d", "r", "d", "l"};
  public static int[] dists = new int[] {810, 200, 750, 200, 750, 200, 800};
  public Minion(int x, int y, int speed, int hp)
  {
    this.hp = hp;
    this.totalhp = hp;
    this.speed = speed;
    this.state = 0;
    this.x = x;
    this.y = y;
  }
  
  public void move()
  {
    if (dist >= dists[state] && state + 1 < dirs.length)
    {
      dist = 0;
      state++;
    }
    if (dirs[state].equals("r")) x += speed;
    else if (dirs[state].equals("l")) x -= speed;
    else if (dirs[state].equals("d")) y += speed;
    else if (dirs[state].equals("u")) y -= speed;
    dist += speed;
  }
  
  public int getX()
  {
    return x;
  }
  
  public int getY()
  {
    return y;
  }
  
  public int getHP()
  {
    return hp;
  }
  
  public int getTotalHP()
  {
    return totalhp;
  }
  
  public void damage(int i)
  {
    hp -= i;
  }
  
  public int getState()
  {
    return state;
  }
  
  public int getDist()
  {
    return dist;
  }
}
