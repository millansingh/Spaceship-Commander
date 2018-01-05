package systems;

import java.util.ArrayList;

import weapons.*;
import crew.Crew;
import game.Ship;
import weapons.Gun;
import weapons.WeaponSet;

public class Weapons extends Part 
{
	//guns=# of guns, damage=damage/shot, accuracy=accuracy of shot, ROF=rate of fire/gun, EnergyPerGun=energy used per turn per gun.
	private int initGuns, EnergyPerGun;
	//public Gun[] gunArray;
	public WeaponSet[] Weapons;
	public int weapRepairIndex;
	private ArrayList<Crew> weapRepairCrew;
	
	public Weapons(int h, int w, int s, WeaponSet[] we)
	{
		super(h,w,s,0,false);
		Weapons = we;
		AISkill=0;
		name="Weapon System";
		weapRepairCrew = new ArrayList<Crew>();
	}
	
	public int getGunCount(int x)
	{
		int g=0;
		for(int i=0;i<Weapons[x].gunArray.length;i++)
		{
			if (Weapons[x].gunArray[i].isOperational())
			{
				g++;
			}
		}
		return Math.min(g,Weapons[x].ammo);
	}
	
	public int getInitGunCount(int x)
	{
		int g=0;
		for(int i=0;i<Weapons[x].gunArray.length;i++)
		{
			g++;
		}
		return g;
	}
	
	public int getTotalGunCount()
	{
		int g=0;
		for (int i=0;i<Weapons.length;i++)
		{
			g+=getGunCount(i);
		}
		
		return g;
	}
	
	public int getInitGuns()
	{
		return initGuns;
	}
	
	public int getDamage(int x)
	{
		return Weapons[x].gunArray[0].myProjectile.damage;
	}
	
	public int getEnergyPerGun()
	{
		return EnergyPerGun;
	}
	
	public int getEnergyUsed()
	{
		int total=0;
		
		for (int x=0;x<Weapons.length;x++)
		{
			if (Weapons[x].gunArray.length>0)
			{
				total+=Weapons[x].gunArray[0].energyCost*getNumGunsToFire(x);
			}
		}
		
		return total;
	}
	
	public int getEnergyUsed(int x)
	{
		return Weapons[x].gunArray[0].energyCost*getNumGunsToFire(x);
	}
	
	public void setGunsToFire(int tot, int x)
	{
		for (int i=0; i<Weapons[x].gunArray.length; i++)
		{
			Weapons[x].gunArray[i].isFiring = false;
		}
		
		int min=0;
		for (int i=1;i<Weapons[x].gunArray.length;i++)
		{
			if (Weapons[x].gunArray[i].isOperational() && Weapons[x].gunArray[i].health<=Weapons[x].gunArray[min].health && !Weapons[x].gunArray[i].isFiring)
			{
				min=i;
			}
		}
		
		for (int j=0;j<tot;j++)
		{
			int h=0;
			for (int k=0;k<Weapons[x].gunArray.length;k++)
			{
				if (Weapons[x].gunArray[k].isOperational() && Weapons[x].gunArray[k].health>=Weapons[x].gunArray[min].health && !Weapons[x].gunArray[k].isFiring && k!=min)
				{
					h=k;
				}
			}
			
			Weapons[x].gunArray[h].isFiring=true;
		}
		
		if (tot==getGunCount(x) && Weapons[x].gunArray[min].isOperational())
		{
			Weapons[x].gunArray[min].isFiring=true;
		}
	}
	
	public void resetGuns()
	{
		for (WeaponSet w : Weapons)
		{
			w.resetGuns();
		}
	}
	
	public void setAim(int a, int i)
	{
		Weapons[i].aim=a;
	}
	
	public int getNumGunsToFire(int x)
	{
		int g = 0;
		for (Gun gu : Weapons[x].gunArray)
		{
			if (gu.isFiring)
			{
				g++;
			}
		}
		return g;
	}
	
	public int getWeight()
	{
		int w=0;
		for (int x=0;x<Weapons.length;x++)
		{
			for (int i=0;i<Weapons[x].gunArray.length;i++)
			{
				w+=Weapons[x].gunArray[i].weight;
			}
		}
		return w+weight;
	}
	
	public void lastStep()
	{
		for (int i=0;i<Weapons.length;i++)
		{
			for (Gun g : Weapons[i].gunArray)
			{
				g.isFiring=false;
			}
		}
	}
	
	public String[] getStringArray()
	{
		String[] s = new String[Weapons.length];
		for (int i=0;i<s.length;i++)
		{
			s[i] = Weapons[i].name;
		}
		
		return s;
	}
	
	public double getAccuracyMod()
	{
		return 1 + (((double)health/2)/initHealth)*getCrewEfficiency();
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		
		for (int i=0;i<Weapons.length;i++)
		{
			if (i<Weapons.length-1)
			{
				result.append(Weapons[i].initGuns + " " + Weapons[i].name + ", ");
			}
			else
			{
				result.append(Weapons[i].initGuns + " " + Weapons[i].name);
			}
		}
		
		if (!result.toString().equals(null))
		{
			return result.toString();
		}
		else
		{
			return null;
		}
	}
	
	public void updateAmmo()
	{
		for (int i=0;i<Weapons.length;i++)
		{
			Weapons[i].ammo-=getNumGunsToFire(i);
		}
	}
	
	//Weapon Repairs:
	public ArrayList<Crew> getWeapRepairCrew()
	{
		return weapRepairCrew;
	}
	
	public int getWeapRepairCrewNum()
	{
		return weapRepairCrew.size();
	}
	
	private int getWeapRepairSkill()
	{
		int s=0;
		for (Crew c : weapRepairCrew)
		{
			s+=c.getRepairSkill();
		}
		return s;
	}
	
	public void clearWeapRepairCrew()
	{
		for (int c=0;c<weapRepairCrew.size();c++)
		{
			weapRepairCrew.get(c).assignment=-1;
			weapRepairCrew.get(c).isOnFire=false;
			weapRepairCrew.remove(c);
			c--;
		}
	}
	
	public void clearWeapRepairCrewHalf()
	{
		int half = weapRepairCrew.size()/2;
		
		for (int c=0;c<weapRepairCrew.size();c++)
		{
			if (half>0)
			{
				weapRepairCrew.get(c).assignment=-1;
				weapRepairCrew.get(c).isOnFire=false;
				weapRepairCrew.remove(c);
				c--;
				half--;
			}
		}
	}
	
	public boolean canRepairWeap(int s)
	{
		if (s==0)
		{
			return false;
		}
		
		for (WeaponSet w : Weapons)
		{
			if (w.isDamaged())
			{
				return true;
			}
		}
		return false;
	}
	
	public void repairStep(Ship s)
	{
		int r = Math.min(getWeapRepairSkill(),s.getScrap());
		s.setScrap(s.getScrap()-r);
		
		r=Weapons[weapRepairIndex].repairStep(r);
		for (WeaponSet w : Weapons)
		{
			r=w.repairStep(r);
		}
	}
}