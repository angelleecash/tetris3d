package info.chenliang.tetris3d;

public class VertexView {
	public Block block;
	public VertexViewCube[] cubes;
	public float[] modelVertices;
	public BlockFrame blockFrame;
	public float[] transformedVertices;
	
	public VertexView(Block block, BlockFrame blockFrame) {
		super();
		this.block = block;
		this.blockFrame = blockFrame;
		modelVertices = new float[4*3];
		
		for (int i = 0,j=0; i < blockFrame.vertices.length; i+=3*8) {
			modelVertices[j++] = blockFrame.vertices[i];
			modelVertices[j++] = blockFrame.vertices[i+1];
			modelVertices[j++] = blockFrame.vertices[i+2];
		}
	}

	public void copyFromFrame(){
		float[] vertices = blockFrame.vertices;
		this.transformedVertices = new float[vertices.length];
		System.arraycopy(vertices, 0, this.transformedVertices, 0, vertices.length);
	}
	
	public void translate(float deltaX, float deltaY)
	{
		for(int i=0; i < transformedVertices.length ;i +=3)
		{
			transformedVertices[i+1] += deltaY;
		}
	}
	
	public void setPosition(int containerX, int containerY)
	{
		for(int i=0; i < transformedVertices.length ;i +=3*8)
		{
			transformedVertices[i] += containerX;
			transformedVertices[i+1] += block.blockContainer.rows*2 -containerY-2;
		}		
	}
}
