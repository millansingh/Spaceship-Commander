package Main;
import java.util.ArrayList;

import Parts.*;
import WeaponSets.WeaponSet;
import Weaponry.Damage;
import Weaponry.Gun;
import Crew.*;
import Misc.*;

public class Ship 
{
	//armor is health, scrap is used to repair systems, but you can't repair armor.
	private int armor, initArmor, hits, maxExtinguishers, scrap;
	public PowerPlant Power;
	public Weapons Weapon;
	public Shields Shield;
	public Engine Engine;
	public LifeSupport LifeSupport;
	public Medbay MedBay;
	public Sensors Sensors;
	public Crew[] crew;
	public Medic[] medics;
	public boolean dShield=false, dPower=false;
	public boolean rPower=false, rShield=false, rEngine=false, rWeapon=false, rLifeSupport=false;
	public String name, fancyName;
	public final int initCrewLength,weight,initMedicsLength;
	public double hullOxygen=1;
	
	public Ship(String n, String fn, int a, int we, PowerPlant p, Weapons w, Shields s, Engine e, LifeSupport l, Medbay me, Sensors se, int c, int med, int m)
	{
		//A ship comprises a PowerPlant, Weapons System, Shield Generator, Engine, Life Support, Medical Bay, Sensors, and Crew. These systems may also contain sub-systems.
		//The heart of the game's mechanics come from the allocation of energy and crew.
		
		name=n;
		fancyName=fn;
		armor=a;
		weight=we;
		maxExtinguishers=m;
		initArmor=a;
		Power=p;
		Weapon=w;
		Shield=s;
		Engine=e;
		LifeSupport=l;
		MedBay=me;
		Sensors=se;
		LifeSupport.setEnergy(Math.min(LifeSupport.getEnergyNeeded(), getMaxLifeSupport()));
		initCrewLength=c;
		crew=new Crew[c];
		int j=(int)(c*0.75);
		for (int i=0;i<crew.length;i++)
		{
			if (i<j)
			{
				crew[i]=new Engineer(100,4+(int)(Math.random()*5));
			}
			else
			{
				crew[i]=new Engineer(100,9+(int)(Math.random()*4));
			}
		}
		initMedicsLength=med;
		medics=new Medic[med];
		int x=(int)(med*0.75);
		for (int i=0;i<medics.length;i++)
		{
			if (i<x)
			{
				medics[i]=new Medic(100,1+(int)(Math.random()*6));
			}
			else
			{
				medics[i]=new Medic(100,7+(int)(Math.random()*3));
			}
		}
		for (Medic medic : medics)
		{
			MedBay.addStaff(medic);
		}
	}
	
	//****************************************************************************
	public int getArmor()
	{
		return armor;
	}
	
	public int getInitArmor()
	{
		return initArmor;
	}
	
	public void setArmor(int a)
	{
		armor=Math.min(Math.max(a, 0),initArmor);
	}
	
	public int getHits()
	{
		return hits;
	}
	
	public int getScrap()
	{
		return scrap;
	}
	
	public void setScrap(int s)
	{
		scrap=s;
	}
	
	public double getEvade()
	{
		return (double)Engine.getThrust()/getWeight()/50;
	}
	
	public double getMaxEvade()
	{
		return (double)Engine.getMaxThrust()/getWeight()/50;
	}
	
	public void addEnergy()
	{
		Power.setEnergyStored(Math.min(Power.getEnergyStored()+Power.getEnergyProduced(), Power.getEnergyStoredMax()));
	}
	
	public int getNumGunsMax(int x)
	{
		if (Weapon.Weapons[x].gunArray.length>0)
		{
			if (Weapon.Weapons[x].gunArray[0].energyCost>0)
			{
				return Math.min((getAvailableEnergy()+Weapon.getEnergyUsed(x))/Weapon.Weapons[x].gunArray[0].energyCost,Weapon.getGunCount(x));
			}
			else
			{
				return Weapon.getGunCount(x);
			}
		}
		else
		{
			return 0;
		}
	}
	
	public int getEnergyUsed()
	{
		return Weapon.getEnergyUsed() + Shield.getEnergy() + Engine.getEnergy() + LifeSupport.getEnergy() + 
				Power.extinguishEnergy + Shield.extinguishEnergy + Engine.extinguishEnergy + Weapon.extinguishEnergy + LifeSupport.extinguishEnergy;
	}
	
	public int getAvailableEnergy()
	{
		return Power.getEnergyStored()-getEnergyUsed();
	}
	
	public int getMaxShields()
	{
		return Math.min(getAvailableEnergy()+Shield.getEnergy(), Shield.getEnergyMax());
	}
	
