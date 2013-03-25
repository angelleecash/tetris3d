package info.chenliang.tetris3d;

public class Block {
	public float x,y,z;
	public int color;
	public BlockFrame[] blockFrames;
	public int frame;
	public float[] transformedVertices;
	
	public Block(float x, float y, float z, int color, BlockFrame[] blockFrames) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.blockFrames = blockFrames;
		copyFromFrame();
	}
	
	public void copyFromFrame(){
		float[] vertices = blockFrames[0].vertices;
		this.transformedVertices = new float[vertices.length];
		System.arraycopy(vertices, 0, this.transformedVertices, 0, vertices.length);
	}
	
	public void drop(float deltaY)
	{
		y += deltaY;
		
		for(int i=0; i < transformedVertices.length ;i +=3)
		{
			transformedVertices[i+1] += deltaY;
		}
	}
	
}
