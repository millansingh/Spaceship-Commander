package main;

public class GraphicsThread extends Thread {

	private Game state;
	
	public boolean running;
	
	private int FPS = 30;
	private double averageFPS;
	private long averageFrameTime;
	
	public GraphicsThread(Game g) {
		state = g;
	}
	
	@Override
	public void run() {
		running = true;
		
		long startTime;
		long URDTimeMili;
		long waitTime;
		long totalTime = 0;
		long frameTime = 0;
		
		int frameCount = 0;
		int maxFrameCount = FPS;
		
		long targetTime = 1000/FPS;
		
		while(running) {
			startTime = System.nanoTime();
			
			draw();
			
			URDTimeMili = (System.nanoTime() - startTime) / 1000000;
			frameTime += URDTimeMili;
			waitTime = targetTime - URDTimeMili;
			
			try {
				Thread.sleep(waitTime);
			}
			catch (Exception e) {
			}
			
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			
			if (frameCount == maxFrameCount) {
				averageFPS = (double) 1000 / ((totalTime / frameCount) / 1000000);
				averageFrameTime = frameTime / frameCount;
				frameTime = 0;
				frameCount = 0;
				totalTime = 0;
				// System.out.println("FPS: " + averageFPS + "\n" + "Average Frame Time: " + averageFrameTime);
				if (state.drawFPS) {
					state.setFPSLabel(averageFPS, averageFrameTime);
				}
			}
		}
	}
	
	public void setRunning(boolean r) {
		running = r;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	public void draw() {
		if (state.getState() == 1) {
			state.updateStrategyMenu();
		}
		else if (state.getState() == 2) {
			state.updateGameWindow();
		}
	}

}
