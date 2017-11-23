package Weaponry;

import Projectiles.Proj_Inferno;

public class Inferno_Launcher extends Gun
{
	public Inferno_Launcher()
	{
		super();
		initHealth=125;
		health=125;
		energyCost=0;
		acc = 0.85;
		myProjectile = new Proj_Inferno();
		setName(" Inferno Missiles");
		weight=0.5;
	}
}