package crew;

public class Medic extends Crew 
{

	public Medic(int h) 
	{
		super(h);
		engineerSkill=0;
		repairSkill=0;
		medicSkill=30;
		role="Medic";
	}
	
	public Medic(int h, int r)
	{
		super(h);
		engineerSkill=0;
		repairSkill=0;
		medicSkill=30;
		role="Medic";
		setRank(r);
	}

}
