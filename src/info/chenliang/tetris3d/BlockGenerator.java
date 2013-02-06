package info.chenliang.tetris3d;

import java.util.List;

public class BlockGenerator {
	List<BlockFramePrototype> blockFramePrototypes;

	public BlockGenerator(List<BlockFramePrototype> blockFramePrototypes) {
		super();
		this.blockFramePrototypes = blockFramePrototypes;
	}
	
	public Block generate()
	{
		int randomIndex = (int)(Math.random()*blockFramePrototypes.size());
		BlockFramePrototype blockFramePrototype = blockFramePrototypes.get(randomIndex); 
		
		Block block = new Block(0, 0, 0, 0, blockFramePrototype.blockFrames.toArray(new BlockFrame[0]));
		return block;
	}
}
