package weapons.laser;

import weapons.Gun;
import weapons.WeaponSet;
import weapons.laser.guns.Laser_Heavy;

public class HeavyLasers extends WeaponSet 
{

	public HeavyLasers(int g) 
	{
		super(g);
		gunArray = new Gun[g];
		name="Heavy Lasers";
		
		for (int i=0;i<gunArray.length;i++)
		{
			gunArray[i] = new Laser_Heavy();
		}
	}

}
