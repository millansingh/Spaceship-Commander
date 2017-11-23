package WeaponSets;

import Weaponry.*;

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