	public int getMaxEngines()
	{
		return Math.min(getAvailableEnergy()+Engine.getEnergy(), Engine.getEnergyMax());
	}
	
	public int getMaxLifeSupport()
	{
		return Math.min(getAvailableEnergy()+LifeSupport.getEnergy(), LifeSupport.getEnergyMax());
	}
	
	public int getMaxSensors()
	{
		return Math.min(getAvailableEnergy()+Sensors.getEnergy(), Sensors.getEnergyMax());
	}
	
	public int getMaxExtinguishers(Part p)
	{
		return Math.min(getAvailableEnergy()+p.extinguishEnergy, maxExtinguishers);
	}
	
	public Part getPartNum(int i)
	{
		if (i==0)
		{
			return Power;
		}
		else if (i==1)
		{
			return Shield;
		}
		else if (i==2)
		{
			return Engine;
		}
		else if (i==3)
		{
			return Weapon;
		}
		else if (i==4)
		{
			return LifeSupport;
		}
		else if (i==5)
		{
			return Sensors;
		}
		else
		{
			return null;
		}
	}
	
	public int getWeight()
	{
		return weight+Power.weight+Shield.weight+Weapon.getWeight()+Engine.weight+LifeSupport.weight;
	}
	
	//****************************************************************************
	
	public Damage[] calcDamage(int g, double e)
	{
		//This method created an array of Damage objects which hold values for different damage types, mundane, fire, splash, and eventually other types.
		//That array gets fed to the other ship for damage calculation. The array length is the number of guns of the other ship, plus the hull, engine, life support, reactor, shields, and weapons system.
		Damage[] damage = new Damage[g+6];
		double evade=e/100;
		
		for (int i=0; i<damage.length; i++)
		{
			damage[i]=new Damage();
		}
		
		//Cycle through weapon-sets and then weapons within them.
		for (WeaponSet weap : Weapon.Weapons)
		{
			for (Gun gun : weap.gunArray)
			{
				if (gun.isFiring)
				{
					//If the gun is firing, calculate accuracy, maximum 95%. "i" holds index for which system the gun hits.
					int i = -1;
					boolean p = false;
					if (Tools.chance(Math.min((gun.getAccuracy()-evade)*Weapon.getAccuracyMod(),0.95)))
					{
						i=calcAim(weap);
						p=Tools.chance(gun.myProjectile.shieldPiercing);
					}
					//In the following calculations, we add damage, splash, and fire damage, and then add those things to the damage array.
					//If the projectile hits the guns
					if (i==6)
					{
						int j = (int)(Math.random()*g)+6;
						damage[j].add(gun.getDamage(), 0, p);
						
						if (gun.myProjectile.fireChance>0)
						{
							boolean f = Tools.chance(gun.myProjectile.fireChance);
							if(f)
							{
								damage[j].add(gun.getFireDamage(), 1, p);
							}
						}
						if (gun.myProjectile.splash)
						{
							for (int d=0; d<gun.myProjectile.numSplash; d++)
							{
								if (Tools.chance(0.15))
								{
									int c = Tools.chance(0.16,0.32,0.48,0.68,0.84);
									damage[c].add(gun.getSplashDamage(), 2, p);
								}
								else
								{
									int x = (int)(Math.random()*g)+6;
									damage[x].add(gun.getSplashDamage(), 2, p);
								}
							}
						}
					}
					//If the projectile hits anything besides hull or guns
					else if (i>=0 && i<3 || i==4 || i==5)
					{
						damage[i].add(gun.getDamage(), 0, p);
						
						if (gun.myProjectile.fireChance>0)
						{
							if (Tools.chance(gun.myProjectile.fireChance))
							{
								damage[i].add(gun.getFireDamage(), 1, p);
							}
						}
						if (gun.myProjectile.splash)
						{
							for (int d=0; d<gun.myProjectile.numSplash; d++)
							{
								if (Tools.chance(0.3))
								{
									int c = Tools.chance(0.16,0.32,0.48,0.68,0.84);
									damage[c].add(gun.getSplashDamage(), 2, p);
								}
								else
								{
									int x = (int)(Math.random()*g)+6;
									damage[x].add(gun.getSplashDamage(), 2, p);
								}
							}
						}
					}
					//If the projectile hits the hull
					else if (i==3)
					{
						damage[i].add(gun.getDamage(), 0, p);
						
						if (gun.myProjectile.splash)
						{
							for (int d=0; d<gun.myProjectile.numSplash; d++)
							{
								if (Tools.chance(0.4))
								{
									int c = Tools.chance(0.16,0.32,0.48,0.68,0.84);
									damage[c].add(gun.getSplashDamage(), 2, p);
								}
								else
								{
									int x = (int)(Math.random()*g)+6;
									damage[x].add(gun.getSplashDamage(), 2, p);
								}
							}
						}
					}
				}
			}
		}
		
		Weapon.updateAmmo();
		return damage;
	}
	
