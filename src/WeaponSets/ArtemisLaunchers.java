package WeaponSets;

import Weaponry.Artemis_Launcher;
import Weaponry.Gun;

public class ArtemisLaunchers extends WeaponSet 
{

	public ArtemisLaunchers(int g, int a) 
	{
		super(g);
		gunArray = new Gun[g];
		name="Artemis Launchers";
		ammo=a;
		bInfiniteAmmo=false;
		
		for (int i=0;i<g;i++)
		{
			gunArray[i]=new Artemis_Launcher();
		}
	}

}
