package info.chenliang.tetris3d;

import java.util.List;

import android.graphics.Point;

public class BlockGenerator {
	public BlockContainer blockContainer;
	List<BlockFramePrototype> blockFramePrototypes;

	public BlockGenerator(BlockContainer blockContainer, List<BlockFramePrototype> blockFramePrototypes) {
		super();
		this.blockContainer = blockContainer; 
		this.blockFramePrototypes = blockFramePrototypes;
	}
	int index;
	public Block generate()
	{
		int randomIndex = (int)(Math.random()*blockFramePrototypes.size());
		randomIndex = index;
		
		index ++;
		index %= blockFramePrototypes.size();
		BlockFramePrototype blockFramePrototype = blockFramePrototypes.get(randomIndex); 
		
		BlockFrame[] blockFrames = blockFramePrototype.blockFrames.toArray(new BlockFrame[0]);
		
		BlockFrame blockFrame = blockFrames[0];
		Point startPosition = blockContainer.generateStartPosition(blockFrame); 
		
		Block block = new Block(0, 20, 0, 0, blockFrames, startPosition.x, startPosition.y);
		return block;
	}
}
