package game;

import static java.lang.Math.*;

public class Tension //Angle-forcer
{
	int a; //Ball A
	int p; //Pivot Ball
	int b; //Ball B
	double r; //angle to maintain in radians
	double s; //Strength with which to maintain

	public Tension(int A, int Pivot, int B)
	{
		a = A;
		p = Pivot;
		b = B;
		r = /*Angle between A and B at P*/ PI;
		s = 1.0;
	}
	
	public void Angle(double radians)
	{
		r = radians;
	}
	public void Strength(double Strength) throws IllegalArgumentException
	{
		if(Strength < 0.0)
		{
			throw(new IllegalArgumentException("Strength must be positive"));
		}
		s = Strength;
	}

	public int BallA()		{ return(a); }
	public int Pivot()		{ return(p); }
	public int BallB()		{ return(b); }
	public double Angle()	{ return(r); }
	public double Strength(){ return(s); }

	public void Move()
	{
		//
	}
}