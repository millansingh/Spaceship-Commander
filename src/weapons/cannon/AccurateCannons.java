package weapons.cannon;

import weapons.cannon.guns.Canon_Accurate;

public class AccurateCannons extends BasicCannons
{
	
	public AccurateCannons(int g) 
	{
		super(g);
		name="Accurate Cannons";
		
		for (int i=0;i<g;i++)
		{
			gunArray[i]=new Canon_Accurate();
		}
	}
}