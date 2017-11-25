package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.Menu;
import game.SpaceshipGame;

public class Game {
	
	public JFrame window;
	public Menu mainMenu;
	public SpaceshipGame game;
	private int state=-1;
	
	public Game() {
		state = 0;
		mainMenu = new Menu(this);
		
		window = new JFrame();
		window.setBounds(700, 300, 800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		window.getContentPane().add(mainMenu);
		System.out.println("Game initialized");
	}
	
	public int getState() {
		return state;
	}
	
	public void close() {
		window.dispose();
	}
	
	public void startGame(boolean useTimer, int timerLength) {
		if (game == null) {
			game = new SpaceshipGame(mainMenu.pShip1, mainMenu.pShip2, this, useTimer, timerLength);
		}
		window.getContentPane().remove(mainMenu);
		window.getContentPane().add(game);
		state = 1;
	}
	
	public void goToMenu() {
		
	}

}
