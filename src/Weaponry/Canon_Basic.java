package Weaponry;

import Projectiles.Proj_ShellBasic;

public class Canon_Basic extends Gun
{

	public Canon_Basic()
	{
		super();
		initHealth = 50;
		health = 50;
		energyCost = 75;
		acc = 0.7;
		myProjectile = new Proj_ShellBasic();
		setName(" Basic Cannons");
		weight=0.75;
	}

}
