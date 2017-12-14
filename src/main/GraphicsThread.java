package main;

public class GraphicsThread extends Thread {

	private Game state;
	
	public boolean running;
	
	private int FPS = 30;
	private double averageFPS;
	
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
		
		int frameCount = 0;
		int maxFrameCount = FPS;
		
		long targetTime = 1000/FPS;
		
		while(running) {
			startTime = System.nanoTime();
			
			draw();
			
			URDTimeMili = (System.nanoTime() - startTime) / 1000000;
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
				frameCount = 0;
				totalTime = 0;
				System.out.println("FPS: " + averageFPS);
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
		if (state.getState() != -1) {
			// TODO Implement this method
		}
	}

}
