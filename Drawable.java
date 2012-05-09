package game;

import java.awt.Color;
import java.awt.geom.Area;

public interface Drawable extends Comparable<Drawable>
{
	public Vect2 Pos();
	public Color GetColor();
	public Area GenerateArea();
	public double Priority();
}