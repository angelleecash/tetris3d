package info.chenliang.tetris3d;

public class BlockContainerCell {
	
	public int x, y;
	public BlockContainerCellState state = BlockContainerCellState.EMPTY;
	public int color;
	
	public BlockContainerCell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	
}
