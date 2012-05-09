package game;

public interface Moveable
{
	public void Move();
	public boolean Overlapping(GameShape go);
	public void Bounce(Moveable m);
	public void ApplyForce(Vect2 f, Vect2 p);

	public Vect2 Pos();
	public double Mass();
	public Vect2 Velocity();
	public boolean Fixed();
}