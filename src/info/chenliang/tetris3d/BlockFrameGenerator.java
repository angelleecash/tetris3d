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
		
		blockFrames[0] = new BlockFrame(blockCells);
		
		return blockFrames;
	}
}
