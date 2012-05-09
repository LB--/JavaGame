package game;

public abstract class GameObject implements Cloneable
{
	public GameObject clone()
	{
		try
		{
			return (GameObject)super.clone();
		}
		catch(CloneNotSupportedException e){return null;}
	}
	public abstract void Become(GameObject go);
}