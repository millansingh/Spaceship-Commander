package systems;

import game.Ship;

public class PowerPlant extends Part 
{
	//EnergyStored=energy in batteries, EnergyStoredMax=max energy in batteries, EnergyProduced=produced energy/turn
	private int EnergyStored, EnergyStoredMax, EnergyProduced, initEnergyMax;
	public int repairAmount;
	
	public PowerPlant(int h, int e, int eMax, int eP, int r, int w, int s, int so, boolean b)
	{
		super(h,w,s,so,b);
		EnergyStored=e;
		EnergyStoredMax=eMax;
		initEnergyMax=eMax;
		EnergyProduced=eP;
		repairAmount=0;
		RESET_TIME=r;
		AISkill=(int)(s*0.25);
		name="Reactor";
	}
	
	//accessor methods
	public int getEnergyStored()
	{
		return EnergyStored;
	}
	
	public int getEnergyStoredMax()
	{
		return EnergyStoredMax;
	}
	
	public int getEnergyProduced()
	{
		if (isActive)
		{
			int e=EnergyProduced;
			
			if (isOverloading)
			{
				e*=1.35;
			}
			
			double temp=Math.max(((health-(repairAmount/2))*1.0)/initHealth,0.15);
			repairAmount=0;
			return Math.max((int)(temp*e*getCrewEfficiency()),0);
		}
		else if (!isActive && health>0)
		{
			return (int)(EnergyProduced*0.5*getCrewEfficiency());
		}
		return 0;
	}
	
	public int getEnergyProducedBase()
	{
		return EnergyProduced;
	}
	
	//Mutator methods
	public void setEnergyStored(int e)
	{
		EnergyStored=e;
	}
	
	public int getInitEnergyMax()
	{
		return initEnergyMax;   
	}
	
	public void resetDone()
	{
		
	}
}