package ui.modules;

import javax.swing.JPanel;

import game.Ship;

public class Module extends JPanel {
	
	private Ship owner;
	
	public Module(Ship own) {
		owner = own;
	}

}
