import java.applet.*;
import java.awt.*;
import game.*;

public class Game_Test extends Applet
{
	Thread gt;
	Game game;
	public void init()
	{
		this.resize(640, 480);
		game = new Game(this);
		setFocusable(true);
	}
	public void start()
	{
		gt = new Thread(game, "Game Thread");
		gt.start();
	}
	public synchronized void stop()
	{
		if(gt != null && gt.isAlive())
		{
			gt.interrupt();
		}
		gt = null;
	}
	
	public void paint(Graphics g){}
}