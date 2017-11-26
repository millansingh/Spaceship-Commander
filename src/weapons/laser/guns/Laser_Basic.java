package weapons.laser.guns;

import weapons.Gun;
import weapons.laser.proj.Proj_LaserBasic;

public class Laser_Basic extends Gun 
{
	public Laser_Basic()
	{
		super();
		initHealth = 75;
		health = 75;
		energyCost = 50;
		acc = 0.8;
		myProjectile=new Proj_LaserBasic();
		setName(" Basic Lasers");
		weight=0.5;
	}
}