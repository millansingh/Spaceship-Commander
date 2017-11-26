package weapons.cannon.guns;

public class Canon_Accurate extends Canon_Basic 
{
	public Canon_Accurate()
	{
		super();
		initHealth=65;
		health=65;
		acc = 0.85;
		energyCost = 90;
		setName(" Accurate Cannons");
		weight=1;
	}
}