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
import main.Game;
import systems.Part;
import ui.components.RichSlider;

public class CrewPanel extends Module implements ActionListener {
	private Part system;
	private JLabel header;
	private RichSlider injuredSlider;
	private JButton crewButton;
	private SpaceshipGame parent; 
	private Game state;

	public CrewPanel(Ship own, int partNum, SpaceshipGame s, Game g) {
		super(own);
		parent = s;
		state = g;
		system = owner.getPartNum(partNum);
		Border border = BorderFactory.createTitledBorder("Crew Controls");
		this.setBorder(border);
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JPanel crewPanel1 = new JPanel(new FlowLayout());
		JPanel crewPanel2 = new JPanel(new FlowLayout());
		
		header = new JLabel("Crew assigned: " + system.getCrewNum() + "/" + system.getEngineersNeeded() + " needed for maximum function.  ||  Medics available: " + owner.getAvailableMedics());
		
		JLabel inj = new JLabel("Injured Crew:");
		injuredSlider = new RichSlider(parent, state, 0, system.getInjuredCrewNum(), 0, 5, 1);
		crewButton = new JButton("Send to Medbay");
		crewButton.addActionListener(this);
		
		if (injuredSlider.getMaximum() == 0) {
			injuredSlider.rsSetEnabled(false);
			crewButton.setEnabled(false);
		}
		
		crewPanel1.add(header);
		//crewPanel1.add(availableMedics);
		crewPanel2.add(inj);
		crewPanel2.add(injuredSlider);
		crewPanel2.add(crewButton);
		
		this.add(crewPanel1);
		this.add(crewPanel2);
		
	}

	public void update() {
		header.setText("Crew assigned: " + system.getCrewNum() + "/" + system.getEngineersNeeded() + " needed for maximum function.  ||  Medics available: " + owner.getAvailableMedics());
		injuredSlider.setMaximum(system.getInjuredCrewNum());
		injuredSlider.setValue(0);
		if (injuredSlider.getMaximum() == 0) {
			injuredSlider.rsSetEnabled(false);
			crewButton.setEnabled(false);
		}
		else {
			injuredSlider.rsSetEnabled(true);
			crewButton.setEnabled(true);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if (b.equals(crewButton)) {
			int i = injuredSlider.getValue();
			
			for (crew.Crew c : system.getInjuredCrew())
			{
				if (i>0)
				{
					owner.MedBay.addInjured(c);
					system.getCrew().remove(c);
					i--;
				}
			}
			update();
			state.updateGameWindow(owner);
		}
	}

}