	//Calculate which part to hit if a gun hits something.
	public int calcAim(WeaponSet w)
	{
		int i = -1;
		
		if (w.aim==-1) //Non-specified targeting
		{
			i=Tools.chance(0.10, 0.20, 0.30, 0.60, 0.70, 0.80);
			//0-0.1 is Powerplant, 0.1-0.2 is Shields, 0.2-0.3 is engine, 0.3-0.6 is hull, 0.6-0.7 is life support, 0.7-0.8 is weapon system, and 0.8-1 is guns.
		}
		
		if (w.aim==0) //Powerplant targeting
		{
			i=Tools.chance(0.25, 0.30, 0.35, 0.75, 0.80, 0.85); 
			//0-0.15 is Powerplant, 0.15-0.2 is shields, 0.2-0.3 is engine, 0.3-0.75 is hull, 0.75-0.8 is life support, 0.8-0.85 is weapon system, and 0.85-1 is guns.
		}
		
		if (w.aim==1) //Shields targeting
		{
			i=Tools.chance(0.05, 0.30, 0.35, 0.75, 0.80, 0.85); 
			//0-0.05 is Powerplant, 0.05-0.3 is shields, 0.3-0.35 is engine, 0.35-0.75 is hull, 0.75-0.8 is life support, 0.8-0.85 is weapon system, and 0.85-1 is guns.
		}
		
		if (w.aim==2) //engine targeting
		{
			i=Tools.chance(0.05, 0.10, 0.35, 0.75, 0.80, 0.85); 
			//0-0.05 is Powerplant, 0.05-0.1 is shields, 0.1-0.35 is engine, 0.35-0.75 is hull, 0.75-0.8 is life support, 0.8-0.85 is weapon system, and 0.85-1 is guns.
		}
		
		if (w.aim==3) //Hull targeting
		{
			i=Tools.chance(0.06, 0.12, 0.18, 0.78, 0.84, 0.90); 
			//0-0.06 is Powerplant, 0.06-0.12 is shields, 0.12-0.18 is engine, 0.18-0.78 is hull, 0.78-0.84 is life support, 0.84-0.9 is weapon system, and 0.9-1 is guns.
		}
		
		if (w.aim==4) //Life Support targeting
		{
			i=Tools.chance(0.05, 0.10, 0.15, 0.55, 0.80, 0.85); 
			//0-0.05 is Powerplant, 0.05-0.1 is shields, 0.1-0.15 is engine, 0.15-0.55 is hull, 0.55-0.8 is life support, 0.8-0.85 is weapon system, and 0.85-1 is guns.
		}
		
		if (w.aim==5) //Weapon System targeting
		{
			i=Tools.chance(0.05, 0.10, 0.15, 0.55, 0.60, 0.85); 
			//0-0.05 is Powerplant, 0.05-0.1 is shields, 0.1-0.15 is engine, 0.15-0.55 is hull, 0.55-0.6 is life support, 0.6-0.85 is weapon system, and 0.85-1 is guns.
		}
		
		if (w.aim==6) //Weapon targeting
		{
			i=Tools.chance(0.08, 0.16, 0.24, 0.54, 0.62, 0.70); 
			//0-0.08 is Powerplant, 0.08-0.16 is shields, 0.16-0.24 is engine, 0.24-0.54 is hull, 0.54-0.62 is life support, 0.62-0.7 is weapon system, and 0.7-1 is guns.
		}
		hits++;
		return i;
	}
	
