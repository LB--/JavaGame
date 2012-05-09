package game;

import java.util.*;
import java.awt.*;
import static java.lang.Math.*;

public class Level
{
	private Game.Player p;
	private ArrayList<GameObject> Objects = new ArrayList<GameObject>();
	private ArrayList<Moveable> Moveables = new ArrayList<Moveable>();
	private ArrayList<Drawable> Drawables = new ArrayList<Drawable>();
	private ArrayList<GameShape> Shapes = new ArrayList<GameShape>();
	private /*temporary*/ ArrayList<Ball> Balls = new ArrayList<Ball>();

	public Level(Game game, Game.Player player)
	{
		Add(p = player);
	}
	
	public void Add(GameObject go)
	{
		Objects.add(go);
		if(go instanceof GameShape)
		{
			Shapes.add((GameShape)go);
			if(go instanceof Ball)
			{
				Balls.add((Ball)go);
			}
		}
		if(go instanceof Moveable)
		{
			Moveables.add((Moveable)go);
		}
		if(go instanceof Drawable)
		{
			Drawables.add((Drawable)go);
		}
	}
	
	private Vect2 gravity = new Vect2(0.0, 0.01);
	public void Move()
	{
		for(int i = 0; i < Balls.size(); ++i)
		{
			Ball a = Balls.get(i);
			for(int j = i+1; j < Balls.size(); ++j)
			{
				Ball b = Balls.get(j);
				if(a.Overlapping(b))
				{
//					GameObject ta = a.clone(), tb = b.clone();
//					ta.Bounce(b);
//					tb.Bounce(a);
//					a.Become(ta);
//					b.Become(tb);
					Ball.Bounce(a, b);
				}
			}
		}
		for(GameShape gs : Shapes)
		{
//			gs.ApplyForce(gravity);
			gs.Move();
		}
	}
	public void Draw(ScreenDrawer sd)
	{
		Collections.sort(Drawables);
		for(Drawable dr : Drawables)
		{
			sd.Draw(dr);
		}
	}
	
	public Game.Player Player(){ return(p); }
}