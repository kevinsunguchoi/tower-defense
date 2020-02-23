package tower.defense;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*
 * File Name: TowerDefense.java
 *   Created: Feb 17, 2018
 *    Author: 
 */


public class TowerDefense extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener
{
  // Declare instance variables here...
  public Timer game = new Timer(10, this), turretFire = new Timer(150, this), minionSpawner = new Timer(500, this);
  private int mouseX, mouseY;
  private int gold = 300, hp = 500, score = 0;
  private final Font smallfont = new Font("Arial Black", 1, 16);
  private final Font font = new Font("Arial Black", 1, 24);
  private final Font bigfont = new Font("Arial Black", 1, 36);
  private final Font giantfont = new Font("Arial Black", 1, 96);
  private final TurretButton[] buttons = new TurretButton[6];
  private final Path[] paths = new Path[7];
  private ArrayList<Turret> turrets = new ArrayList<Turret>();
  private ArrayList<Minion> minions = new ArrayList<Minion>();
  private boolean placing, canPlace, gameOver;
  
  // Constructor
  public TowerDefense(int w, int h, JFrame f)
  {
    super.setOpaque(true);
    super.setPreferredSize(new Dimension(w, h));
    super.setBackground(new Color(10, 120, 10));
    super.addMouseListener(this);
    super.addMouseMotionListener(this);
    super.addKeyListener(this);
    super.requestFocusInWindow();
    buttons[0] = new TurretButton(15, 0*125 + 40, new Turret(20,20,50,50,75,5,150));
    buttons[1] = new TurretButton(15, 1*125 + 40, new Turret(20,20,50,50,100,5,250, new Color(50, 200, 100)));
    buttons[2] = new TurretButton(15, 2*125 + 40, new Turret(10,10,70,70,100,10,500, new Color(200, 50, 50)));
    buttons[3] = new TurretButton(15, 3*125 + 40, new Turret(10,10,70,70,150,10,800, new Color(50, 50, 200)));
    buttons[4] = new TurretButton(15, 4*125 + 40, new Turret(32,32,25,25,250,5,1500, new Color(200, 200, 50)));
    buttons[5] = new TurretButton(15, 5*125 + 40, new Turret(7,7,75,75,400,25,9999, new Color(200, 50, 200)));
    paths[0] = new Path(120,50, 850, 50);
    paths[1] = new Path(920, 100, 50, 200);
    paths[2] = new Path(170,250, 750, 50);
    paths[3] = new Path(170, 300, 50, 200);
    paths[4] = new Path(220,450, 750, 50);
    paths[5] = new Path(920, 500, 50, 200);
    paths[6] = new Path(120,650, 800, 50);
    game.start();
    turretFire.start();
  }
  
  public void newGame()
  {
    turrets.clear();
    minions.clear();
    gold = 300; 
    hp = 500; 
    score = 0;
    gameOver = false;
    placing = false;
    game.start();
    turretFire.start();
  }
  @Override
  public boolean isFocusable()
  {
    return true;
  }
  