	public Damage[] takeDamage(Damage[] damage)
	{
		//Apply calculated damage using the array from the other ship's calcDamage method. Most of the horsepower comes from the tageDamageHelper method.
		Damage[] d = damage;
		Power.setEnergyStored(Power.getEnergyStored()-getEnergyUsed());
		int total=0;
		d=deflect(d);
		
		for(int i=0;i<d.length;i++)
		{
			if (i==0)
			{
				total = takeDamageHelper(d, Power, total, i);
			}
			else if (i==1)
			{
				total = takeDamageHelper(d, Shield, total, i);
			}
			else if (i==2)
			{
				total = takeDamageHelper(d, Engine, total, i);
			}
			else if (i==3)
			{
				total+=d[i].getDamage(0);
				if (d[i].getDamage(2)>0)
				{
					total+=d[i].getDamage(2);
				}
			}
			else if (i==4)
			{
				total = takeDamageHelper(d, LifeSupport, total, i);
			}
			else if (i==5)
			{
				total = takeDamageHelper(d, Weapon, total, i);
			}
			else if (i>=6)
			{
				int[] w = new int[Weapon.Weapons.length];
				w[0]=Weapon.Weapons[0].initGuns;
				for (int x=1;x<w.length;x++)
				{
					w[x]=Weapon.Weapons[x].initGuns+w[x-1];
				}
				
				for (int y=0;y<w.length;y++)
				{
					if (i<w[y]+6 && y==0)
					{
						total+=Math.max(d[i].getDamage(0)-Weapon.Weapons[y].gunArray[i-6].health, 0);
						Weapon.Weapons[y].gunArray[i-6].health-=d[i].getDamage(0);
						
						if (d[i].getDamage(2)>0)
						{
							Weapon.Weapons[y].gunArray[i-6].health-=d[i].getDamage(2);
						}
					}
					
					else if (i<w[y]+6 && y>0 && i>w[y-1]+6)
					{
						total+=Math.max(d[i].getDamage(0)-Weapon.Weapons[y].gunArray[i-6-w[y-1]].health, 0);
						Weapon.Weapons[y].gunArray[i-6-w[y-1]].health-=d[i].getDamage(0);
						
						if (d[i].getDamage(2)>0)
						{
							Weapon.Weapons[y].gunArray[i-6-w[y-1]].health-=d[i].getDamage(2);
						}
					}
				}
			}
		}
		
		armor-=total;
		
		overload();
		oxygenStep();
		
		Shield.resetStep();
		Power.resetStep();
		Engine.resetStep();
		Weapon.resetStep();
		LifeSupport.resetStep();
		MedBay.resetStep();
		
		Shield.fireCheck();
		Power.fireCheck();
		Engine.fireCheck();
		Weapon.fireCheck();
		LifeSupport.fireCheck();
		MedBay.fireCheck();
		
		return d;
	}
	
	public int takeDamageHelper(Damage[] d, Part p, int total, int i)
	{
		//Hull damage and system damage
		total+=Math.max(d[i].getDamage(0)-p.health, 0);
		p.health=Math.max(p.health-d[i].getDamage(0),0);
		
		//Fire damage
		if (d[i].getDamage(1)>0)
		{
			p.fireDamage+=d[i].getDamage(1);
			
			if (!p.isOnFire)
			{
				p.isOnFire=true;
			}
		}
		
		//Splash damage
		if (d[i].getDamage(2)>0)
		{
			p.health=Math.max(p.health-d[i].getDamage(2),0);
		}
		
		//Apply crew damage based on mundane+splash damage
		if (p.getCrewNum()>0)
		{
			for (Crew c : p.getCrew())
			{
				int crewDamage=(int)((d[i].getDamage(0)+d[i].getDamage(2))*Math.random()*0.2);
				c.health-=crewDamage;
				if (crewDamage>0)
				{
					c.systemDamage=true;
				}
			}
		}
		if (p.getRepairCrewNum()>0)
		{
			for (Crew c : p.getRepairCrew())
			{
				int crewDamage=(int)((d[i].getDamage(0)+d[i].getDamage(2))*Math.random()*0.2);
				c.health-=crewDamage;
				if (crewDamage>0)
				{
					c.systemDamage=true;
				}
			}
		}
		
		return total;
	}

	public Damage[] deflect(Damage[] damage)
	{
		//This method first deflects damage applied to targeted area, and then spends all remaining energy (if there is any) on other systems, dividing the energy by ratios of the damage those systems recieved.
		
		Damage [] d = damage;
		if (Shield.health>0)
		{
			if (Shield.getConcentration(0)>-1)
				{d=deflectHelper(d,0);}
			if (Shield.getConcentration(1)>-1)
				{d=deflectHelper(d,1);}
			if (Shield.getConcentration(2)>-1)
				{d=deflectHelper(d,2);}
			
			if (Shield.getEnergy()>0)
			{
				int weapdamage=0;
				for (int i=6; i<d.length; i++)
				{
					weapdamage+=d[i].getDamage(0);
				}
				
				int total = weapdamage+d[0].getDamage(0)+d[1].getDamage(0)+d[2].getDamage(0)+d[3].getDamage(0)+d[4].getDamage(0)+d[5].getDamage(0);
				
				double r1 = (double)d[0].getDamage(0)/total;
				double r2 = (double)d[1].getDamage(0)/total;
				double r3 = (double)d[2].getDamage(0)/total;
				double r4 = (double)d[3].getDamage(0)/total;
				double r5 = (double)d[4].getDamage(0)/total;
				double r6 = (double)d[5].getDamage(0)/total;
				double r7 = (double)weapdamage/total;
				
				int d1 = (int)(r1*Shield.getEnergy());
				int d2 = (int)(r2*Shield.getEnergy());
				int d3 = (int)(r3*Shield.getEnergy());
				int d4 = (int)(r4*Shield.getEnergy());
				int d5 = (int)(r5*Shield.getEnergy());
				int d6 = (int)(r6*Shield.getEnergy());
				int d7 = (int)(r7*Shield.getEnergy());
				
				d[0].deflect(d1);
				d[1].deflect(d2);
				d[2].deflect(d3);
				d[3].deflect(d4);
				d[4].deflect(d5);
				d[5].deflect(d6);
				
				int dweaps=0;
				
				for (int k=6;k<d.length;k++)
				{
					if (d[k].getDamage(0)>0)
					{
						dweaps++;
					}					
				}
				
				if(dweaps>0)
				{
					int dperweap=d7/dweaps;
					
					for (int j=6;j<d.length;j++)
					{
						if (d[j].getDamage(0)>0)
						{
							d[j].deflect(dperweap);
						}					
					}
				}
				
				Shield.setEnergy(0);
			}
		}
		return d;
	}
	
