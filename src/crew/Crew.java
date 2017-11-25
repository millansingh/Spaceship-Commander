package crew;

import main.Tools;

public class Crew 
{
	
	public int health,assignment=-1;   //Crew assignment: -2=in medical bay, -1=not assigned, 0=reactor, 1=shield generator, 2=engines, 3=weapon system, 4=life support, 5=sensors, and 6=repairs.
	public int repairAssignment=0;    //Repair assignment: 0=reactor, 1=shield generator, 2=engines, 3=weapon system, 4=life support, 5=sensors, 6=weapons.
	private int threshold, maxHealth,rankNum;
	protected int engineerSkill,repairSkill,medicSkill;
	private double morale;
	protected String name,role,rank;
	public boolean isOnFire=false,isDead=false,lowOxygen=false,systemDamage=false;
	public boolean gender; //true=male, false=female.
	
	protected String[] maleFirstNames={"Jackev","Jose","Joshua","Jase","Jace","John","Jonny","Roldy","Jimmy","Jerry","Jaime","Victor","Henry","Amin","Rege","Danny","Sam","Kenny",
			"Ward","Jeff","Keith","Reece","Randy","Enes","Eran","Ken","Reggie","Paul","Don","Russell","Mitch","Lane","Mason","Miles","Jack","Perry","Terry","Parker","Smith",
			"Cole","Hugh","Ryan","Miguel","Matthew","Naveen","Alex","Alexander","Richard","Rick","Morgan","Michael"};
			
	protected String[] femaleFirstNames={"Sara","Nicky","Dora","Jennie","Jenny","Jen","Cynthia","Patra","Pat","Jane","Janie","Thea","Dena","Dina","Jude","Juda","Phelia","Risa",
			"Rita","Rosie","Rose","Joyce","Jess","Jessica","Jessie","Jana","Judy","Arol","Brene","Lizzy","Liz","Lisa","Kathy","Katie","Kat","Kathra","Brianna","Brie","Brienna",
			"Bonny","Debbie","Terrie","Mira","Nadim","Alex","Alexandra","Morgan","Alicia","Elisha","Eliza","Nadia"};
	
	protected String[] lastNames={"Bennard","Miray","Harray","Thite","Marte","Lexand","Messi","Carte","Sturnard","Andez","Hezal","Rezal","Teray","Riffost","Frost","Sonett","Rigan",
			"Cloray","Parker","Ganes","Smith","Jackson","Johnson","Moore","Riffin","Scoley","Parkins","Perkins","Gerson","Grivis","Morra","Homir","Hite","Mira","Monson","Jones",
			"Copeland","Cape","Cooper","Lipson","Perry","Mason","Ward","Walker","Cole","Terson","Michaels","Matthews","Richards","Lipton","Castle","Beckett","Morgan","Grimes"};
	
	//For all ship personnel, not for soldiers or the Captain
	protected String[] shipRanks={"Recruit","Petty Officer","Petty Officer First Class","Chief Petty Officer","Sergeant","Staff Sergeant","Sergeant First Class",
			"Master Sergeant","Chief Master Sergeant","Warrant Officer","Chief Warrant Officer","Lieutenant","Lieutenant Commander","Commander"};
	
	
	public Crew(int h, double m)
	{
		health=h;
		maxHealth=h;
		morale=m;
		threshold=h/2;
		engineerSkill=0;
		createName(Tools.chance(0.5));
	}
	
	public Crew(int h)
	{
		health=h;
		maxHealth=h;
		morale=1;
		threshold=h/2;
		engineerSkill=0;
		createName(Tools.chance(0.5));
	}
	
	protected void createName(boolean g)
	{
		int i=0;
		int j=(int)(Math.random()*lastNames.length);
		String s1;
		String s2=lastNames[j];
		
		if (g)
		{
			do
			{
				i = (int)(Math.random()*maleFirstNames.length);
				s1=maleFirstNames[i];
			} while (maleFirstNames[i].equals(s2));
			gender=true;
		}
		else
		{
			do
			{
				i = (int)(Math.random()*femaleFirstNames.length);
				s1=femaleFirstNames[i];
			} while (femaleFirstNames[i].equals(s2));
			gender=false;
		}
		
		name=s1 + " " + s2;
	}
	
	protected void setRank(int i)
	{
		rank = shipRanks[i];
		rankNum=i;
	}
	
	public int getRankNum()
	{
		return rankNum;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getMorale()
	{
		return morale;
	}
	
	public int getMaxHealth()
	{
		return maxHealth;
	}
	
	public int getEngineerSkill()
	{
		return engineerSkill;
	}
	
	public int getRepairSkill()
	{
		return repairSkill;
	}
	
	public int getMedicSkill()
	{
		return medicSkill;
	}
	
	public boolean isRepairing()
	{
		if (assignment==4)
		{
			return true;
		}
		return false;
	}
	
	private String getPartName(int i)
	{
		if (i==0)
		{
			return "Reactor";
		}
		else if (i==1)
		{
			return "Shields";
		}
		else if (i==2)
		{
			return "Engine";
		}
		else if (i==3)
		{
			return "Weapon System";
		}
		else if (i==4)
		{
			return "Life Support";
		}
		else if (i==5)
		{
			return "Sensors";
		}
		else if (i==6)
		{
			return "Repairing " + getPartName(repairAssignment);
		}
		return "Unassigned";
	}
	
	private String getDeathMethod()
	{
		if (isOnFire && !lowOxygen)
		{
			if (systemDamage)
			{
				return Tools.chance("Burned Alive.", regularDeath(), 0.5);
			}
			else
			{
				return "Burned Alive.";
			}
		}
		else if (lowOxygen && !isOnFire)
		{
			if (systemDamage)
			{
				return Tools.chance("Asphyxiation.", regularDeath(), 0.5);
			}
			else
			{
				return "Asphyxiation.";
			}
		}
		else if (lowOxygen && isOnFire)
		{
			if (systemDamage)
			{
				return Tools.chance("Burned Alive.", "Asphyxiation.", regularDeath(), 0.25, 0.5);
			}
			else
			{
				return Tools.chance("Burned Alive.", "Asphyxiation.", 0.5);
			}
		}
		else
		{
			return regularDeath();
		}
	}
	
	private String regularDeath()
	{
		int i = Tools.chance(0.2, 0.4, 0.6, 0.8);
		
		if (i==0)
		{
			return "Head Trauma.";
		}
		else if (i==1)
		{
			return "Shockwave.";
		}
		else if (i==2)
		{
			return "Explosion.";
		}
		else if (i==3)
		{
			return "Shrapnel.";
		}
		else if(i==4)
		{
			return "Blood Loss.";
		}
		return null;
	}
	
	public String toString()
	{
		StringBuffer s = new StringBuffer(health + " health, " + getPartName(assignment) + ": " + rank + " " + name + ", " + role);
		if (health<=0)
		{
			s.append(" (deceased)");
		}
		return s.toString();
	}
	
	public String toStringDead()
	{
		return getPartName(assignment) + ": " + rank + " " + name + ", " + role + " - " + getDeathMethod();
	}
}