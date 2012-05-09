package game;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;

public class Link extends GameObject implements Drawable
{
	private Ball a, b;
	private double l, w;
	boolean bkg, dmg;

	public Link(Ball A, Ball B, double length, double thickness)
	{
		a = A;
		b = B;
		l = length;
		w = thickness;
		bkg = dmg = false;
	}
	public Link clone()
	{
		Link l = (Link)super.clone();
		l.a = a.clone();
		l.b = b.clone();
		return l;
	}
	public void Become(GameObject go)
	{
		Link from = (Link)go;
		a.Become(from.a);
		b.Become(from.b);
		l = from.l;
		w = from.w;
		bkg = from.bkg;
		dmg = from.dmg;
	}

	public void Length(double Length)
	{
		l = Length;
	}
	public void Thickness(double Thickness)
	{
		w = Thickness;
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
	
	public Ball A()						{ return a;		}
	public Ball B()						{ return b;		}
	public double Length()				{ return(l);	}
	public double Thickness()			{ return(w);	}

	private static final Color HALF_WHITE = new Color(255, 255, 255, 128);
	public Color GetColor()
	{
		if(bkg && dmg) return Color.GREEN;
		else if(bkg) return HALF_WHITE;
		else if(dmg) return Color.RED;
		else return Color.WHITE;
	}

/*	public Shape GenerateShape(ScreenDrawer.ScreenState screen)
	{
		double X = screen.X, Y = screen.Y, Zoom = screen.Zoom;
		Vect2 p1 = ba.Pos(), p2 = bb.Pos();
		double x1 = p1.X, y1 = p1.Y, x2 = p2.X, y2 = p2.Y, r = w/2;
		Vect2 sz = screen.AppSize();
		int hw = (int)sz.X/2, hh = (int)sz.Y/2;
		double A = -atan2(y2 - y1, x2 - x1);
		double px[] = new double[4], py[] = new double[4];
		px[0] = (x1-X+sin(A)*r)*Zoom + hw;
		py[0] = (y1-Y+cos(A)*r)*Zoom + hh;
		px[1] = (x2-X+sin(A)*r)*Zoom + hw;
		py[1] = (y2-Y+cos(A)*r)*Zoom + hh;
		px[2] = (x2-X-sin(A)*r)*Zoom + hw;
		py[2] = (y2-Y-cos(A)*r)*Zoom + hh;
		px[3] = (x1-X-sin(A)*r)*Zoom + hw;
		py[3] = (y1-Y-cos(A)*r)*Zoom + hh;
		Polygon ln = new Polygon();
		ln.addPoint((int)px[0], (int)py[0]);
		ln.addPoint((int)px[1], (int)py[1]);
		ln.addPoint((int)px[2], (int)py[2]);
		ln.addPoint((int)px[3], (int)py[3]);
		return ln;
	}*/
	public Vect2 Pos(){ return a.Pos(); }
	public Area GenerateArea()
	{
		Vect2 p2 = b.Pos().sub(a.Pos());
		double x1 = 0.0, y1 = 0.0, x2 = p2.X, y2 = p2.Y, r = w/2;
		double A = -atan2(y2 - y1, x2 - x1);
		double px[] = new double[4], py[] = new double[4];
		px[0] = x1+sin(A)*r;
		py[0] = y1+cos(A)*r;
		px[1] = x2+sin(A)*r;
		py[1] = y2+cos(A)*r;
		px[2] = x2-sin(A)*r;
		py[2] = y2-cos(A)*r;
		px[3] = x1-sin(A)*r;
		py[3] = y1-cos(A)*r;
		Polygon ln = new Polygon();
		ln.addPoint((int)px[0], (int)py[0]);
		ln.addPoint((int)px[1], (int)py[1]);
		ln.addPoint((int)px[2], (int)py[2]);
		ln.addPoint((int)px[3], (int)py[3]);
		return new Area(ln);
	}

	public double Priority(){ return w;	}
	public int compareTo(Drawable dr)
	{
		double p1 = Priority(), p2 = dr.Priority();
		if(p1 < p2) return-1;
		if(p1 > p2) return+1;
		return 0;
	}
}