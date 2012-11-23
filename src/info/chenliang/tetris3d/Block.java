package info.chenliang.tetris3d;

public class Block {
	public float x,y,z;
	public int color;
	public BlockFrame[] blockFrames;
	
	public Block(float x, float y, float z, int color, BlockFrame[] blockFrames) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.blockFrames = blockFrames;
	}
	
	
}
