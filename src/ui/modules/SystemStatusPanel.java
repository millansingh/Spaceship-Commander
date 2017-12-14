package ui.modules;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import game.Ship;
import systems.Part;

public class SystemStatusPanel extends Module {
	
	private Part system;
	private JLabel healthLabel, oxygenLabel, repairLabel;
	private JProgressBar healthBar;
	
	private ImageIcon oxygenIcon = new ImageIcon("img/icons/icon_Oxygen.png","Oxygen Icon");
	private ImageIcon repairIcon = new ImageIcon("img/icons/icon_repair.png","Repair");
	
	public SystemStatusPanel(Ship own, int partNum) {
		super(own);
		
		system = owner.getPartNum(partNum);
		
		this.setLayout(new FlowLayout());
		
		healthLabel = new JLabel(system.getName() + " health:");
		healthBar = new JProgressBar(0, system.initHealth);
		healthBar.setString(String.valueOf(system.health));
		healthBar.setStringPainted(true);
		healthBar.setValue(system.health);
		healthBar.setForeground(system.getButtonColor());
		oxygenLabel = new JLabel(system.oxygenLevel*100 + "%",oxygenIcon,JLabel.LEFT);
		repairLabel = new JLabel(String.valueOf(system.getRepairCrewNum()),repairIcon,JLabel.LEFT);
		
		this.add(healthLabel);
		this.add(healthBar);
		this.add(oxygenLabel);
		this.add(repairLabel);
	}
	
	public int getHealth() {
		return system.health;
	}
	
	public void update() {
		oxygenLabel.setText(system.oxygenLevel*100 + "%");
		repairLabel.setText(String.valueOf(system.getRepairCrewNum()));
		
		this.revalidate();
		this.repaint();
	}

}
