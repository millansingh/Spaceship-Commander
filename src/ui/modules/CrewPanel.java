package ui.modules;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import game.Ship;
import game.SpaceshipGame;
import systems.Part;
import ui.components.RichSlider;

public class CrewPanel extends Module implements ActionListener {
	private Part system;
	private JLabel header;
	private RichSlider injuredSlider;
	private JButton crewButton;
	

	public CrewPanel(Ship own, int partNum, SpaceshipGame g) {
		super(own);
		
		system = owner.getPartNum(partNum);
		Border border = BorderFactory.createTitledBorder("Crew Controls");
		this.setBorder(border);
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JPanel crewPanel1 = new JPanel(new FlowLayout());
		JPanel crewPanel2 = new JPanel(new FlowLayout());
		
		header = new JLabel("Crew assigned: " + system.getCrewNum() + "/" + system.getEngineersNeeded() + " needed for maximum function.  ||  Medics available: " + owner.getAvailableMedics());
		
		JLabel inj = new JLabel("Injured Crew:");
		injuredSlider = new RichSlider(g, 0,system.getInjuredCrewNum(),0);
		crewButton = new JButton("Send to Medbay");
		crewButton.addActionListener(this);
		
		crewPanel1.add(header);
		//crewPanel1.add(availableMedics);
		crewPanel2.add(inj);
		crewPanel2.add(injuredSlider);
		crewPanel2.add(crewButton);
		
		this.add(crewPanel1);
		this.add(crewPanel2);
		
	}

	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if (b.equals(crewButton)) {
			
		}
	}

}
