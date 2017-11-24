package weapons.launcher.proj;

import weapons.Projectile;

public class Proj_Inferno extends Projectile
{
	public Proj_Inferno()
	{
		super();
		damage=125;
		splashDamage=0;
		numSplash=0;
		shieldPiercing=1.0;
		fireChance=0.90;
		fireDamage=100;
		splash=false;
	}
}