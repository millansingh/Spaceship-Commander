package ui.modules;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import game.Ship;
import game.SpaceshipGame;
import main.Game;
import systems.Part;
import ui.components.RichSlider;


public class SystemExtinguishPanel extends Module {

	private Part system;
	private Game state;
	private RichSlider extSlider;
	private JLabel fireIconL, fireExtinguishL;
	private ImageIcon fireIcon = new ImageIcon("img/icons/icon_Fire.png","On Fire"),
			fireNIcon = new ImageIcon("img/icons/icon_FireN.png","Not On Fire");
	
	private ImageIcon extinguishIcon = new ImageIcon("img/icons/icon_Extinguisher.png","Extinguishers Activated"),
			extinguishNIcon = new ImageIcon("img/icons/icon_ExtinguisherN.png","Extinguishers Deactivated");
	
	public SystemExtinguishPanel(Ship own, int partNum,Game g) {
		super(own);
		state = g;
		system = owner.getPartNum(partNum);
		JLabel ext = new JLabel("Extinguish Energy:");
		extSlider = new RichSlider(this,0,owner.getMaxExtinguishers(system),0,owner.getMaxExtinguishers(system)/5,owner.getMaxExtinguishers(system)/25, true);
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
	
	public void update() {
		if (state.gameStart) {
			extSlider.setMaximum(owner.getMaxExtinguishers(system));
			extSlider.setMajorTickSpacing(owner.getMaxExtinguishers(system)/5);
			extSlider.setMinorTickSpacing(owner.getMaxExtinguishers(system)/25);
			if (owner.getMaxExtinguishers(system)>5)
			{
				extSlider.setLabelTable(extSlider.createStandardLabels(owner.getMaxExtinguishers(system)/5));
			}
			
			/*
			 * Used for debug set fire command only right now!!
			 * */
			if (system.isOnFire) {
				fireIconL.setIcon(fireIcon);
				fireIconL.setText(String.valueOf(system.fireDamage));
			}
			else {
				fireIconL.setIcon(fireNIcon);
			}
			
			
			if (system.extinguishing) {
				fireExtinguishL.setIcon(extinguishIcon);
			}
			else {
				fireExtinguishL.setIcon(extinguishNIcon);
			}
			
			system.initializeExtinguishers(extSlider.getValue());
			this.revalidate();
			this.repaint();
		}
	}

}
