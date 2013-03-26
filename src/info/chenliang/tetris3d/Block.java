package info.chenliang.tetris3d;

public class Block {
	public float x,y,z;
	public int color;
	public BlockFrame[] blockFrames;
	public int frame;
	public float[] transformedVertices;
	public int containerX, containerY;
	
	public Block(float x, float y, float z, int color, BlockFrame[] blockFrames, int containerX, int containerY) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.blockFrames = blockFrames;
		this.containerX = containerX;
		this.containerY = containerY;
		
		copyFromFrame();
		
		for(int i=0; i < transformedVertices.length ;i +=3)
		{
			transformedVertices[i] += containerX;
			transformedVertices[i+1] += 40-containerY;
		}
	}
	
	public void copyFromFrame(){
		float[] vertices = blockFrames[0].vertices;
		this.transformedVertices = new float[vertices.length];
		System.arraycopy(vertices, 0, this.transformedVertices, 0, vertices.length);
	}
	
	public void drop()
	{
		containerY += 2;
		
		for(int i=0; i < transformedVertices.length ;i +=3)
		{
			transformedVertices[i+1] -= 2;
		}
	}
	
	public BlockFrame currentFrame()
	{
		return blockFrames[frame];
	}
}
