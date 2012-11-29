package info.chenliang.tetris3d;


public class BlockFrame {
	public float[] vertices;
	public byte[] indices;

	public BlockFrame(float[] blockCells, byte[] indices) {
		super();
		this.vertices = blockCells;
		this.indices = indices;
	}
	
}
