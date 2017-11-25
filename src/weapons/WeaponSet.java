package weapons;

public class WeaponSet 
{
	public Gun[] gunArray;
	public int aim=-1,initGuns,ammo;
	public boolean bInfiniteAmmo;
	public String name;
	
	public WeaponSet(int g)
	{
		initGuns=g;
		ammo=100000;
		bInfiniteAmmo=true;
	}
	
	public void resetGuns()
	{
		for (Gun g : gunArray)
		{
			g.isFiring=false;
		}
	}
	
	public boolean isDamaged()
	{
		for (Gun g : gunArray)
		{
			if (g.health<g.initHealth)
			{
				return true;
			}
		}
		return false;
	}
	
	public int repairStep(int r)
	{
		int temp=r;
		
		for (Gun g : gunArray)
		{
			int h = g.initHealth-g.health;
			
			if (h<temp)
			{
				temp-=h;
				g.health=g.initHealth;
			}
			else
			{
				g.health+=temp;
				temp=0;
				break;
			}
		}
		
		return temp;
	}
}