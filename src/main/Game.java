package main;

import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import game.Menu;
import game.Ship;
import game.SpaceshipGame;
import game.StrategyMenu;

public class Game {
	
	public JFrame window;
	public Menu mainMenu;
	public StrategyMenu strategyMenu;
	public SpaceshipGame game;
	private int state=-1;
	public boolean gameStart = false;
	public boolean drawFPS = true;
	private JPanel statsPanel;
	private JLabel FPSLabel, frameTimeLabel;
	
	private GraphicsThread drawThread;
	
	public Game() {
		state = 0;
		mainMenu = new Menu(this);
		
		window = new JFrame();
		window.setBounds(500, 300, 800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		statsPanel = new JPanel();
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
		statsPanel.setBounds(10,35,150,40);
		statsPanel.setOpaque(false);
		FPSLabel = new JLabel();
		frameTimeLabel = new JLabel();
		statsPanel.add(FPSLabel);
		statsPanel.add(frameTimeLabel);
		
		window.getLayeredPane().add(statsPanel,JLayeredPane.PALETTE_LAYER);
		window.getContentPane().add(mainMenu);
		
		window.pack();
		window.setVisible(true);
		
		System.out.println("Game initialized");
		
		drawThread = new GraphicsThread(this);
		drawThread.start();
	}
	
	public void setStatsPanel(double FPS, long averageFrameTime) {
		if (state == 2) {
			FPS = Math.round(FPS * 100.0)/100.0;
			FPSLabel.setText("FPS: " + FPS);
			frameTimeLabel.setText("Average Frame Time: " + averageFrameTime);
		}
	}
	
	public int getState() {
		return state;
	}
	
	public void close() {
		drawThread.setRunning(false);
		window.dispose();
	}
	
	public void beginStrategy(boolean useTimer, int timerLength) {
		strategyMenu = new StrategyMenu(mainMenu.pShip1, mainMenu.pShip2, this, useTimer, timerLength);
		window.getContentPane().remove(mainMenu);
		window.getContentPane().add(strategyMenu);
		window.pack();
		window.setVisible(true);
		state = 1;
	}
	
	public void startGame(boolean useTimer, int timerLength) {
		game = new SpaceshipGame(strategyMenu.ship1, strategyMenu.ship2, this, useTimer, timerLength);
		window.getContentPane().remove(strategyMenu);
		window.getContentPane().add(game);
		window.pack();
		window.setVisible(true);
		state = 2;
	}
	
	public void goToMenu() {
		mainMenu = new Menu(this);
		window.getContentPane().remove(game);
		window.getContentPane().add(mainMenu);
		window.pack();
		window.setVisible(true);
		state = 0;
	}
	
	public void updateStrategyMenu() {
		strategyMenu.update();
	}
	
	public void updateGameWindow() {
		game.update();
	}

}
