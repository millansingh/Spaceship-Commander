package systems;

public class LifeSupport extends Part 
{

	private int energy,energyMax,energyNeeded;
	
	public LifeSupport(int h, int e, int eN, int w, int s) 
	{
		super(h, w, s, 0, false);
		energyNeeded=eN;
		energyMax=e;
		energy=0;
		AISkill=(int)(s*0.5);
		name="Life Support";
	}

	public int getEnergy()
	{
		return energy;
	}
	
	public int getEnergyMax()
	{
		return Math.max((int)(energyMax*((double)health/initHealth)*getCrewEfficiency()),0);
	}
	
	public int getEnergyNeeded()
	{
		return energyNeeded;
	}
	
	public double getMaxRefill()
	{
		return (((0.4*energyMax)/energyNeeded))-0.4;
	}
	
	public double getRefill()
	{
		return (((0.4*energy)/energyNeeded))-0.4;
	}
	
	public void setEnergy(int i)
	{
		energy=i;
	}
}
