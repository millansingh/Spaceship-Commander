package main;

import java.util.Scanner;

import game.Ship;
import game.SpaceshipGame;
import systems.Part;

public class cmdLineClass implements Runnable
{
	Ship s1,s2,current;
	SpaceshipGame parent;
	
	private Scanner scan;
	
	public cmdLineClass(Ship a, Ship b, SpaceshipGame s)
	{
		s1=a;
		s2=b;
		current=a;
		parent=s;
		System.out.println("Initialized");
	}
	
	public void scanner(String s)
	{
		if (s.equals("Drain"))
		{
			System.out.println("How much do you want to drain?");
			int n = scan.nextInt();
			current.Power.setEnergyStored(current.Power.getEnergyStored()-n);
			System.out.println("New energy level: " + current.Power.getEnergyStored());
			parent.update(current);
		}
		else if (s.equals("Add"))
		{
			System.out.println("How much do you want to add?");
			int n = scan.nextInt();
			current.Power.setEnergyStored(current.Power.getEnergyStored()+n);
			System.out.println("New energy level: " + current.Power.getEnergyStored());
			parent.update(current);
		}
		else if (s.equals("Damage System"))
		{
			System.out.println("Which system would you like to damage (give an index, not a name)?");
			int n = scan.nextInt();
			Part p = current.getPartNum(n);
			System.out.println("How much damage would you like to apply?");
			int d = scan.nextInt();
			p.health-=d;
			System.out.println("The new system health is " + p.health);
			parent.update(current);
		}
		else if (s.equals("Repair System"))
		{
			System.out.println("Which system would you like to repair (give an index, not a name)?");
			int n = scan.nextInt();
			Part p = current.getPartNum(n);
			System.out.println("How much damage would you like to repair?");
			int d = scan.nextInt();
			p.health+=d;
			System.out.println("The new system health is " + p.health);
			parent.update(current);
		}
		else if (s.equals("Start Fire"))
		{
			System.out.println("Which system would you like to start a fire in (give an index, not a name)?");
			int n = scan.nextInt();
			Part p = current.getPartNum(n);
			System.out.println("How big of a fire would you like to create?");
			int d = scan.nextInt();
			p.isOnFire=true;
			p.fireDamage+=d;
			System.out.println("The system is currently on fire with " + p.fireDamage + " fire damage per turn.");
			parent.update(current);
		}
		else
		{
			System.out.println("Invalid command, try again.");
			scanner(scan.nextLine());
		}
		System.out.println("All, done, what next?");
		scanner(scan.nextLine());
	}

	public void run() 
	{
		scan = new Scanner(System.in);
		System.out.println("Enter a debug command:");
		scanner(scan.nextLine());
	}
}