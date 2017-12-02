package ui.modules;

import javax.swing.JLabel;

import game.Ship;
import systems.Part;

public class SystemStatusModule extends Module {
	
	private Part system;
	private JLabel healthLabel;
	
	public SystemStatusModule(Ship own, int partNum) {
		super(own);
		
		system = owner.getPartNum(partNum);
		
		healthLabel = new JLabel(system.getName() + " health:");
	}
	
	public int getHealth() {
		return system.health;
	}

}
