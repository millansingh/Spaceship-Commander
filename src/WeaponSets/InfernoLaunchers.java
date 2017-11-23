package WeaponSets;

import Weaponry.Gun;
import Weaponry.Inferno_Launcher;

public class InfernoLaunchers extends WeaponSet
{

	public InfernoLaunchers(int g, int a)
	{
		super(g);
		gunArray = new Gun[g];
		name="Inferno Launchers";
		ammo=a;
		bInfiniteAmmo=false;
		
		for (int i=0;i<g;i++)
		{
			gunArray[i]=new Inferno_Launcher();
		}
	}

}
