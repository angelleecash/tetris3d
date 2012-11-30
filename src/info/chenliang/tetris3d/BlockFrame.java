package info.chenliang.tetris3d;


public class BlockFrame {
	public float[] vertices;
	public short[] indices;
	public float[] colors;

	public BlockFrame(float[] blockCells, short[] indices, float[] colors) {
		super();
		this.vertices = blockCells;
		this.indices = indices;
		this.colors = colors;
	}
	
}
