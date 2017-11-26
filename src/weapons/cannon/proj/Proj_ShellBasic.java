package weapons.cannon.proj;

import weapons.Projectile;

public class Proj_ShellBasic extends Projectile
{
	
	public Proj_ShellBasic() 
	{
		super();
		damage = 60;
		fireChance=0;
		shieldPiercing=0;
		splash=true;
		splashDamage=15;
		numSplash=2;
		//accuracyMod = 0.5;
	}

}
