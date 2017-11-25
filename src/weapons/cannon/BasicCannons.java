package weapons.cannon;

import weapons.Gun;
import weapons.WeaponSet;
import weapons.cannon.guns.Canon_Basic;

public class BasicCannons extends WeaponSet
{
	
	public BasicCannons(int g) 
	{
		super(g);
		gunArray = new Gun[g];
		name="Basic Cannons";
		
		for (int i=0;i<g;i++)
		{
			gunArray[i]=new Canon_Basic();
		}
	}
}