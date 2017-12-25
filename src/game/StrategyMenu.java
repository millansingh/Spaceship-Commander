package game;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.Game;

public class StrategyMenu extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -3804271264423533919L;
	
	private Game state;
	private JButton startGame;
	private boolean turn = true; // true = player 1, false = player 2
	
	public Ship ship1, ship2;
	public boolean timer;
	public int timerLength;

	public StrategyMenu(Ship pShip1, Ship pShip2, Game g, boolean useTimer, int timerLength) {
		setLayout(new FlowLayout());
		
		startGame = new JButton("Start Game");
		startGame.addActionListener(this);
		this.add(startGame);
		
		ship1 = pShip1;
		ship2 = pShip2;
		state = g;
		this.timer = useTimer;
		this.timerLength = timerLength;
	}

	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		
		if (b.equals(startGame)) {
			if (turn) {
				turn = !turn;
			}
			else {				
				state.startGame(timer, timerLength);
			}
		}
		
	}
}
