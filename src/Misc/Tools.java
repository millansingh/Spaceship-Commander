package Misc;

import java.util.ArrayList;

import Crew.Crew;
import Crew.Engineer;
import Main.Ship;

public class Tools
{
	private static Crew[] testCrew;
	
	public static void main (String[] args)
	{
		testCrew=new Crew[5];
		int j=3;
		for (int i=0;i<testCrew.length;i++)
		{
			if (i<j)
			{
				testCrew[i]=new Engineer(100,3+(int)(Math.random()*6));
				//System.out.println(testCrew[i].toString());
			}
			else
			{
				testCrew[i]=new Engineer(100,9+(int)(Math.random()*4));
				//System.out.println(testCrew[i].toString());
			}
		}
		
		ArrayList<Crew> a = new ArrayList<Crew>();
		for (Crew c : testCrew)
		{
			a.add(c);
		}
		System.out.println(a);
		System.out.println(sortCrew(a));
	}
	
	public static boolean chance(double i)
	{
		double a = Math.random();
		if (a<=i)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static String chance(String s, String st, double i)
	{
		if (chance(i))
		{
			return s;
		}
		else
		{
			return st;
		}
	}
	
	public static int chance(double i, double j)
	{
		double a = Math.random();
		if (a<=i)
		{
			return 0;
		}
		else if (a>i && a<=j)
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	
	public static String chance(String s, String st, String str, double i, double j)
	{
		int x = chance(i,j);
		
		if (x==0)
		{
			return s;
		}
		else if (x==1)
		{
			return st;
		}
		else
		{
			return str;
		}
	}
	
	public static int chance(double i, double j, double k)
	{
		double a = Math.random();
		if (a<=i)
		{
			return 0;
		}
		else if (a>i && a<=j)
		{
			return 1;
		}
		else if (a>j && a<=k)
		{
			return 2;
		}
		else
		{
			return 3;
		}
	}
	
	public static int chance(double i, double j, double k, double l)
	{
		double a = Math.random();
		if (a<=i)
		{
			return 0;
		}
		else if (a>i && a<=j)
		{
			return 1;
		}
		else if (a>j && a<=k)
		{
			return 2;
		}
		else if (a>k && a<=l)
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}
	
	public static int chance(double i, double j, double k, double l, double m)
	{
		double a = Math.random();
		if (a<=i)
		{
			return 0;
		}
		else if (a>i && a<=j)
		{
			return 1;
		}
		else if (a>j && a<=k)
		{
			return 2;
		}
		else if (a>k && a<=l)
		{
			return 3;
		}
		else if (a>l && a<=m)
		{
			return 4;
		}
		else
		{
			return 5;
		}
	}
	
	public static int chance(double i, double j, double k, double l, double m, double n)
	{
		double a = Math.random();
		if (a<=i)
		{
			return 0;
		}
		else if (a>i && a<=j)
		{
			return 1;
		}
		else if (a>j && a<=k)
		{
			return 2;
		}
		else if (a>k && a<=l)
		{
			return 3;
		}
		else if (a>l && a<=m)
		{
			return 4;
		}
		else if (a>m && a<=n)
		{
			return 5;
		}
		else
		{
			return 6;
		}
	}
	
	public static ArrayList<Crew> sortCrew(ArrayList<Crew> c)
	{
		int x = c.size();
		ArrayList<Crew> end = new ArrayList<Crew>();
		
		//Order crew by rank.
		for (int i=0;i<x;i++)
		{
			int a=0;
			for (int j=1;j<c.size();j++)
			{
				if (c.get(j).getRankNum()>=c.get(a).getRankNum())
				{
					a=j;
				}
			}
			end.add(c.get(a));
			c.remove(a);
		}
		
		//Remove deceased crew from list and then append that to the bottom.
		ArrayList<Crew> deceased = new ArrayList<Crew>();
		
		for (int k=0;k<end.size();k++)
		{
			if (end.get(k).isDead)
			{
				deceased.add(end.get(k));
				end.remove(k);
				k--;
			}
		}
		
		end.addAll(deceased);
		
		return end;
	}
	
	/*
	 * DEBUG TOOLS!!!!!!!!!!
	 */
	
	public static void clearConsole()
	{
	    try
	    {
	        String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (Exception exception)
	    {
	        //  Handle exception.
	    }
	}
}
