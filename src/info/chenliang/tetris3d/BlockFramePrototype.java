package info.chenliang.tetris3d;

import java.util.List;

public class BlockFramePrototype {
	List<BlockFrame> blockFrames;

	public BlockFramePrototype(List<BlockFrame> blockFrames) {
		super();
		this.blockFrames = blockFrames;
	}
	
	public BlockFrame getBlockFrame(int index)
	{
		Assert.verify(index < blockFrames.size(), "Frame index out of boundary");
		return blockFrames.get(index); 
	}
}
