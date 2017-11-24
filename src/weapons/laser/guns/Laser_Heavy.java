package weapons.laser.guns;

import weapons.laser.proj.Proj_LaserHeavy;

public class Laser_Heavy extends Laser_Basic 
{
	public Laser_Heavy()
	{
		super();
		initHealth = 125;
		health = 125;
		energyCost = 100;
		acc = 0.7;
		myProjectile=new Proj_LaserHeavy();
		setName(" Heavy Lasers");
		weight=1.0;
	}
}