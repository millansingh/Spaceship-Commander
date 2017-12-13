package main;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.Menu;
import game.Ship;
import game.SpaceshipGame;

public class Game implements Runnable {
	
	public JFrame window;
	public Menu mainMenu;
	public SpaceshipGame game;
	private int state=-1;
	public boolean gameStart = false;
	public boolean running;
	
	private Thread gameThread;
	
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
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void run() {
		running = true;
		
		while(running) {
			draw();
		}
	}
	
	public int getState() {
		return state;
	}
	
	public void close() {
		running = false;
		window.dispose();
	}
	
	public void draw() {
		if (state != -1) {
			
		}
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