	private Damage[] deflectHelper(Damage[] dam, int index)
	{
		Damage[] d = dam;
		if (Shield.getConcentration(index) < 6)
		{
			int e = d[Shield.getConcentration(index)].deflect(Shield.getEnergy());
			Shield.setEnergy(e);
		}
		else if (Shield.getConcentration(index) == 6)
		{
			for (int i=6; i<d.length; i++)
			{
				if (Shield.getEnergy()>0)
				{		
					int e = d[i].deflect(Shield.getEnergy());
					Shield.setEnergy(e);
				}
			}
		}
		
		return d;
	}
	
	public void overload()
	{
		if (Shield.isOverloading)
		{
			Shield.health*=Math.min((0.8+(0.1*(((double)Shield.getEngineerSkill()+Shield.AISkill-Shield.skillNeeded)/(Shield.skillNeededOverload-Shield.skillNeeded)))),1);
		}
		
		if (Power.isOverloading)
		{
			Power.health*=Math.min((0.8+(0.1*(((double)Power.getEngineerSkill()+Power.AISkill-Power.skillNeeded)/(Power.skillNeededOverload-Power.skillNeeded)))),1);
		}
		
		if (Shield.health<=0 && Shield.isOverloading)
		{
			dShield=true;
		}
		
		if (Power.health<=0 && Power.isOverloading)
		{
			dPower=true;
		}
	}
	
	public void oxygenStep()
	{
		Shield.oxygenLevel=Math.max(Math.min(Shield.oxygenLevel+LifeSupport.getRefill(),1),0);
		Power.oxygenLevel=Math.max(Math.min(Power.oxygenLevel+LifeSupport.getRefill(),1),0);
		Engine.oxygenLevel=Math.max(Math.min(Engine.oxygenLevel+LifeSupport.getRefill(),1),0);
		Weapon.oxygenLevel=Math.max(Math.min(Weapon.oxygenLevel+LifeSupport.getRefill(),1),0);
		LifeSupport.oxygenLevel=Math.max(Math.min(LifeSupport.oxygenLevel+LifeSupport.getRefill(),1),0);
		MedBay.oxygenLevel=Math.max(Math.min(MedBay.oxygenLevel+LifeSupport.getRefill(),1),0);
		
		hullOxygen=Math.max(Math.min(hullOxygen+LifeSupport.getRefill(),1),0);
		ArrayList<Crew> cr = getFreeCrew();
		for (Crew c : cr)
		{
			if (hullOxygen<0.5)
			{
				c.lowOxygen=true;
				double d = (1-hullOxygen)/2;
				c.health-=d*50;
			}
			else
			{
				c.lowOxygen=false;
			}
		}
		
		Shield.oxygenCrew();
		Power.oxygenCrew();
		Engine.oxygenCrew();
		Weapon.oxygenCrew();
		LifeSupport.oxygenCrew();
		MedBay.oxygenCrew();
	}
	
	public void deactivateExtinguishers()
	{
		Power.extinguishEnergy=0;
		Power.extinguishing=false;
		Shield.extinguishEnergy=0;
		Shield.extinguishing=false;
		Engine.extinguishEnergy=0;
		Engine.extinguishing=false;
		Weapon.extinguishEnergy=0;
		Weapon.extinguishing=false;
		LifeSupport.extinguishEnergy=0;
		LifeSupport.extinguishing=false;
	}
	
	public int getExtinguisherEnergy()
	{
		int i = 0;
		
		i+=Weapon.extinguishEnergy;
		i+=Shield.extinguishEnergy;
		i+=Power.extinguishEnergy;
		i+=Engine.extinguishEnergy;
		i+=LifeSupport.extinguishEnergy;
		
		return i;
	}

