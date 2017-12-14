package main;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.Menu;
import game.Ship;
import game.SpaceshipGame;

public class Game {
	
	public JFrame window;
	public Menu mainMenu;
	public SpaceshipGame game;
	private int state=-1;
	public boolean gameStart = false;
	
	private GraphicsThread drawThread;
	
	public Game() {
		state = 0;
		mainMenu = new Menu(this);
		
		window = new JFrame();
		window.setBounds(500, 300, 800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().add(mainMenu);
		window.pack();
		window.setVisible(true);
		
		System.out.println("Game initialized");
		
		drawThread = new GraphicsThread(this);
		drawThread.start();
	}
	
	public int getState() {
		return state;
	}
	
	public void close() {
		drawThread.setRunning(false);
		window.dispose();
	}
	
	public void startGame(boolean useTimer, int timerLength) {
		game = new SpaceshipGame(mainMenu.pShip1, mainMenu.pShip2, this, useTimer, timerLength);
		window.getContentPane().remove(mainMenu);
		window.getContentPane().add(game);
		window.pack();
		window.setVisible(true);
		state = 1;
	}
	
	public void goToMenu() {
		mainMenu = new Menu(this);
		window.getContentPane().remove(game);
		window.getContentPane().add(mainMenu);
		window.pack();
		window.setVisible(true);
		state = 0;
	}
	
	public void updateGameWindow(Ship s) {
		game.update(s);
	}

}
