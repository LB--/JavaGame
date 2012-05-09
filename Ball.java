package game;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;

public class Ball extends GameShape
{
	double r;

	public Ball(Vect2 p, double m, double radius)
	{
		super(p, m);
		r = radius;
	}
	public Ball clone()
	{
		return (Ball)super.clone();
	}
	public void Become(GameObject go)
	{
		super.Become(go);
		Ball from = (Ball)go;
		r = from.r;
	}

	public void Size(double Radius)
	{
		r = Radius;
	}
	
	public void Move()
	{
		if(!fixed)
		{
			x += xVel;
			y += yVel;
		}
		else
		{
			xVel = yVel = 0.0;
		}
	}
	public boolean Overlapping(GameShape go)
	{
		if(go instanceof Ball)
		{
			Ball b = (Ball)go;
			return this != b && sqrt(pow(b.x-x, 2.0) + pow(b.y-y, 2.0)) <= r + b.r;
		}
		else return super.Overlapping(go);
	}
	public void Bounce(Moveable o)
	{
/*		if(fixed) return;
		if(!o.Fixed())
		{
			Vect2 pos = Pos(), opos = o.Pos();
			Vect2 Dn = pos.sub(opos);
			double delta = Dn.length();
			Dn.normalize();
			Vect2 Dt = new Vect2(Dn.Y, -Dn.X);
			if(delta == 0.0)
			{
				x += 0.01;
				return;
			}
			double m1 = mass;
			double m2 = o.Mass();
			double M = m1 + m2;

			Vect2 mT = Dn.mul(r + o.NearestRadius(pos) - delta);

			Pos(pos.add(mT.mul(m2/M)));

			Vect2 v1 = Velocity();
			Vect2 v2 = o.Velocity();

			Vect2 v1n = Dn.mul(Vect2.dot(v1, Dn));
			Vect2 v1t = Dt.mul(Vect2.dot(v1, Dt));

			Vect2 v2n = Dn.mul(Vect2.dot(v2, Dn));
			Vect2 v2t = Dt.mul(Vect2.dot(v2, Dt));

			Velocity(v1t.add(Dn.mul((m1 - m2) / M * v1n.length() + 2.0 * m2 / M * v2n.length())));
		}
		else
		{
			Vect2 pos = Pos(), opos = o.Pos();
			Vect2 dist = opos.sub(pos);
			if(dist.length() == 0.0)
			{
				x += 0.01;
				return;
			}
			double realdist = o.NearestRadius(pos)+r;
			dist = dist.mul(realdist/dist.length());
			Pos(opos.sub(dist));
			dist = pos.sub(opos);
			Vect2 bv = Velocity();
			double n = new Vect2(dist).ATan2();
			double i = bv.ATan2();
			double r = n-(i-n);
			Velocity(new Vect2(Vect2.ORIGIN, bv.length(), r));
		}
*/	}
	public static void Bounce(Ball a, Ball b)
	{
		if(a.fixed && b.fixed) return;
		if(b.fixed)
		{
			Ball t = a;
			a = b;
			b = t;
		}
		if(!a.fixed)
		{
			//http://en.wikipedia.org/wiki/Elastic_collision#Two-dimensional_C.23_example
			Vect2 Dn = a.Pos().sub(b.Pos());
			double delta = Dn.length();
			Dn.normalize();
			Vect2 Dt = new Vect2(Dn.Y, -Dn.X);
			if(delta == 0.0)
			{
				b.x += 0.01;
				return;
			}
			double m1 = a.mass;
			double m2 = b.mass;
			double M = m1 + m2;

			Vect2 mT = Dn.mul(a.r + b.r - delta);

			a.Pos(a.Pos().add(mT.mul(m2/M)));
			b.Pos(b.Pos().sub(mT.mul(m1/M)));

			Vect2 v1 = a.Velocity();
			Vect2 v2 = b.Velocity();

			Vect2 v1n = Dn.mul(Vect2.dot(v1, Dn));
			Vect2 v1t = Dt.mul(Vect2.dot(v1, Dt));

			Vect2 v2n = Dn.mul(Vect2.dot(v2, Dn));
			Vect2 v2t = Dt.mul(Vect2.dot(v2, Dt));

			a.Velocity(v1t.add(Dn.mul((m1 - m2) / M * v1n.length() + 2.0 * m2 / M * v2n.length())));
			b.Velocity(v2t.sub(Dn.mul((m2 - m1) / M * v2n.length() + 2.0 * m1 / M * v1n.length())));

//			a.Move();
//			b.Move();
		}
		else
		{
			Vect2 dist = a.Pos().sub(b.Pos());
			if(dist.length() == 0.0)
			{
				b.x += 0.01;
				return;
			}
			double realdist = a.r+b.r;
			dist = dist.mul(realdist/dist.length());
			b.Pos(a.Pos().sub(dist));
			dist = b.Pos().sub(a.Pos());
			Vect2 bv = b.Velocity();
			double n = new Vect2(dist).ATan2();
			double i = bv.ATan2();
			double r = n-(i-n);
			b.Velocity(new Vect2(cos(r)*bv.length(), sin(r)*bv.length()));
//			b.Move();
		}
	
	}

	public double Size()	{ return r;	}
	public double Priority(){ return r;	}

/*	public Shape GenerateShape(ScreenDrawer.ScreenState s)
	{
		Vect2 sz = s.AppSize();
		double size = r*2.0*s.Zoom;
		return new Ellipse2D.Double((x-r-s.X)*s.Zoom+sz.X/2.0, (y-r-s.Y)*s.Zoom+sz.Y/2.0, size, size);
	}*/
	public Area GenerateArea()
	{
		double size = r*2.0;
		Shape s;
		s = new Ellipse2D.Double(-r, -r, size, size);
		return new Area(s);
	}
}