	public int ramStep(double e)
	{
		if (Engine.isRamming && armor>0)
		{
			if (Engine.ramTimer>0)
			{
				Engine.ramTimer--;
			}
			
			if (Engine.ramTimer==0)
			{
				boolean i = Tools.chance(0.5+getEvade()-e);
				if (i)
				{
					return 1;
				}
				else
				{
					Engine.initializeReset();
					return 0;
				}
			}
		}
		
		return -1;
	}
	
	public void ramEnergy()
	{
		if (getAvailableEnergy()>Engine.getEnergyMax())
		{				
			Engine.setEnergy(Engine.getEnergyMax());
		}
		else
		{
			deactivateExtinguishers();
			if (getAvailableEnergy()>Engine.getEnergyMax())
			{
				Engine.setEnergy(Engine.getEnergyMax());
			}
			else
			{
				Engine.setEnergy(getAvailableEnergy());
			}
		}
	}
	
	public void repairStep()
	{
		Power.repairAmount = repairHelper(Power);
		repairHelper(Shield);
		repairHelper(Weapon);
		repairHelper(Engine);
		repairHelper(LifeSupport);
		
		if (Power.health==Power.initHealth && Power.getRepairCrewNum()>0)
		{
			freeRepairCrew(0);
			rPower=true;
		}
		
		if (Shield.health==Shield.initHealth && Shield.getRepairCrewNum()>0)
		{
			freeRepairCrew(1);
			rShield=true;
		}
		
		if (Weapon.health==Weapon.initHealth && Weapon.getRepairCrewNum()>0)
		{
			freeRepairCrew(3);
			rWeapon=true;
		}
		
		if (Engine.health==Engine.initHealth && Engine.getRepairCrewNum()>0)
		{
			freeRepairCrew(2);
			rEngine=true;
		}
		
		if (LifeSupport.health==LifeSupport.initHealth && LifeSupport.getRepairCrewNum()>0)
		{
			freeRepairCrew(4);
			rLifeSupport=true;
		}
		
		if (scrap==0)
		{
			freeRepairCrew(0);
			freeRepairCrew(1);
			freeRepairCrew(2);
			freeRepairCrew(3);
			freeRepairCrew(4);
			Weapon.clearWeapRepairCrew();
		}
		
		Weapon.repairStep(this);
		if (!Weapon.canRepairWeap(scrap))
		{
			Weapon.clearWeapRepairCrew();
		}
	}
	
	private int repairHelper(Part p)
	{
		int repair = 0;
		
		if (p.initHealth-p.health<=p.getRepairSkill())
		{
			repair = p.initHealth-p.health;
		}
		else
		{
			repair = p.getRepairSkill();
		}
		
		repair = Math.min(scrap, repair);
		scrap-=repair;
		p.health+=repair;
		return repair;
	}
	
	public ArrayList<Crew> crewCheck() 
	{
		ArrayList <Crew> c = new ArrayList<Crew>();
		for (int i=0;i<crew.length;i++)
		{
			if (crew[i].health<=0 && !crew[i].isDead)
			{
				crew[i].isDead=true;
				c.add(crew[i]);
			}
		}
		for (int j=0;j<medics.length;j++)
		{
			if (medics[j].health<=0 && !medics[j].isDead)
			{
				medics[j].isDead=true;
				c.add(medics[j]);
			}
		}
		Weapon.crewCheck();
		Shield.crewCheck();
		Power.crewCheck();
		Engine.crewCheck();
		LifeSupport.crewCheck();
		MedBay.crewCheck();
		
		Shield.partHealthCrewCheck();
		Power.partHealthCrewCheck();
		Engine.partHealthCrewCheck();
		Weapon.partHealthCrewCheck();
		LifeSupport.partHealthCrewCheck();
		MedBay.partHealthCrewCheck();
		
		return Tools.sortCrew(c);
	}
	
	public void allocateCrew(int i, int tot)
	{
		int a=0;
		Part p = getPartNum(i);
		
		for (Crew c : crew)
		{
			if (c.assignment==-1 && a<tot)
			{
				a++;
				c.assignment=i;
				p.getCrew().add(c);
			}
			else if (a>=tot)
			{
				break;
			}
		}
	}
	
	//Allocate optimal crew automatically to Power, Shield, Engine, and Weapons assuming there is enough to do so.
	public void manTheShip()
	{
		allocateCrew(0, Math.min(Power.manPart(), getAvailableCrew()));
		allocateCrew(3, Math.min(Weapon.manPart(), getAvailableCrew()));
		allocateCrew(1, Math.min(Shield.manPart(), getAvailableCrew()));
		allocateCrew(2, Math.min(Engine.manPart(), getAvailableCrew()));
	}
	
