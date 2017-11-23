package Weaponry;

import Projectiles.Proj_Artemis;

public class Artemis_Launcher extends Gun 
{
	public Artemis_Launcher()
	{
		super();
		initHealth=75;
		health=75;
		energyCost=0;
		acc = 0.85;
		myProjectile = new Proj_Artemis();
		setName(" Artemis Missiles");
		weight=0.5;
	}
}