package Projectiles;

public class Proj_ArtilleryBasic extends Projectile
{
	public Proj_ArtilleryBasic()
	{
		super();
		damage=350;
		splashDamage=50;
		numSplash=4;
		shieldPiercing=0.20;
		splash=true;
	}
}