  // Perform any custom painting (if necessary) in this method
  @Override  
  public void paintComponent(Graphics g2)
  {
    super.paintComponent(g2);
    Graphics2D g = (Graphics2D)g2;
    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHints(rh);
    g.setFont(font);
    
    //draw paths
    for (Path p : paths)
    {
      if (p != null) 
      {
        g.setColor(Color.GRAY);
        g.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
        if (p.getWidth() > p.getHeight())
        {
          g.setColor(new Color(110,110,110));
          for (int i = p.getX(); i <= (p.getX()+p.getWidth()); i += 20)
          {
            g.drawLine(i, p.getY(), i, p.getY()+p.getHeight());
          }
        }
        else
        {
          g.setColor(new Color(110,110,110));
          for (int i = p.getY(); i <= (p.getY()+p.getHeight()); i += 20)
          {
            g.drawLine(p.getX(), i, p.getX()+p.getWidth(), i);
          }
        }
      }
    }
    
    //debug coordinates
    /*g.setColor(Color.WHITE);
    //g.fillRect(super.getWidth()-240, 0, 240, 30);
    //g.setColor(Color.BLACK);
    g.drawString("X: " + mouseX + "   Y: " + mouseY, super.getWidth()-230, 20);*/
    
    //Placing turret outline
    if(placing)
    {
      Turret lastTurret = turrets.get(turrets.size()-1);
      int range = lastTurret.getRange();
      if (canPlace) g.setColor(new Color(0,255,0,55));
      else g.setColor(new Color(255,0,0,55));
      g.fillOval(mouseX-range, mouseY-range, 2*range, 2*range);
    }
    
    //Drawing turret highlight and attack
    for (Turret t : turrets)
    {
      if(t != null) 
      {
        if (t.isHighlighted() && t.isPlaced())
        {
          int tx = t.getX() + t.getWidth()/2;
          int ty = t.getY() + t.getWidth()/2;
          int range = t.getRange();
          g.setColor(new Color(255, 255, 255, 65));
          g.fillOval(tx-range, ty-range, 2*range, 2*range);
        }
        if (t.isAttacking() && t.isPlaced())
        {
          int tx = t.getX() + t.getWidth()/2;
          int ty = t.getY() + t.getWidth()/2;
          int range = t.getRange();
          g.setColor(new Color(t.getColor().getRed(), t.getColor().getGreen(), t.getColor().getBlue(),40));
          g.fillOval(tx-range, ty-range, 2*range, 2*range);
        }
      }
    }
    
    
    //drawing turrets
    for (Turret t : turrets)
    {
      if(t != null)
      {
        g.setColor(t.getColor());
        g.fillRect(t.getX(), t.getY(), t.getWidth(), t.getHeight());
      }
    }
    
    //drawing minions
    g.setColor(Color.WHITE);
    for (Minion m : minions)
    {
      if(m != null) 
      {
        g.setColor(Color.BLACK);
        g.fillRect(m.getX()-10, m.getY()-13, 40, 3);
        g.setColor(new Color(200, 0, 0));
        g.fillRect(m.getX()-10, m.getY()-13, (int)(1.0*m.getHP()/m.getTotalHP()*40), 3);
        
        if (m.getHP() <= 10) g.setColor(new Color(200,0,0));
        else if (m.getHP() <= 25) g.setColor(new Color(0,0,200));
        else if (m.getHP() <= 50) g.setColor(new Color(200,200,0));
        else if (m.getHP() <= 100) g.setColor(new Color(0,200,0));
        else if (m.getHP() <= 200) g.setColor(Color.BLACK);
        else if (m.getHP() <= 500) g.setColor(Color.WHITE);
        g.fillRect(m.getX(), m.getY(), 20,20);
      }
    }
    
    //Sidebar
    g.setColor(Color.BLACK);
    g.fillRect(0,0,120, super.getHeight());
    g.setColor(Color.WHITE);
    g.drawString("Turrets", 8, 25);
    
    //Drawing turret buttons
    for (TurretButton t : buttons)
    {
      if(t != null) 
      {
        Turret tur = t.getTurret();
        
        if (gold >= tur.getPrice())
        { 
          if (t.isHighlighted()) g.setColor(new Color(140,140,140));
          else g.setColor(new Color(70,70,70));
        }
        else
        {
          if (t.isHighlighted()) g.setColor(new Color(160,50,50));
          else g.setColor(new Color(110,30,30));
        }
        g.fillRect(t.getX(), t.getY(), t.getSize(), t.getSize() + t.getExtraHeight());
        
        //turret in box
        g.setColor(tur.getColor());
        g.fillRect(t.getX() + tur.getX(), t.getY()+tur.getY(), tur.getWidth(), tur.getHeight());
        if (gold < tur.getPrice()) 
        {
          g.setColor(new Color(110,30,30,65)); //fade image
          g.fillRect(t.getX() + tur.getX(), t.getY()+tur.getY(), tur.getWidth(), tur.getHeight());
        }
        
        //damage and price bg
        g.setColor(new Color(200, 0, 0));
        g.fillRect(t.getX(), t.getY()+t.getSize(), t.getSize()/2-10, t.getExtraHeight());
        g.setColor(new Color(210, 170, 0));
        g.fillRect(t.getX()+t.getSize()/2-10, t.getY()+t.getSize(), t.getSize()/2+10, t.getExtraHeight());
        
        //damage and prices
        g.setColor(Color.BLACK);
        g.setFont(smallfont);
        g.drawString(""+tur.getDamage(),t.getX()+5, t.getY()+t.getSize()+16);
        g.drawString(""+tur.getPrice(),t.getX()+t.getSize()/2-5, t.getY()+t.getSize()+16);
      }
    }
    
    //Bottom bar
    g.setFont(font);
    g.setColor(new Color(200, 0, 0));
    g.fillRect(120, super.getHeight()-50, 293, 50);
    g.setColor(new Color(210, 170, 0));
    g.fillRect(413, super.getHeight()-50, 293, 50);
    g.setColor(Color.BLACK);
    g.fillRect(706, super.getHeight()-50, 294, 50);
    g.setColor(Color.BLACK);
    g.drawString("Health: " + hp, 130, super.getHeight()-15);
    g.drawString("Gold: " + gold, 423, super.getHeight()-15);
    g.setColor(Color.WHITE);
    g.drawString("Score: " + score, 716, super.getHeight()-15);
    
    if (!minionSpawner.isRunning())
    {
      g.setFont(bigfont);
      g.setColor(Color.WHITE);
      g.drawString("Place a turret to start", 122, super.getHeight()-60);
    }
    
    //gameover text
    if (gameOver)
    {
      g.setColor(Color.BLACK);
      g.fillRect(120, super.getHeight()/2-100, super.getWidth()-120, 200);
      g.setFont(giantfont);
      g.setColor(Color.WHITE);
      g.drawString("Game over!", 120, super.getHeight()/2+20);
      g.setFont(bigfont);
      g.drawString("Click anywhere to restart", 122, super.getHeight()/2+70);
    }
  }
  
