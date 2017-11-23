package HeroWeapons;

import Weaponry.Gun;
import WeaponSets.WeaponSet;

public class MantaRaySet extends WeaponSet 
{

	public MantaRaySet(int g) 
	{
		super(g);
		gunArray = new Gun[g];
		name="The Manta Ray";
		
		for (int i=0;i<g;i++)
		{
			gunArray[i]=new MantaRay();
		}
	}

}
