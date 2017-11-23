package Parts;

import java.util.ArrayList;

import Crew.Crew;
import Crew.Medic;

public class Medbay extends Part 
{

	private ArrayList<Crew> injured;
	private ArrayList<Medic> staff;
	public int threshold;
	
	public Medbay(int h, int w) 
	{
		super(h, w, 0, 0, false);
		injured=new ArrayList<Crew>();
		staff=new ArrayList<Medic>();
		threshold=50;
	}
	
	public int getMedicSkill()
	{
		int x=0;
		for (Medic m : staff)
		{
			x+=m.getMedicSkill();
		}
		return x;
	}
	
	public double getMultiplier()
	{
		double healthRatio = (double)health/initHealth;
		return healthRatio+1;
	}
	
	public ArrayList<Crew> getInjured()
	{
		return injured;
	}
	
	public void addStaff(Medic m)
	{
		staff.add(m);
	}

	public void addInjured(Crew c)
	{
		injured.add(c);
		c.assignment=-2;
	}
	
	public void heal()
	{
		int heal=0;
		if (injured.size()>0)
		{
			heal=Math.min((int)((getMedicSkill()/injured.size())*getMultiplier()),60);
		}
		for (Crew c : injured)
		{
			c.health=Math.min(c.health+heal,100);	
		}
		for (int i=0;i<injured.size();i++)
		{
			if (injured.get(i).health>=threshold)
			{
				injured.get(i).assignment=-1;
				injured.remove(i);
				i--;
			}
		}
	}
	
	public void oxygenCrew()
	{
		for (Crew c : injured)
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
		
		for (Medic m : staff)
		{
			if (oxygenLevel<0.5)
			{
				m.lowOxygen=true;
				double d = (1-(oxygenLevel))/2;
				m.health-=d*50;
			}
			else
			{
				m.lowOxygen=false;
			}
		}
	}
}
