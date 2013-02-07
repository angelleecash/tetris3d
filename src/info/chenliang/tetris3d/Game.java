package info.chenliang.tetris3d;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.location.Address;
import android.opengl.GLSurfaceView;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Game implements Runnable, OnTouchListener{

	public static final int REFRESH_INTERVAL = 30;
	
	public boolean running;
	public Thread thread;
	public GLSurfaceView view;
	
	public BlockContainer blockContainer;
	public Block block;
	
	
//	public Matrix modelMatrix;
//	public Matrix viewMatrix;
	//public Matrix projectionMatrix;
	float[] projectionMatrix = new float[16];
	float[] viewMatrix = new float[16];
	float[] modelMatrix = new float[16];
	float[] finalMatrix = new float[16];
	public Tetris3d tetris3d;
	
	public BlockGenerator blockGenerator;
	
	public Game(GLSurfaceView view, Tetris3d tetris3d) {
		super();
		this.view = view;
		this.tetris3d = tetris3d;
	
		blockContainer = new BlockContainer();
		
		List<BlockFramePrototype> blockFramePrototypes = loadBlockFramePrototypes();
		blockGenerator = new BlockGenerator(blockFramePrototypes);
		
		block = blockGenerator.generate();
	}
	

	private List<BlockFramePrototype> loadBlockFramePrototypes()
	{
		List<BlockFramePrototype> blockFramePrototypes = new ArrayList<BlockFramePrototype>();
		
		try {
			InputStream inputStream = tetris3d.getResources().getAssets().open("Blocks.txt");
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;

			
			final int IDLE = 0;
			final int READ = 1;
			
			int mode = IDLE;
			
			int count = 0;
			int index = 0;
			
			ArrayList<BlockFrame> blockFrames = null;
			
			short[] baseIndices= {0,1,3,
					 1,2,3,
					 1,2,6,
					 1,5,6,
					 5,6,7,
					 5,4,7,
					 0,1,5,
					 0,4,5,
					 0,4,7,
					 0,3,3,
					 2,3,7,
					 2,6,7};
			
			while((line = bufferedReader.readLine()) != null)
			{
				line = line.trim();
				if(line.length() > 0 && !line.startsWith("/"))
				{
					if(mode == IDLE)
					{
						count = Integer.parseInt(line);
						index = 0;
						mode = READ;
						
						blockFrames = new ArrayList<BlockFrame>();
					}
					else if(mode == READ)
					{
						Assert.verify(index < count, "Block read out of boundary");
						
						String[] positions = line.split(" ");
						Assert.verify(positions.length == 4, "Invalid positions");
						
						float[] vertices = new float[4*8*3];
						short[] indices = new short[4*6*2*3];
						float[] colors = new float[4*8*3];		
						
						for(int i=0;i < 4;i ++)
						{
							String position = positions[i];
							String[] coordinates = position.split(",");
							Assert.verify(coordinates.length == 2, "Invalid coordinates");
							
							float x = Float.parseFloat(coordinates[0]);
							float y = Float.parseFloat(coordinates[1]);
							
							int colorOffset = i*8*3;
							
							float red = 0.0f;
							float green = 0.0f;
							float blue = 0.0f;
							
							if(i == 0)
							{
								green = 1.0f;
							}
							else if(i == 1)
							{
								red = 1.0f;
							}
							else if(i == 2)
							{
								blue = 1.0f;
							}
							else if(i == 3)
							{
								red = 1.0f;
								green = 1.0f;
							}
							
							for(int j=0;j < 8; j++)
							{
								colors[colorOffset + j*3] = red;
								colors[colorOffset + j*3 + 1] = green;
								colors[colorOffset + j*3 + 2] = blue;
							}
							
							int vertexOffset = i*8*3;
							
							float z = 1;
							
							vertices[vertexOffset + 0] = x;
							vertices[vertexOffset + 1] = y;
							vertices[vertexOffset + 2] = z;
							
							vertices[vertexOffset + 3] = x+2;
							vertices[vertexOffset + 4] = y;
							vertices[vertexOffset + 5] = z;
							
							vertices[vertexOffset + 6] = x+2;
							vertices[vertexOffset + 7] = y+2;
							vertices[vertexOffset + 8] = z;
							
							vertices[vertexOffset + 9] = x;
							vertices[vertexOffset + 10] = y+2;
							vertices[vertexOffset + 11] = z;
							
							z = -1;
							
							vertices[vertexOffset + 12] = x;
							vertices[vertexOffset + 13] = y;
							vertices[vertexOffset + 14] = z;
							
							vertices[vertexOffset + 15] = x+2;
							vertices[vertexOffset + 16] = y;
							vertices[vertexOffset + 17] = z;
							
							vertices[vertexOffset + 18] = x+2;
							vertices[vertexOffset + 19] = y+2;
							vertices[vertexOffset + 20] = z;
							
							vertices[vertexOffset + 21] = x;
							vertices[vertexOffset + 22] = y+2;
							vertices[vertexOffset + 23] = z;
							
							int indexOffset = i*6*2*3;
							int v = i*8;
							for(int j=0;j < baseIndices.length;j ++)
							{
								indices[indexOffset+j] = (short)(baseIndices[j] + v);
							}
						}
						
						for (int j = 0; j < vertices.length; j++) {
							vertices[j] *= 15;
						}
						
						BlockFrame blockFrame = new BlockFrame(vertices, indices, colors);
						blockFrames.add(blockFrame);
						
						index ++;
						if(index >= count)
						{
							BlockFramePrototype blockFramePrototype = new BlockFramePrototype(blockFrames);
							blockFramePrototypes.add(blockFramePrototype);
							
							mode = IDLE;
						}
					}
					else
					{
						Assert.verify(false, "unknown state");
					}
					
				}
			}
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Can not load block data", e);
		}
		
		Assert.verify(blockFramePrototypes.size() == 7, "There should be 7 type of blocks!!!");
		
		return blockFramePrototypes;
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

	long blockChangeTimer;
	private void tick()
	{
		blockChangeTimer += 100;
		if(blockChangeTimer >= 20000)
		{
			block = blockGenerator.generate();
			blockChangeTimer = 0;
		}
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


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(block != null)
		{
			block.frame += 1;
			if(block.frame >= block.blockFrames.length)
			{
				block = blockGenerator.generate();
			}
		}
		return false;
	}
}
