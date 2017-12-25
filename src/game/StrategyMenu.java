package game;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Game;

public class StrategyMenu extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -3804271264423533919L;
	
	private Game state;
	private JPanel headingPanel;
	private JLabel turnLabel;
	private JButton startGame, addCrew;
	private boolean turn = true; // true = player 1, false = player 2
	
	public final int CREWCOST = 50;
	
	public Ship ship1, ship2;
	public boolean timer;
	public int timerLength;

	public StrategyMenu(Ship pShip1, Ship pShip2, Game g, boolean useTimer, int timerLength) {
		ship1 = pShip1;
		ship2 = pShip2;
		state = g;
		this.timer = useTimer;
		this.timerLength = timerLength;
		
		setLayout(new FlowLayout());
		
		headingPanel = new JPanel(new FlowLayout());
		if (turn) {			
			turnLabel = new JLabel("Player 1 -- Scrap: " + ship1.getScrap());
		}
		else {
			turnLabel = new JLabel("Player 2 -- Scrap: " + ship2.getScrap());
		}
		
		headingPanel.add(turnLabel);

		addCrew = new JButton("Add Crew");
		addCrew.addActionListener(this);
		
		startGame = new JButton("Finish");
		startGame.addActionListener(this);
		
		this.add(headingPanel);
		this.add(addCrew);
		this.add(startGame);
	}
	
	public void update() {
		if (turn) {			
			turnLabel.setText("Player 1 -- Scrap: " + ship1.getScrap());
		}
		else {
			turnLabel.setText("Player 2 -- Scrap: " + ship2.getScrap());
		}
		
		this.revalidate();
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		
		Ship s;
		if (turn) {
			s = ship1;
		}
		else {
			s = ship2;
		}
		
		if (b.equals(startGame)) {
			if (turn) {
				turn = !turn;
			}
			else {				
				state.startGame(timer, timerLength);
			}
		}
		else if (b.equals(addCrew)) {
			if (s.getScrap() >= CREWCOST && s.crew.size() < s.MAXCREWLENGTH) {
				s.addInitCrew(1);
				s.setScrap(s.getScrap() - CREWCOST);
			}
		}
		
	}
}
