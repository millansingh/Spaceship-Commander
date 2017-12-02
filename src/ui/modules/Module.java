package ui.modules;

import javax.swing.JPanel;

import game.Ship;

public class Module extends JPanel {
	
	protected Ship owner;
	
	public Module(Ship own) {
		owner = own;
	}

}
