package weapons;

public class Damage 
{
	public int[] damage=new int[100];
	public int[] damageType=new int[100]; //0 is mundane, 1 is fire, 2 is splash, 3 is breach
	private int tracking;
	public boolean[] isPiercing=new boolean[100];
	
	public Damage()
	{
		tracking = 0;
	}
	
	public Damage(int d, int t, boolean b)
	{
		damage[0] = d;
		damageType[0] = t;
		tracking = 1;
		isPiercing[0]=b;
	}
	
	//Adds new damage to the array
	public void add(int d, int t, boolean b)
	{
		damage[tracking]=d;
		damageType[tracking]=t;
		isPiercing[tracking]=b;
		tracking++;
	}
	
	public int getDamage(int t)
	{
		int total = 0;
		for (int i=0;i<damage.length;i++)
		{
			if (damageType[i]==t)
			{
				total+=damage[i];
			}
		}
		return total;
	}
	
	public int deflect(int d)
	{
		int r = d;
		
		if (d>=getDamage(0))
		{
			for (int i=0;i<damage.length;i++)
			{
				if (!isPiercing[i])
				{
					r-=damage[i];
					damage[i]=0;
					//System.out.println("Shields breached!!");
				}
			}
			return r;
		}
		
		else
		{
			for (int i=0;i<damage.length;i++)
			{
				if (damageType[i]==0)
				{
					if (r>=damage[i])
					{
						if (!isPiercing[i])
						{
							r-=damage[i];
							damage[i]=0;
							//System.out.println("Shields breached!!");
						}
					}
					else
					{
						if (!isPiercing[i])
						{
							damage[i]-=r;
							r=0;
							//System.out.println("Shields breached!!");
							return 0;
						}
					}
				}
				else if (damageType[i]==1 || damageType[i]==2 || damageType[i]==3)
				{
					damage[i]=0;
				}
			}
		}
		
		return r;
	}
}