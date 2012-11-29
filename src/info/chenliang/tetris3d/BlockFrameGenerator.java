package info.chenliang.tetris3d;

public class BlockFrameGenerator {
	public BlockFrame[] generateBlockFrames()
	{
		BlockFrame[] blockFrames = new BlockFrame[4];
		BlockCell[] blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(0.0f, 0.0f, -1.0f);
		blockCells[2] = new BlockCell(0.0f, 0.0f, 1.0f);
		blockCells[3] = new BlockCell(0.0f, 1.0f, 0.0f);
		
		blockFrames[0] = generateBlockFrame(blockCells);
		
		blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(0.0f, 1.0f, 0.0f);
		blockCells[2] = new BlockCell(0.0f, 0.0f, -1.0f);
		blockCells[3] = new BlockCell(0.0f, -1.0f, 0.0f);
		
		blockFrames[1] = generateBlockFrame(blockCells);
		
		blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(0.0f, 0.0f, -1.0f);
		blockCells[2] = new BlockCell(0.0f, 0.0f, 1.0f);
		blockCells[3] = new BlockCell(0.0f, -1.0f, 0.0f);
		
		blockFrames[2] = generateBlockFrame(blockCells);
		
		blockCells = new BlockCell[4];
		blockCells[0] = new BlockCell(0.0f, 0.0f, 0.0f);
		blockCells[1] = new BlockCell(0.0f, 1.0f, 0.0f);
		blockCells[2] = new BlockCell(0.0f, 0.0f, 1.0f);
		blockCells[3] = new BlockCell(0.0f, -1.0f, 0.0f);
		
		blockFrames[3] = generateBlockFrame(blockCells);
		
		return blockFrames;
	}
	
	public BlockFrame generateBlockFrame(BlockCell[] blockCells)
	{
		float[] vertices = new float[blockCells.length*8*3];
		
		for (int i = 0; i < blockCells.length; i++) {
			BlockCell blockCell = blockCells[i];
			
			final float x = blockCell.x;
			final float z = blockCell.z;
			
			int offset = i*8*3;
			
			float y = blockCell.y - 0.5f;			
			for(int j=0 ;j < 4; j ++)
			{
				vertices[offset] = x + 0.5f*(i%3==0?-1:1);
				vertices[offset+1] = y;
				vertices[offset+2] = z + 0.5f*(i<2?-1:1);
				
				offset += 3;
			}
			
			y = blockCell.y + 0.5f;
			for(int j=0 ;j < 4; j ++)
			{
				vertices[offset] = x + 0.5f*(i%3==0?-1:1);
				vertices[offset+1] = y;
				vertices[offset+2] = z + 0.5f*(i<2?-1:1);
				
				offset += 3;
			}
		}
		
		for(int i=0; i <vertices.length; i++)
		{
			vertices[i] *= 30;
		}
		
		byte[] indices= {1,2,3,
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
		
		return new BlockFrame(vertices, indices);
	}
}
