package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.Menu;

public class Game {
	
	public static Menu window;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	window = new Menu(new Game());
        		window.setBounds(700, 300, 500, 400);
        		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		window.setVisible(true);
        		System.out.println("Game initialized");
            }
        });
	}
	
	public void Game() {
		
	}
	
	public void startGame() {
		
	}
	
	public void goToMenu() {
		
	}

}
