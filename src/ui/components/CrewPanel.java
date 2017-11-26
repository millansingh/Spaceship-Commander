package ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ui.modules.RichSlider;

public class CrewPanel extends JPanel implements ActionListener {
	private JLabel label;
	private JButton sendButton;
	private RichSlider slider;
	
	public CrewPanel() {
		
	}

	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		
		if (b.equals(sendButton)) {
			
		}
	}
}
