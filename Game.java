package game;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import static java.lang.Math.*;
import java.util.*;

public class Game implements Runnable
{
	private Applet app;
	private KeyHandler keys;
	private MouseHandler mouse;
	public Game(Applet applet)
	{
		app = applet;
		keys = new KeyHandler(applet);
		mouse = new MouseHandler(applet);
	}
	public Applet App()
	{
		return app;
	}

	private boolean kLeft()
	{
		return keys.Key(KeyEvent.VK_LEFT)
			|| keys.Key(KeyEvent.VK_A);
	}
	private boolean kRight()
	{
		return keys.Key(KeyEvent.VK_RIGHT)
			|| keys.Key(KeyEvent.VK_D);
	}
	private boolean kUp()
	{
		return keys.Key(KeyEvent.VK_UP)
			|| keys.Key(KeyEvent.VK_W);
	}
	private boolean kDown()
	{
		return keys.Key(KeyEvent.VK_DOWN)
			|| keys.Key(KeyEvent.VK_S);
	}
	private boolean kJump()
	{
		return keys.Key(KeyEvent.VK_SPACE);
	}
	private boolean kzIn()
	{
		return keys.Key(KeyEvent.VK_I);
	}
	private boolean kzOut()
	{
		return keys.Key(KeyEvent.VK_O);
	}
	private boolean krCcw()
	{
		return keys.Key(KeyEvent.VK_PERIOD);
	}
	private boolean krCw()
	{
		return keys.Key(KeyEvent.VK_COMMA);
	}

	public class Player extends Ball
	{
		public Player()
		{
			super(Vect2.ORIGIN, 1.0, 15.0);
		}
		public Color GetColor()
		{
			return Color.GREEN;
		}
		public double Priority()
		{
			return Double.MAX_VALUE;
		}
	}
	
	private Level level;
	private ScreenDrawer.ScreenState screen;
	private ScreenDrawer _sd;
	private Player p;
	public void run()
	{
		try{Thread.sleep(500);}catch(InterruptedException e){return;}
		_sd = new ScreenDrawer(this);
		screen = _sd.new ScreenState(320.0, 240.0, 1.0);
		level = new Level(this, p = new Player());
		{
			Ball tl, tr, bl, br;
			level.Add(tl = new Ball(new Vect2(0.0, 0.0), 1.0, 10.0));
			level.Add(tr = new Ball(new Vect2(640.0, 0.0), 1.0, 10.0));
			level.Add(br = new Ball(new Vect2(640.0, 480.0), 1.0, 10.0));
			level.Add(bl = new Ball(new Vect2(0.0, 480.0), 1.0, 10.0));
			tl.Fixed(true); tl.Damaging();
			tr.Fixed(true);
			bl.Fixed(true);
			br.Fixed(true); br.Background();
			level.Add(new Link(tl, tr, 640.0, 8.0));
			level.Add(new Link(tr, br, 480.0, 8.0));
			level.Add(new Link(br, bl, 640.0, 8.0));
			level.Add(new Link(bl, tl, 480.0, 8.0));
/**/			level.Add(new Link(tl, p, 240.0, 8.0));
			level.Add(new Ball(new Vect2(600.0, 240.0), 1.0, 10.0));
		}
		p.Pos(new Vect2(320.0, 240.0));
		for(;;)
		{
//			long t1 = System.currentTimeMillis();

			Move();
			Draw(_sd);
			app.getToolkit().sync();

//			long time = System.currentTimeMillis()-t1;

//			if(time < 5)
//			{
				try{Thread.sleep(/*5-time*/0, 0);}catch(InterruptedException e){return;}
//			}
		}
	}
	private double Accel = 0.01;
	private static final double DbgZS = 5.0;
	private static final double ZoomRate = 0.05;
	public void Move()
	{
		if(mouse.Right)
		{
			Vect2 pos = screen.App2Game(new Vect2(mouse.X, mouse.Y));
			level.Add(new Ball(pos, 1.0/abs(screen.Zoom), 10.0/abs(screen.Zoom)));
		}
		for(int i = 0; i < 5; ++i)
		{
			p.ApplyForce(new Vect2(((kRight()?1:0)-(kLeft()?1:0))*Accel, 0.0), Vect2.ORIGIN);
			if(kJump())
			{
				//
			}
			level.Move();
		}
		if(mouse.Left)
		{
			p.Pos(screen.App2Game(new Vect2(mouse.X, mouse.Y)));
			p.Velocity(Vect2.ORIGIN);
			screen.X += (kRight()?1:0)/screen.Zoom*DbgZS;
			screen.X -= (kLeft()?1:0)/screen.Zoom*DbgZS;
			screen.Y -= (kUp()?1:0)/screen.Zoom*DbgZS;
			screen.Y += (kDown()?1:0)/screen.Zoom*DbgZS;
		}
		else
		{
			Vect2 pos = p.Pos();
			screen.X = pos.X;
			screen.Y = pos.Y;
		}
		screen.Zoom *= 1.0 + (kzIn()?ZoomRate:0.0) - (kzOut()?ZoomRate:0.0);
		if(mouse.Scrolled != 0.0)
		{
			screen.Zoom /= 1.0 + mouse.Scrolled*ZoomRate*2.0;
			mouse.Scrolled = 0.0;
		}
		if(kzIn() && kzOut())
		{
			screen.Zoom = 1.0;
		}
		if(kUp() && kDown())
		{
			screen.Zoom = -1.0;
		}
		if(krCcw())
		{
			screen.Rot += PI/100.0;
		}
		if(krCw())
		{
			screen.Rot -= PI/100.0;
		}
		if(krCcw() && krCw())
		{
			screen.Rot = 0.0;
		}
	}
	public void Draw(ScreenDrawer sd)
	{
		level.Draw(sd);

		{
			double X = screen.X, Y = screen.Y, Zoom = screen.Zoom, Rot = screen.Rot;
			Dimension d = app.getSize();
			double fw = d.width, fh = d.height, hw = fw/2, hh = fh/2;
			Vect2 pos = p.Pos(), vel = p.Velocity();
			sd.DrawText(new Vect2(X-hw/Zoom+10/Zoom, Y-hh/Zoom+20/Zoom), "X: "+pos.X, Color.ORANGE);
			sd.DrawText(new Vect2(X-hw/Zoom+10/Zoom, Y-hh/Zoom+35/Zoom), "Y: "+pos.Y, Color.ORANGE);
			sd.DrawText(new Vect2(X-hw/Zoom+10/Zoom, Y-hh/Zoom+50/Zoom), "Zoom: "+Zoom, Color.ORANGE);
			sd.DrawText(new Vect2(X-hw/Zoom+10/Zoom, Y-hh/Zoom+65/Zoom), "Rotation: "+Rot, Color.ORANGE);
			sd.DrawText(new Vect2(X-hw/Zoom+10/Zoom, Y-hh/Zoom+80/Zoom), "X vel: "+vel.X, Color.ORANGE);
			sd.DrawText(new Vect2(X-hw/Zoom+10/Zoom, Y-hh/Zoom+95/Zoom), "Y vel: "+vel.Y, Color.ORANGE);
		}

		sd.Display(screen);
	}

