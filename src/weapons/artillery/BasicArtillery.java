package weapons.artillery;

import weapons.Gun;
import weapons.WeaponSet;
import weapons.artillery.guns.Artillery_Basic;

public class BasicArtillery extends WeaponSet
{
	public BasicArtillery(int g)
	{
		super(g);
		gunArray = new Gun[g];
		name="Basic Artillery";
		
		for (int i=0;i<gunArray.length;i++)
		{
			gunArray[i]=new Artillery_Basic();
		}
	}
}