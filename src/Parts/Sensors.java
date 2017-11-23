package Parts;

import java.awt.Color;

import javax.swing.JLabel;

import Main.Ship;
import Misc.Tools;

public class Sensors extends Part
{
	private int energy, energyMax;
	private boolean bIsLockedCrew, bIsLockedEnergy;
	
	public Sensors(int h, int w, int s, int ai, boolean b, int emax)
	{
		super(h, w, s, s, b);
		energy=0;
		energyMax=emax;
		AISkill=ai;
		name="Sensors";
	}
	
	public int getEnergy()
	{
		return energy;
	}
	
	public int getEnergyMax()
	{
		return (int)(energyMax*((double)health/initHealth));
	}
	
	public void setEnergy(int e)
	{
		energy=e;
	}
	
	public boolean isLockedCrew()
	{
		return bIsLockedCrew;
	}
	
	public boolean isLockedEnergy()
	{
		return bIsLockedEnergy;
	}
	
	public void lockCrew()
	{
		bIsLockedCrew=true;
	}
	
	public void lockEnergy()
	{
		bIsLockedEnergy=true;
	}
	
	public void unlockCrew()
	{
		bIsLockedCrew=false;
	}
	
	public void unlockEnergy()
	{
		bIsLockedEnergy=false;
	}
	
	public double getSensorEffectiveness()
	{
		double sens = (double)energy/energyMax;
		return sens*getCrewEfficiency();
	}

	public JLabel sensHealth(int h, int i, boolean s)
	{
		JLabel output=new JLabel("Unknown");
		
		if (!s)
		{
			return sensHealthHelper(h,i);
		}
		else
		{
			if (Tools.chance(Math.max(getSensorEffectiveness()-0.1, 0)))
			{
				return sensHealthHelper(h,i);
			}
		}
		
		return output;
	}
	
	private JLabel sensHealthHelper(int h, int i)
	{
		JLabel output = new JLabel("Unknown");
		if (h >= i*0.8)
		{
			output = new JLabel("Healthy");
			output.setForeground(Color.GREEN);
		}
		else if (h >= i*0.5)
		{
			output = new JLabel("Fair");
			output.setForeground(new Color(255,222,0));
		}
		else if (h >= i*0.2)
		{
			output = new JLabel("Weak");
			output.setForeground(new Color(255,100,0));
		}
		else if (h < i*0.2)
		{
			output = new JLabel("Dire");
			output.setForeground(Color.RED);
		}
		return output;
	}
	
}
