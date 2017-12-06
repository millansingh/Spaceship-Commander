package ui.modules;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import game.Ship;
import game.SpaceshipGame;
import systems.Part;
import ui.components.RichSlider;


public class SystemExtinguishPanel extends Module {

	private Part system;
	private SpaceshipGame parent;
	private RichSlider extSlider;
	private JLabel fireIconL, fireExtinguishL;
	private ImageIcon fireIcon = new ImageIcon("img/icons/icon_Fire.png","On Fire"),
			fireNIcon = new ImageIcon("img/icons/icon_FireN.png","Not On Fire");
	
	private ImageIcon extinguishIcon = new ImageIcon("img/icons/icon_Extinguisher.png","Extinguishers Activated"),
			extinguishNIcon = new ImageIcon("img/icons/icon_ExtinguisherN.png","Extinguishers Deactivated");
	
	public SystemExtinguishPanel(Ship own, int partNum,SpaceshipGame g) {
		super(own);
		parent = g;
		system = owner.getPartNum(partNum);
		JLabel ext = new JLabel("Extinguish Energy:");
		extSlider = new RichSlider(parent,0,owner.getMaxExtinguishers(system),0,owner.getMaxExtinguishers(system)/5,owner.getMaxExtinguishers(system)/25);
		extSlider.setValue(system.extinguishEnergy);
		fireIconL = new JLabel(String.valueOf(system.fireDamage),fireNIcon,JLabel.LEFT);
		fireExtinguishL = new JLabel(extinguishNIcon);
		
		if (system.isOnFire) {
			fireIconL.setIcon(fireIcon);
		}
		
		if (system.extinguishing) {
			fireExtinguishL.setIcon(extinguishIcon);
		}
			
		this.add(ext);
		this.add(extSlider);
		this.add(fireIconL);
		this.add(fireExtinguishL);
		
	}

}