	private class KeyHandler implements KeyListener
	{
		public KeyHandler(Applet a)
		{
			a.addKeyListener(this);
		}

		private HashSet<Integer> keys = new HashSet<Integer>();
		public void keyTyped(KeyEvent e){}
		public void keyPressed(KeyEvent e)
		{
			keys.add(e.getKeyCode());
		}
		public void keyReleased(KeyEvent e)
		{
			keys.remove(e.getKeyCode());
		}
		boolean Key(int code)
		{
			return keys.contains(code);
		}
	}

	private class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener
	{
		public MouseHandler(Applet a)
		{
			a.addMouseListener(this);
			a.addMouseMotionListener(this);
			a.addMouseWheelListener(this);
		}

		public int X, Y;
		public void mouseDragged(MouseEvent e){mouseMoved(e);}
		public void mouseMoved(MouseEvent e)
		{
			X = e.getX();
			Y = e.getY();
		}

		public boolean OnScreen, Left, Middle, Right;
		public void mouseClicked(MouseEvent e){}
		public void mouseEntered(MouseEvent e)
		{
			OnScreen = true;
		}
		public void mouseExited(MouseEvent e)
		{
			OnScreen = false;
		}
		public void mousePressed(MouseEvent e)
		{
			switch(e.getButton())
			{
			case MouseEvent.BUTTON1:
				Left = true;
				break;
			case MouseEvent.BUTTON2:
				Middle = true;
				break;
			case MouseEvent.BUTTON3:
				Right = true;
				break;
			}
		}
		public void mouseReleased(MouseEvent e)
		{
			switch(e.getButton())
			{
			case MouseEvent.BUTTON1:
				Left = false;
				break;
			case MouseEvent.BUTTON2:
				Middle = false;
				break;
			case MouseEvent.BUTTON3:
				Right = false;
				break;
			}
		}

		public double Scrolled;
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			Scrolled += e./*getPreciseWheelRotation*/getWheelRotation();
		}
	}
}