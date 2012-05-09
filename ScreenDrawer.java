package game;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;
import java.util.*;

public class ScreenDrawer
{
	private Game game;
	private Graphics appg; //real graphics
	Dimension d; //size of applet
	private Image i; //buffer
	private Graphics2D g; //buffer graphics
	public ScreenDrawer(Game gam)
	{
		game = gam;
		UpdateDrawRegion();
	}
	
	public class ScreenState
	{
		public double X, Y, Zoom, Rot = 0.0;
		public ScreenState(double x, double y, double zoom)
		{
			X = x;
			Y = y;
			Zoom = zoom;
		}
		public Vect2 Game2App(Vect2 g)
		{
			//return new Vect2((g.X-X)*Zoom+d.width/2, (g.Y-Y)*Zoom+d.height/2);
			Point2D pt = g.toPoint2D();

			AffineTransform trans = new AffineTransform();
			trans.translate(X, -Y);
			trans.transform(pt, pt);

			if(Zoom != 1.0)
			{
				trans = new AffineTransform();
				trans.scale(Zoom, Zoom);
				trans.transform(pt, pt);
			}

			if(Rot != 0.0)
			{
				trans = new AffineTransform();
				trans.rotate(Rot);
				trans.transform(pt, pt);
			}

			trans = new AffineTransform();
			trans.translate(d.width/2.0, d.height/2.0);
			trans.transform(pt, pt);

			return new Vect2(pt);
		}
		public Vect2 App2Game(Vect2 a)
		{
			//return new Vect2((a.X-d.width/2)/Zoom+X, (a.Y-d.height/2)/Zoom+Y);
			Point2D pt = a.toPoint2D();
			
			AffineTransform trans = new AffineTransform();
			trans.translate(-d.width/2.0, -d.height/2.0);
			trans.transform(pt, pt);

			if(Rot != 0.0)
			{
				trans = new AffineTransform();
				trans.rotate(-Rot);
				trans.transform(pt, pt);
			}

			if(Zoom != 1.0)
			{
				trans = new AffineTransform();
				trans.scale(1.0/Zoom, 1.0/Zoom);
				trans.transform(pt, pt);
			}

			trans = new AffineTransform();
			trans.translate(X, Y);
			trans.transform(pt, pt);
			
			return new Vect2(pt);
		}
		public Vect2 AppSize()
		{
			return new Vect2(d);
		}
		public Vect2 App2Game()
		{
			Vect2 tl = App2Game(new Vect2(0, 0));
			Vect2 br = App2Game(new Vect2(d.width, d.height));
			return new Vect2(br.X-tl.X, br.Y-tl.Y);
		}
	}

	private class Text
	{
		Vect2 p;
		String str;
		Color color;
		public Text(Vect2 pos, String text, Color col)
		{
			p = pos;
			str = text;
			color = col;
		}
		public void Draw(ScreenState screen)
		{
			double X = screen.X, Y = screen.Y, Zoom = screen.Zoom;
			if(true)
			{
				g.setColor(color);
				g.drawString(str, (int)((p.X-X)*Zoom)+d.width/2, (int)((p.Y-Y)*Zoom)+d.height/2);
			}
		}
	}
	private ArrayList<Drawable> drawq = new ArrayList<Drawable>();
	private ArrayList<Text> textq = new ArrayList<Text>();
	public void DrawText(Vect2 p, String text, Color color)
	{
		textq.add(new Text(new Vect2(p), text, color));
	}
	public void Draw(Drawable dr)
	{
		drawq.add(dr);
	}
	private static final Color GridColor = new Color(255, 255, 255, 128);
	private static final double GridSize = 256.0;
	private static final double GridParallax = 0.7;
	private class Grid implements Drawable
	{
		ScreenState screen;
		public Grid(ScreenState Screen)
		{
			screen = Screen;
		}
		public Vect2 Pos()
		{
			return new Vect2(screen.X, screen.Y);
		}
		public Color GetColor()
		{
			return GridColor;
		}
		public Area GenerateArea()
		{
			int fw = d.width, fh = d.height;
			double maxsize = sqrt(fw*fw+fh*fh);
			int gl = (int)(maxsize/(GridSize*screen.Zoom))+1;
			double	xoff = fmod(screen.X*GridParallax, GridSize),
					yoff = fmod(screen.Y*GridParallax, GridSize);
			Area grid = new Area();
			for(int i = -(gl/=2); i <= gl+1; ++i)
			{
				grid.add(new Area(new Rectangle2D.Double
					(i*GridSize-xoff,-maxsize/2.0/screen.Zoom,
						1.0/screen.Zoom,maxsize/screen.Zoom)));
				grid.add(new Area(new Rectangle2D.Double
					(-maxsize/2.0/screen.Zoom,i*GridSize-yoff,
						maxsize/screen.Zoom,1.0/screen.Zoom)));
			}
			return grid;
		}
		public double Priority()
		{
			return Double.MIN_VALUE;
		}
		public int compareTo(Drawable dr)
		{
			return-1;
		}
	}
	private static double fmod(double val, double by)
	{
		double res = val/by;
		return by * (res-(long)res);
	}
	public void Display(ScreenState screen)
	{
		UpdateDrawRegion();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, d.width, d.height);
		drawq.add(0, new Grid(screen));
		double tx = d.width/2.0, ty = d.height/2.0;
		Vect2 pos;
		Area s;
		AffineTransform trans;
		for(Drawable dr : drawq)
		{
			pos = dr.Pos();
			s = dr.GenerateArea();
			trans = new AffineTransform();	trans.translate(pos.X-screen.X, pos.Y-screen.Y);	s = s.createTransformedArea(trans);
			if(screen.Zoom != 1.0)
			{trans = new AffineTransform();	trans.scale(screen.Zoom, screen.Zoom);				s = s.createTransformedArea(trans);}
			if(screen.Rot != 0.0)
			{trans = new AffineTransform();	trans.rotate(screen.Rot);							s = s.createTransformedArea(trans);}
			trans = new AffineTransform();	trans.translate(tx, ty);							s = s.createTransformedArea(trans);
			if(s.intersects(0, 0, d.width, d.height))
			{
				g.setColor(dr.GetColor());
				g.draw/*fill*/(s);
			}
		}
		drawq.clear();
		for(Text t : textq)
		{
			t.Draw(screen);
		}
		textq.clear();
		appg.drawImage(i, 0, 0, game.App());
	}
	private void UpdateDrawRegion()
	{
		if(!game.App().getSize().equals(d))
		{
			appg = game.App().getGraphics();
			d = game.App().getSize();
			i = game.App().createImage(d.width, d.height);
			g = (Graphics2D)i.getGraphics();
		}
	}
}