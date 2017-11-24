package systems;

public class Engine extends Part 
{
	private int energy,energyMax;
	public int RAM_TIME,ramTimer,skillNeededRam;
	private double efficiency;
	public boolean isRamming=false;
	
	public Engine(int h, int e, double ef, int r, int w, int s, int so) 
	{
		super(h,w,s,0,false);
		energy=0;
		energyMax=e;
		efficiency=ef;
		ramTimer=0;
		RAM_TIME=r;
		AISkill=s/5;
		skillNeededRam=so;
		name="Engines";
	}
	
	public void initiateRamming()
	{
		isRamming=true;
		ramTimer=RAM_TIME;
	}
	
	public void initializeReset()
	{
		super.initializeReset();
		resetTimer = RAM_TIME;
		isRamming=false;
	}
	
	public int getEnergy()
	{
		return energy;
	}
	
	public int getEnergyMax()
	{
		return Math.max((int)(energyMax*((double)health/initHealth)*getCrewEfficiency()),0);
	}
	
	public int getEnergyMaxBase()
	{
		return energyMax;
	}
	
	public void setEnergy(int i)
	{
		energy=i;
	}
	
	public double getEfficieny()
	{
		return efficiency;
	}
	
	public int getThrust()
	{
		return (int)(energy*efficiency*300);
	}
	
	public int getMaxThrust()
	{
		return (int)(efficiency*energyMax*300);
	}
	
	public int getEngineersNeededRam()
	{
		int s = skillNeededRam-AISkill;
		return (s/150)+1;
	}
	
	public int getCrewNumNeeded()
	{
		if (isRamming)
		{
			return getCrewNum()-getEngineersNeededRam();
		}
		return getCrewNum();
	}
	
	public boolean canRam()
	{
		if (!isRamming && getEngineerSkill()>=skillNeededRam-AISkill && isActive)
		{
			return true;
		}
		return false;
	}
}