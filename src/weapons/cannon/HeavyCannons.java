package weapons.cannon;

import weapons.cannon.guns.Canon_Heavy;

public class HeavyCannons extends BasicCannons
{
	
	public HeavyCannons(int g)
	{
		super(g);
		name="Heavy Cannons";
		
		for (int i=0;i<g;i++)
		{
			gunArray[i]=new Canon_Heavy();
		}
	}

}
