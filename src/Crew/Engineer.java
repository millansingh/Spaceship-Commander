package Crew;

public class Engineer extends Crew 
{
	
	

	public Engineer(int h) 
	{
		super(h);
		engineerSkill=150;
		repairSkill=25;
		medicSkill=0;
		role="Engineer";
		setRank(5);
	}
	
	public Engineer(int h, int r)
	{
		super(h);
		engineerSkill=150;
		repairSkill=25;
		medicSkill=0;
		role="Engineer";
		setRank(r);
	}

}