  // Process GUI input in this method
  @Override  
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == game)
    {
      for (int i = 0; i < minions.size(); i++)
      {
        minions.get(i).move();
        if (minions.get(i).getX() <= 110)
        {
          minions.remove(i);
          hp -= 10;
          if (hp == 0)
          {
            gameOver = true;
            game.stop();
            turretFire.stop();
            minionSpawner.stop();
          }
        }
      }
    }
    if (e.getSource() == turretFire)
    {
      for (int i = 0; i < turrets.size(); i++)
      {
        Turret t = turrets.get(i);
        t.setAttacking(false);
        ArrayList<Minion> minionsInRange = new ArrayList<Minion>();
        for (int j = 0; j < minions.size(); j++)
        {
          Minion m = minions.get(j);
          if (t.isPlaced() && isInRange(m, t))
          {
            minionsInRange.add(m);
          }
        }
        
        Minion farthestMinion = null;
        for (int j = 0; j < minionsInRange.size(); j++)
        {
          Minion m = minionsInRange.get(j);
          if (j == 0) farthestMinion = minionsInRange.get(0);
          else
          {
            if (m.getState() >= farthestMinion.getState() && m.getDist() >= farthestMinion.getDist())
            {
              farthestMinion = m;
            }
          }
        }
        if (farthestMinion != null)
        {
          int scoreaddition = t.getDamage();
            if (farthestMinion.getHP() - t.getDamage() < 0) scoreaddition = t.getDamage() - Math.abs(farthestMinion.getHP() - t.getDamage());
            farthestMinion.damage(t.getDamage());
            t.setAttacking(true);
            score += scoreaddition;
            if (farthestMinion.getHP() <= 0) 
            {
              gold += (int) (farthestMinion.getTotalHP()/1.5);
              minions.remove(farthestMinion);
            }
        }
      }
    }
    if (e.getSource() == minionSpawner)
    {
      int rand = 0;
      if (score <= 1000) rand = (int) (Math.random()*2);
      else if (score <= 2000) rand = (int) (Math.random()*3);
      else if (score <= 5000) rand = (int) (Math.random()*4);
      else if (score <= 10000) rand = (int) (Math.random()*5);
      else rand = (int) (Math.random()*6);
      
      if (rand == 0) minions.add(new Minion(120, 65, 5, 10));
      else if (rand == 1) minions.add(new Minion(120, 65, 5, 20));
      else if (rand == 2) minions.add(new Minion(120, 65, 6, 50));
      else if (rand == 3) minions.add(new Minion(120, 65, 2, 100));
      else if (rand == 4) minions.add(new Minion(120, 65, 1, 200));
      else if (rand == 5) minions.add(new Minion(120, 65, 1, 500));
      
    }
    super.repaint();
  }
  
  //<editor-fold defaultstate="collapsed" desc="--This method will launch your application--">
  public static void main(String[] args)
  {
    JFrame.setDefaultLookAndFeelDecorated(false);
    JFrame fr = new JFrame("Tower Defense");
    fr.setContentPane(new TowerDefense(1000, 800, fr));
    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fr.setLocation(10, 10);
    fr.setResizable(false);
    fr.pack();
    fr.setVisible(true);  
  }
  //</editor-fold>  

  @Override
  public void mouseClicked(MouseEvent e)
  {
    mouseX = e.getX();
    mouseY = e.getY();
    if (gameOver)
    {
      newGame();
    }
    if (mouseX < 120 && !placing)
    {
      for (TurretButton t : buttons)
      {
        if (t != null && t.containsCoord(mouseX, mouseY))
        {
          Turret tur = t.getTurret();
          if (gold - tur.getPrice() >= 0)
          {
            placing = true;
            canPlace = false;
            turrets.add(tur);
            turrets.get(turrets.size()-1).setPosition(mouseX-tur.getWidth()/2, mouseY-tur.getHeight()/2);
          }
        }
      }
    }
    if (placing)
    {
      Turret lastTurret = turrets.get(turrets.size()-1);
      if (SwingUtilities.isLeftMouseButton(e))
      {
        int w = lastTurret.getWidth()/2;
        int h = lastTurret.getHeight()/2;
        lastTurret.setPosition(mouseX-w, mouseY-h);
        placing = false;
        if (!checkPlacement(mouseX-w,mouseY-h).equals("none") || !checkPlacement(mouseX+w,mouseY-h).equals("none") || !checkPlacement(mouseX-w,mouseY+h).equals("none") || !checkPlacement(mouseX+w,mouseY+h).equals("none"))
        {
          placing = true;
        }
        if (gold - lastTurret.getPrice() < 0)
        {
          placing = true;
        }
        if (placing == false)
        {
          turrets.get(turrets.size()-1).place();
          gold -= lastTurret.getPrice();
          if (turrets.size() == 1 && !minionSpawner.isRunning()) minionSpawner.start();
        }
      }
      else if (SwingUtilities.isRightMouseButton(e))
      {
        turrets.remove(turrets.size()-1);
        placing = false;
      }
    }
    super.repaint();
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
  
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    mouseX = e.getX();
    mouseY = e.getY();
    super.repaint();
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    mouseX = e.getX();
    mouseY = e.getY();
    for (TurretButton t : buttons)
    {
      if (t != null)
      {         
        if (t.containsCoord(mouseX, mouseY))
        {
          t.setHighlight(true);
        }
        else
        {            
          t.setHighlight(false);
        }
      }
    }
    if (mouseX > 120)
    {
      for (Turret t: turrets)
      {
        if (t != null)
        {
          if (t.containsCoord(mouseX, mouseY) && !placing)
          {
            t.setHighlight(true);
          }
          else
          {
            t.setHighlight(false);
          }
        }
      }
    }
    
    if (placing)
    {
      Turret lastTurret = turrets.get(turrets.size()-1);
      int w = lastTurret.getWidth()/2;
      int h = lastTurret.getHeight()/2;
      lastTurret.setPosition(mouseX-w, mouseY-h);
      canPlace = true;
      if (!checkPlacement(mouseX-w,mouseY-h).equals("none") || !checkPlacement(mouseX+w,mouseY-h).equals("none") || !checkPlacement(mouseX-w,mouseY+h).equals("none") || !checkPlacement(mouseX+w,mouseY+h).equals("none"))
      {
        canPlace = false;
      }
    }
    super.repaint();
  }

  public String checkPlacement(int x, int y)
  {
    if (x < 120 || y > super.getHeight()-30)
    {
      return "menu";
    }
    for (Path p : paths)
    {
      if (p != null)
      {
        if (p.containsCoord(x, y)) 
        {
          return "path";
        }
      }
    }
    for (int i = 0; i < turrets.size()-1; i++)
    {
      Turret t = turrets.get(i);
      if (t != null)
      {
        if (t.containsCoord(x, y)) 
        {
          return "turret";
        }
      }
    }
    return "none";
  }
  
  public boolean isInRange(Minion m, Turret t)
  {
    int mx = m.getX() + 10;
    int my = m.getY() + 10;
    int tx = t.getX() + t.getWidth()/2;
    int ty = t.getY() + t.getHeight()/2;
    int tr = t.getRange();
    int dist = (int) Math.sqrt((mx - tx)*(mx-tx) + (my-ty)*(my-ty));
    return dist <= tr;
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
  
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    
  }
}
