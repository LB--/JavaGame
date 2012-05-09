package game;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import static java.lang.Math.*;

public class Vect2 implements Cloneable
{
	public static final Vect2 ORIGIN = new Vect2();
	public double X, Y;

	public Vect2()
	{
		X = Y = 0.0;
	}
	public Vect2(Vect2 from)
	{
		X = from.X;
		Y = from.Y;
	}
	public Vect2(double x, double y)
	{
		X = x;
		Y = y;
	}
	public Vect2(Vect2 p, double r, double theta)
	{
		X = cos(theta)*r;
		Y = sin(theta)*r;
		add(p);
	}
	public Vect2(Dimension d)
	{
		X = d.getWidth();
		Y = d.getHeight();
	}
	public Vect2(Point2D pt)
	{
		X = pt.getX();
		Y = pt.getY();
	}

	public Vect2 add(Vect2 v)
	{
		return new Vect2(X+v.X, Y+v.Y);
	}
	public Vect2 sub(Vect2 v)
	{
		return new Vect2(X-v.X, Y-v.Y);
	}
	public Vect2 mul(double d)
	{
		return new Vect2(X*d, Y*d);
	}
	public Vect2 div(double d)
	{
		return new Vect2(X/d, Y/d);
	}
	public double length()
	{
		return sqrt(pow(X, 2.0)+pow(Y, 2.0));
	}
	public void normalize()
	{
		double len = length();
		X /= len;
		Y /= len;
	}
	public static double dot(Vect2 a, Vect2 b)
	{
		return a.X*b.X + a.Y*b.Y;
	}
	
	public double ATan2()
	{
		return Math.atan2(Y, X);
	}
	public Vect2 Abs()
	{
		return new Vect2(abs(X), abs(Y));
	}

	public String toString()
	{
		return"("+X+", "+Y+")";
	}
	public Dimension toDimension()
	{
		Dimension d = new Dimension();
		d.setSize(X, Y);
		return d;
	}
	public Point2D toPoint2D()
	{
		return new Point2D.Double(X, Y);
	}
	public Vect2 toPolar() //from cartesian (r, theta)=(X, Y)
	{
		return new Vect2(length(), -atan2(Y, X));
	}
	public Vect2 toCartesian() //from polar (r, theta)=(X, Y)
	{
		return new Vect2(cos(Y)*X, sin(Y)*X);
	}
}