	public void freeCrew(int i, int tot)
	{
		int a=0;
		Part p = getPartNum(i);
		
		for (int c=0;c<p.getCrew().size();c++)
		{
			if (a<tot)
			{
				a++;
				p.getCrew().get(c).isOnFire=false;
				p.getCrew().get(c).assignment=-1;
				p.getCrew().remove(c);
				c--;
			}
			else
			{
				break;
			}
		}
	}
	
	public void freeRepairCrew()
	{
		for (Crew c : crew)
		{
			if (c.assignment==6)
			{
				c.assignment=-1;
				c.isOnFire=false;
			}
		}
		Power.clearRepairCrew();
		Shield.clearRepairCrew();
		Engine.clearRepairCrew();
		Weapon.clearRepairCrew();
		LifeSupport.clearRepairCrew();
		
		Weapon.clearWeapRepairCrew();
	}
	
	public void freeRepairCrew (int i)
	{
		Part p = getPartNum(i);
		
		for (Crew c : crew)
		{
			if (c.repairAssignment==i && c.assignment==6)
			{
				c.assignment=-1;
				c.isOnFire=false;
				p.getRepairCrew().remove(c);
			}
		}
	}
	
	public void freeRepairCrewHalf()
	{
		Power.clearRepairCrewHalf();
		Shield.clearRepairCrewHalf();
		Engine.clearRepairCrewHalf();
		Weapon.clearRepairCrewHalf();
		LifeSupport.clearRepairCrewHalf();
		Weapon.clearWeapRepairCrewHalf();
	}
	
	public void clearCrew()
	{
		for (int i=0;i<crew.length;i++)
		{
			crew[i].assignment=-1;
			crew[i].isOnFire=false;
		}
		Weapon.clearCrew();
		Shield.clearCrew();
		Power.clearCrew();
		Engine.clearCrew();
		LifeSupport.clearCrew();
	}
	
	public void allocateRepairCrew(int i, int tot)
	{
		int a=0;
		Part p = getPartNum(i);
		
		for (Crew c : crew)
		{
			if (c.assignment==-1 && a<tot)
			{
				a++;
				c.assignment=6;
				c.repairAssignment=i;
				p.getRepairCrew().add(c);
			}
			else if (a>=tot)
			{
				break;
			}
		}
	}
	
	public void allocateRepairCrewWeap(int tot)
	{
		int a=0;
		
		for (Crew c : crew)
		{
			if (c.assignment==-1 && a<tot)
			{
				a++;
				c.assignment=5;
				c.repairAssignment=5;
				Weapon.getWeapRepairCrew().add(c);
			}
			else if (a>=tot)
			{
				break;
			}
		}
	}
	
	public boolean[] checkCrewOverload()
	{
		boolean[] s = {false,false,false};
		boolean check=false;
		
		if (Power.isOverloading && Power.getCrewNum()<Power.getEngineersNeeded() && !Power.isDestroyed)
		{
			boolean b = moveCrew(Power.getEngineersNeeded()-Power.getCrewNum(),0);
			s[0]=b;
			check=true;
			
			if (!b)
			{
				overloadRamCrewShort(0);
			}
		}
		if (Shield.isOverloading && Shield.getCrewNum()<Shield.getEngineersNeeded() && !Shield.isDestroyed)
		{
			boolean b = moveCrew(Shield.getEngineersNeeded()-Shield.getCrewNum(),0);
			s[1]=b;
			check=true;
			
			if (!b)
			{
				overloadRamCrewShort(1);
			}
		}
		if (Engine.isRamming && Engine.getCrewNum()<Engine.getEngineersNeededRam() && !Engine.isDestroyed)
		{
			boolean b = moveCrew(Engine.getEngineersNeededRam()-Engine.getCrewNum(),0);
			s[2]=b;
			check=true;
			
			if (!b)
			{
				overloadRamCrewShort(2);
			}
		}
		
		if (check)
		{
			return s;
		}
		return null;
	}
	
	public int overloadRamCrewShort(int i)
	{
		Part p = getPartNum(i);
		if (p instanceof PowerPlant)
		{
			int d =(int)(Power.getInitEnergyMax()*Math.random());
			armor-=d;
			Power.isDestroyed=true;
			Power.isOverloading=false;
			for ( Crew c : Power.getCrew())
			{
				c.health=0;
			}
			for ( Crew c : Power.getRepairCrew())
			{
				c.health=0;
			}
			return d;
		}
		if (p instanceof Shields)
		{
			int d =(int)(Shield.getInitEnergyMax()*Math.random());
			armor-=d;
			Shield.isDestroyed=true;
			Shield.isOverloading=false;
			for ( Crew c : Shield.getCrew())
			{
				c.health=0;
			}
			for ( Crew c : Shield.getRepairCrew())
			{
				c.health=0;
			}
			return d;
		}
		if (p instanceof Engine)
		{
			Engine.initializeReset();
		}
		return 0;
	}
	
