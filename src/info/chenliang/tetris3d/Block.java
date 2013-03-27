package info.chenliang.tetris3d;

public class Block {
	public int color;
	public int frame;
	
	public int containerX, containerY;
	public BlockContainer blockContainer;
	public BlockFramePrototype prototype;
	public VertexView vertexView;
	
	public Block(BlockContainer blockContainer, BlockFramePrototype prototype, int containerX, int containerY) {
		super();
		this.blockContainer = blockContainer;
		this.prototype = prototype;
		this.containerX = containerX;
		this.containerY = containerY;
		
		BlockFrame blockFrame = getCurrentFrame();
		vertexView = new VertexView(this, blockFrame);
		vertexView.setPosition(containerX, containerY);
	}
	
	public void drop()
	{
		containerY += 2;
		vertexView.translate(0, -2);
	}
	
	public void moveToLeft()
	{
		containerX -= 2;
		vertexView.translate(-2, 0);
	}
	
	public void moveToRight()
	{
		containerX += 2;
		vertexView.translate(2, 0);
	}
	
	public BlockFrame getCurrentFrame()
	{
		return prototype.blockFrames[frame];
	}
	
	public BlockFrame getNextFrame()
	{
		int nextFrame = frame + 1;
		nextFrame %= prototype.blockFrames.length;
		return prototype.blockFrames[nextFrame];
	}
}
