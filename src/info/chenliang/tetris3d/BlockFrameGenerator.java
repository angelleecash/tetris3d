package info.chenliang.tetris3d;

public class BlockFrameGenerator {
	public BlockFrame[] generateBlockFrames()
	{
		BlockFrame[] blockFrames = new BlockFrame[4];
		BlockCell[] blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(1.0f, 0.0f, 0.0f);
		blockCells[2] = new BlockCell(-1.0f, 0.0f, 0.0f);
		blockCells[3] = new BlockCell(0.0f, 1.0f, 0.0f);
		
		blockFrames[0] = generateBlockFrame(blockCells);
		
		blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(0.0f, 1.0f, 0.0f);
		blockCells[2] = new BlockCell(0.0f, -1.0f, 0.0f);
		blockCells[3] = new BlockCell(1.0f, 0.0f, 0.0f);
		
		blockFrames[1] = generateBlockFrame(blockCells);
		
		blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(1.0f, 0.0f, 0.0f);
		blockCells[2] = new BlockCell(-1.0f, 0.0f, 0.0f);
		blockCells[3] = new BlockCell(0.0f, -1.0f, 0.0f);
		
		blockFrames[2] = generateBlockFrame(blockCells);
		
		blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(0.0f, 1.0f, 0.0f);
		blockCells[2] = new BlockCell(-1.0f, 0.0f, 0.0f);
		blockCells[3] = new BlockCell(0.0f, -1.0f, 0.0f);
		
		blockFrames[3] = generateBlockFrame(blockCells);
		
		return blockFrames;
	}
	
	public BlockFrame generateBlockFrame(BlockCell[] blockCells)
	{
		float[] vertices = new float[blockCells.length*8*3];
		float[] colors = new float[blockCells.length*8*3];
		
		float red = 0.0f, green = 0.0f, blue = 0.0f;
		
		for(int i=0;i < colors.length;i+=3)
		{
			if(i == 0 || i%9 == 0)
			{
				red = (float)Math.random();
				green = (float)Math.random();
				blue = (float)Math.random();
			}
			colors[i] = red;
			colors[i+1] = green;
			colors[i+2] = blue;
			
		}
		
		for (int i = 0; i < blockCells.length; i++) {
			BlockCell blockCell = blockCells[i];
			
			final float x = blockCell.x;
			final float z = blockCell.z;
			
			int offset = i*8*3;
			
			float y = blockCell.y - 0.5f;			
			for(int j=0 ;j < 4; j ++)
			{
				vertices[offset] = x + 0.5f*(j%3==0?-1:1);
				vertices[offset+1] = y;
				vertices[offset+2] = z + 0.5f*(j<2?-1:1);
				
				offset += 3;
			}
			
			y = blockCell.y + 0.5f;
			for(int j=0 ;j < 4; j ++)
			{
				vertices[offset] = x + 0.5f*(j%3==0?-1:1);
				vertices[offset+1] = y;
				vertices[offset+2] = z + 0.5f*(j<2?-1:1);
				
				offset += 3;
			}
		}
		
		for(int i=0; i <vertices.length; i++)
		{
			vertices[i] *= 40;
		}
		
		short[] baseIndices= {1,2,3,
						 3,0,1,
						 4,7,6,
						 4,6,5,
						 0,4,1,
						 4,5,1,
						 1,5,2,
						 5,6,2,
						 3,6,7,
						 2,6,3,
						 0,3,4,
						 3,7,4};
		
		short[] indices = new short[4*6*2*3];
		System.arraycopy(baseIndices, 0, indices, 0, 6*2*3);
		
		for(int i=1; i <= 3; i++)
		{
			int offset = i*6*2*3;
			for(int j=0; j < baseIndices.length; j++)
			{
				indices[offset + j] = (short)(i*8 + baseIndices[j]);
			}
		}
		
		return new BlockFrame(vertices, indices, colors);
	}
}
