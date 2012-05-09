package game;

import java.awt.*;
import java.awt.geom.*;

public abstract class GameShape extends GameObject implements Drawable, Moveable
{
	protected double x, y, xVel, yVel, mass;
	protected boolean bkg, dmg, fixed;
	public GameShape(Vect2 p, double m)
	{
		x = p.X;
		y = p.Y;
		xVel = yVel = 0.0;
		mass = m;
		bkg = dmg = fixed = false;
	}
	public GameShape clone()
	{
		return (GameShape)super.clone();
	}
	public void Become(GameObject go)
	{
		GameShape gs = (GameShape)go;
		x = gs.x;
		y = gs.y;
		xVel = gs.xVel;
		yVel = gs.yVel;
		mass = gs.mass;
		bkg = gs.bkg;
		dmg = gs.dmg;
		fixed = gs.fixed;
	}
	
	public void Pos(Vect2 v)
	{
		x = v.X;
		y = v.Y;
	}
	public void Velocity(Vect2 v)
	{
		xVel = v.X;
		yVel = v.Y;
	}
	public void ApplyForce(Vect2 f, Vect2 p)
	{
		xVel += f.X;
		yVel += f.Y;
	}
	public void Mass(double Mass)
	{
		mass = Mass;
	}
	
	public void Normal()
	{
		bkg = dmg = false;
	}
	public void Background()
	{
		dmg = !(bkg = true);
	}
	public void Damaging()
	{
		bkg = !(dmg = true);
	}
	public void Finish()
	{
		bkg = dmg = true;
	}
	private static final Color HALF_WHITE = new Color(255, 255, 255, 128);
	public Color GetColor()
	{
		if(bkg && dmg) return Color.GREEN;
		else if(bkg) return HALF_WHITE;
		else if(dmg) return Color.RED;
		else return Color.WHITE;
	}
	public void Fixed(boolean state)
	{
		fixed = state;
	}
	
	public Vect2 Pos()		{ return new Vect2(x, y);		}
	public Vect2 Velocity()	{ return new Vect2(xVel, yVel);	}
	public double Mass()	{ return(mass);					}
	public boolean Fixed()	{ return(fixed);				}

	public boolean Overlapping(GameShape go)
	{
		if(this == go)
		{
			return false;
		}
		Area a = GenerateArea(), b;
		if(a == null || (b = go.GenerateArea()) == null)
		{
			return false;
		}
		a.intersect(b);
		return !a.isEmpty();
	}

	public int compareTo(Drawable dr)
	{
		double p1 = Priority(), p2 = dr.Priority();
		if(p1 < p2) return-1;
		if(p1 > p2) return+1;
		return 0;
	}
}