	public boolean moveCrew(int tot, int i)
	{
		Part p = getPartNum(i);
		int a=0;
		
		for (Crew c : crew)
		{
			if (c.assignment==-1 && a<tot)
			{
				c.assignment=i;
				p.getCrew().add(c);
				a++;
			}
		}
		
		if (a<tot)
		{
			for (Crew c : crew)
			{
				if (c.assignment==5 && a<tot)
				{
					Part pc = getPartNum(c.repairAssignment);
					pc.getRepairCrew().remove(c);
					c.assignment=i;
					p.getCrew().add(c);
					a++;
				}
			}
		}
		
		if (a<tot)
		{
			for (Crew c : crew)
			{
				if (c.assignment==3 && a<tot)
				{
					Part pc = getPartNum(c.assignment);
					pc.getCrew().remove(c);
					c.assignment=i;
					p.getCrew().add(c);
					a++;
				}
			}
		}
		
		if (a<tot)
		{
			return false;
		}
		return true;
	}
	
	public int getRepairSpeed(int i)
	{
		return crew[0].getRepairSkill()*i;
	}
	
	public int getAvailableCrew()
	{
		int i=0;
		for (Crew c : crew)
		{
			if (c.assignment==-1 && !c.isDead)
			{
				i++;
			}
		}
		if (Shield.isOverloading && Shield.getCrewNum()<Shield.getEngineersNeeded())
		{
			i-=Shield.getEngineersNeeded()-Shield.getCrewNum();
		}
		if (Power.isOverloading && Power.getCrewNum()<Power.getEngineersNeeded())
		{
			i-=Power.getEngineersNeeded()-Power.getCrewNum();
		}
		if (Engine.isRamming && Engine.getCrewNum()<Engine.getEngineersNeededRam())
		{
			i-=Engine.getEngineersNeededRam()-Engine.getCrewNum();
		}
		return Math.max(i,0);
	}
	
	public ArrayList<Crew> getFreeCrew()
	{
		ArrayList<Crew> cr = new ArrayList<Crew>();
		
		for (Crew c : crew)
		{
			if (c.assignment==-1 && !c.isDead)
			{
				cr.add(c);
			}
		}
		
		return cr;
	}
	
	public int getRequiredCrew()
	{
		int i=0;
		
		if (Shield.isOverloading)
		{
			i+=Shield.getEngineersNeeded();
		}
		if (Power.isOverloading)
		{
			i+=Power.getEngineersNeeded();
		}
		if (Engine.isRamming)
		{
			i+=Engine.getEngineersNeededRam();
		}
		return i;
	}
	
	public int getAliveCrew()
	{
		int x = 0;
		for (Crew c : crew)
		{
			if (!c.isDead)
			{
				x++;
			}
		}
		return x;
	}
	
	public boolean isOverloadingOrRamming()
	{
		if (Power.isOverloading || Shield.isOverloading || Engine.isRamming)
		{
			return true;
		}
		return false;
	}
	
	public String getPartNameCrew(int i)
	{
		if (i==0)
		{
			return Power.getName();
		}
		else if (i==1)
		{
			return Shield.getName();
		}
		else if (i==2)
		{
			return Engine.getName();
		}
		else if (i==3)
		{
			return Weapon.getName();
		}
		else if (i==4)
		{
			return LifeSupport.getName();
		}
		else if (i==5)
		{
			return Sensors.getName();
		}
		else
		{
			return null;
		}
	}

	/*
	 * Medics stuff:
	 * */
	public int getAvailableMedics()
	{
		int i=0;
		for (Medic m : medics)
		{
			if (!m.isDead)
			{
				i++;
			}
		}
		return i;
	}
	
	public void endStep()
	{
		hits=0;
		addEnergy();
		Shield.setEnergy(0);
		Engine.setEnergy(0);
		//LifeSupport.setEnergy(0);
		LifeSupport.setEnergy(Math.min(Math.min(LifeSupport.getEnergyNeeded(), getMaxLifeSupport()),Power.getEnergyStored()));
		MedBay.heal();
		Sensors.setEnergy(0);
		Sensors.unlockCrew();
		Sensors.unlockEnergy();
		if (Engine.isRamming)
		{
			ramEnergy();
		}
		for (Crew c : crew)
		{
			c.systemDamage=false;
		}
	}
}