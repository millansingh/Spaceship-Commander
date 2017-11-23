package HeroWeapons;

import Projectiles.Projectile;

public class MantaProj extends Projectile 
{
	public MantaProj()
	{
		super();
		damage=0;
		splashDamage=200;
		numSplash=10;
		shieldPiercing=0.9;
		splash=true;
	}
}