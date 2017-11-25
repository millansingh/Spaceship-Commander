package weapons;

public class Gun 
{
	public int health,initHealth;
	public double weight;
	public int energyCost;
	public double acc;
	public boolean isFiring;
	public Projectile myProjectile;
	private String name;
	
	public Gun ()
	{
//		health=h;
//		initHealth=h;
//		acc=a;
//		damage=d;
		isFiring=false;
	}
	
	public boolean isOperational()
	{
		if (health>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public double getAccuracy()
	{
		double temp=health/initHealth;
		if (temp<0.8)
		{
			return Math.max(acc*temp,acc*0.4);
		}
		else
		{
			return acc;
		}
	}
	
	public int getDamage()
	{
		return ((myProjectile.damage/2)+(int)(Math.random()*myProjectile.damage));
	}
	
	public int getFireDamage()
	{
		return ((myProjectile.fireDamage/2)+(int)(Math.random()*myProjectile.fireDamage));
	}
	
	public int getSplashDamage()
	{
		return (int)((myProjectile.splashDamage)*Math.random());
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String s)
	{
		name=s;
	}
}