package info.chenliang.tetris3d;


public class BlockFrame {
	public float[] vertices;
	public short[] indices;
	public float[] colors;

	public BlockFrame(float[] vertices, short[] indices, float[] colors) {
		super();
		this.vertices = vertices;
		this.indices = indices;
		this.colors = colors;
	}
	
}
