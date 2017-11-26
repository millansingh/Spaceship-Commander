package weapons.hero.guns;

import weapons.artillery.guns.Artillery_Basic;
import weapons.hero.proj.MantaProj;

public class MantaRay extends Artillery_Basic 
{
	public MantaRay()
	{
		super();
		initHealth=1000;
		health=1000;
		energyCost=2000;
		acc=0.8;
		myProjectile=new MantaProj();
		setName("The Manta Ray");
		weight=10;
	}
}