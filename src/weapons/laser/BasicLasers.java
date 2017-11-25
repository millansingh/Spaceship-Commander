package weapons.laser;

import weapons.Gun;
import weapons.WeaponSet;
import weapons.laser.guns.Laser_Basic;

public class BasicLasers extends WeaponSet
{
	public BasicLasers(int g)
	{
		super(g);
		gunArray = new Gun[g];
		name="Basic Lasers";
		
		for (int i=0;i<gunArray.length;i++)
		{
			gunArray[i] = new Laser_Basic();
		}
	}
}