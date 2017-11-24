package systems;

public class Shields extends Part
{
	private int energy,energyMax,initEnergyMax;
	private int[] concentration;
	
	public Shields(int h, int e, int r, int w, int s, int so, boolean b) 
	{
		super(h,w,s,so,b);
		energyMax=e;
		initEnergyMax=e;
		energy=0;
		concentration = new int[3];
		concentration[0]=-1;
		concentration[1]=-1;
		concentration[2]=-1;
		RESET_TIME=r;
		AISkill=s/5;
		name="Shield Generator";
	}
	
	public int getEnergy()
	{
		return energy;
	}
	
	public int getInitEnergyMax()
	{
		return initEnergyMax;
	}
	
	public int getEnergyMax()
	{
		if (isActive)
		{
			int eMax=energyMax;
			if (isOverloading)
			{
				eMax*=1.5;
			}
			return Math.max((int)(eMax*((double)health/initHealth)*getCrewEfficiency()),0);
		}
		return 0;
	}
	
	public int getEnergyMaxBase()
	{
		return energyMax;
	}
	
	public void setEnergy(int e)
	{
		energy=e;
	}
	
	public int getConcentration(int i)
	{
		return concentration[i];
	}

	public void setConcentration(int c, int i) 
	{
		concentration[i] = c;
	}
	
	public void initializeReset()
	{
		super.initializeReset();
		isOverloading=false;
		energy=0;
	}
}