package weapons.cannon.guns;

import weapons.cannon.proj.Proj_ShellHeavy;

public class Canon_Heavy extends Canon_Basic
{
	public Canon_Heavy()
	{
		super();
		initHealth = 75;
		health = 75;
		energyCost = 110;
		acc = 0.55;
		myProjectile = new Proj_ShellHeavy();
		setName(" Heavy Cannons");
		weight=1.5;
	}
}