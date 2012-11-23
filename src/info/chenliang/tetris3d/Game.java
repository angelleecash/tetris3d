package info.chenliang.tetris3d;

import android.opengl.GLSurfaceView;

public class Game implements Runnable{

	public static final int REFRESH_INTERVAL = 30;
	
	public boolean running;
	public Thread thread;
	public GLSurfaceView view;
	
	public BlockContainer blockContainer;
	public Block block;
	
	public BlockFrameGenerator blockFrameGenerator;
	public int a;
	public Game(GLSurfaceView view) {
		super();
		this.view = view;
		blockContainer = new BlockContainer();
		blockFrameGenerator = new BlockFrameGenerator();
		
		block = new Block(0, 0, 0, 0, blockFrameGenerator.generateBlockFrames());
	}

	public void start()
	{
		if(thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void pause()
	{
		
	}
	
	public void exit()
	{
		running = false;
		synchronized (this) {
			notifyAll();
		}
	}
	
	private void init()
	{
		
	}

	private void tick()
	{
		
	}
	
	private void draw()
	{
		view.requestRender();
	}
	
	@Override
	public void run() {
		running = true;
		
		while(running)
		{
			long startTime = System.currentTimeMillis();
			
			tick();
			draw();
			
			long timeSpent = System.currentTimeMillis() - startTime;
			long sleepTime = REFRESH_INTERVAL - timeSpent;
			if(sleepTime > 0)
			{
				synchronized (this) {
					try {
						wait(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
