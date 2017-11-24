package Main;

import javax.swing.JFrame;

public class Game extends JFrame {
	
	public static Menu window;

	public static void main(String[] args) {
		window = new Menu();
		window.setBounds(700, 300, 500, 400);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	public void Game() {
		
	}

}
