package systems;

import java.awt.Color;
import java.util.ArrayList;

import crew.Crew;

public class Part
{
	public int health, initHealth, weight, skillNeeded, skillNeededOverload, AISkill;
	protected String name;
	public boolean isActive, isOverloading, isOnFire=false, extinguishing=false, isDestroyed=false;
	public int resetTimer, RESET_TIME, fireDamage, extinguishEnergy=0, breachSize;
	public double oxygenLevel;
	private ArrayList<Crew> crew,repairCrew;
	private boolean canOverload;
	
	public Part(int h, int w, int s, int so, boolean b)
	{
		health=h;
		initHealth=h;
		weight=w;
		isActive=true;
		isOverloading=false;
		canOverload=b;
		isOnFire=false;
		fireDamage=0;
		resetTimer=0;
		skillNeeded=s;
		skillNeededOverload=so;
		crew=new ArrayList<Crew>();
		repairCrew=new ArrayList<Crew>();
		oxygenLevel=1.0;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Color getButtonColor()
	{
		int healthRatio=(int)(255*Math.min((Math.max((double)health/initHealth, (double)0)),1));
		
		return new Color(255-healthRatio,healthRatio,0);
	}
	
	public int getCrewNum()
	{
		return crew.size();
	}
	
	public int getCrewNumNeeded()
	{
		if (isOverloading)
		{
			return getCrewNum()-getEngineersNeededOverload();
		}
		return getCrewNum();
	}
	
	public int getRepairCrewNum()
	{
		return repairCrew.size();
	}
	
	public ArrayList<Crew> getCrew()
	{
		return crew;
	}
	
	public ArrayList<Crew> getRepairCrew()
	{
		return repairCrew;
	}
	
	public ArrayList<Crew> getInjuredCrew()
	{
		ArrayList<Crew> cr = new ArrayList<Crew>();
		
		for (Crew c : crew)
		{
			if (c.health>0 && c.health<=50)
			{
				cr.add(c);
			}
		}
		
		return cr;
	}
	
	public int getInjuredCrewNum()
	{
		int i=0;
		
		for (Crew c : crew)
		{
			if (c.health>0 && c.health<=50)
			{
				i++;
			}
		}
		return i;
	}
	
	public int getRepairSkill()
	{
		int s=0;
		for (Crew c : repairCrew)
		{
			s+=c.getRepairSkill();
		}
		return s;
	}
	
	public void clearCrew()
	{
		for (int i=0;i<crew.size();i++)
		{
			crew.get(i).isOnFire=false;
			crew.get(i).assignment=-1;
			crew.remove(i);
			i--;
		}
	}
	
	public void clearRepairCrew()
	{
		for (int c=0;c<repairCrew.size();c++)
		{
			repairCrew.get(c).isOnFire=false;
			repairCrew.get(c).assignment=-1;
			repairCrew.remove(c);
			c--;
		}
	}
	
	public void clearRepairCrewHalf()
	{
		int half = repairCrew.size()/2;
		
		for (int c=0;c<repairCrew.size();c++)
		{
			if (half>0)
			{
				repairCrew.get(c).isOnFire=false;
				repairCrew.get(c).assignment=-1;
				repairCrew.remove(c);
				c--;
				half--;
			}
		}
	}
	
	public int manPart()
	{
		return getEngineersNeeded()-getCrewNum();
	}
	
	public void partHealthCrewCheck()
	{
		if (health<=0)
		{
			clearCrew();
		}
	}
	
	public void crewCheck()
	{
		for (int i=0;i<crew.size();i++)
		{
			if (crew.get(i).health<=0)
			{
				crew.remove(i);
				i--;
			}
		}
		for (int j=0;j<repairCrew.size();j++)
		{
			if (repairCrew.get(j).health<=0)
			{
				repairCrew.remove(j);
				j--;
			}
		}
	}
	
	public int getEngineerSkill()
	{
		int s=0;
		for (Crew c : crew)
		{
			s+=c.getEngineerSkill();
		}
		return s;
	}
	
	public int getEngineersNeeded()
	{
		int s = skillNeeded-AISkill;
		if (s%150==0)
		{
			return s/150;
		}
		else
		{
			return (s/150)+1;
		}
	}
	
	public int getEngineersNeededOverload()
	{
		int s = skillNeededOverload-AISkill;
		if (s%150==0)
		{
			return s/150;
		}
		else
		{
			return (s/150)+1;
		}
	}
	
	public double getCrewEfficiency()
	{
		
		return Math.min((double)(getEngineerSkill()+AISkill)/skillNeeded,1.0);
	}
	
	public boolean canOverload()
	{
		if (!isOverloading && getEngineerSkill()>=skillNeeded-AISkill && isActive && canOverload)
		{
			return true;
		}
		return false;
	}
	
	public boolean canRepair(int s)
	{
		if (health<initHealth && s>0)
		{
			return true;
		}
		
		return false;
	}
	
	public void setName(String s)
	{
		name=s;
	}
	
	public void initializeReset()
	{
		resetTimer = RESET_TIME;
		isActive = false;
		isOverloading = false;
	}
	
	public void initializeExtinguishers(int i)
	{
		if (i==0)
		{
			extinguishing=false;
		}
		else
		{
			extinguishing=true;
		}
		extinguishEnergy=i;
	}
	
	public void resetStep()
	{
		if (!isActive)
		{	
			if (resetTimer > 0)
			{
				resetTimer--;
			}
			
			if (resetTimer == 0)
			{
				isActive=true;
				resetDone();
			}
		}
	}
	
	//Placeholder function, in case I need to add to this.
	protected void resetDone()
	{
		return;
	}
	
	public void fireCheck()
	{
		oxygenLevel-=(double)fireDamage/1000;
		double oxyRatio=Math.min((oxygenLevel*1.4),1.0);
		
		if (extinguishing && isOnFire)
		{
			damageCrewFire(0.5);
			health=Math.max(health-fireDamage,0);
			fireDamage=(int)((fireDamage*oxyRatio)-(extinguishEnergy*0.5));
		}
		
		if (isOnFire && !extinguishing)
		{
			damageCrewFire(1);
			health=Math.max(health-fireDamage,0);
			fireDamage*=(1.35*oxyRatio);
		}
		
		if (fireDamage<=0)
		{
			isOnFire=false;
			fireDamage=0;
			extinguishing=false;
			extinguishEnergy=0;
			extinguishCrew();
		}
	}
	
	protected void damageCrewFire(double m)
	{
		for (Crew c : crew)
		{
			c.health-=(int)(m*(fireDamage/2)*((Math.random()/2)+0.5));
			c.isOnFire=true;
		}
		for (Crew c : repairCrew)
		{
			c.health-=(int)(m*(fireDamage/2)*((Math.random()/2)+0.5));
			c.isOnFire=true;
		}
	}
	
	protected void extinguishCrew()
	{
		for (Crew c : crew)
		{
			c.isOnFire=false;
		}
		for (Crew c : repairCrew)
		{
			c.isOnFire=false;
		}
	}
	
	public void oxygenCrew()
	{
		for (Crew c : crew)
		{
			if (oxygenLevel<0.5)
			{
				c.lowOxygen=true;
				double d = (1-(oxygenLevel))/2;
				c.health-=d*50;
			}
			else
			{
				c.lowOxygen=false;
			}
		}
	}
}