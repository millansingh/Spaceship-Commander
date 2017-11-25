package weapons.artillery.guns;

import weapons.Gun;
import weapons.artillery.proj.Proj_ArtilleryBasic;

public class Artillery_Basic extends Gun
{
	public Artillery_Basic()
	{
		super();
		initHealth = 250;
		health = 250;
		energyCost = 450;
		acc = 0.6;
		myProjectile = new Proj_ArtilleryBasic();
		setName(" Basic Artillery Cannons");
		weight=2.5;
	}
}