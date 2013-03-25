package info.chenliang.tetris3d;

public class BlockContainer {
	
	public int rows, columns;
	
	public BlockContainerCell[][] data;
	
	public BlockContainer(int rows, int columns) {
		super();
		this.rows = rows;
		this.columns = columns;
		
		init();
	}
	
	private void init()
	{
		data = new BlockContainerCell[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				BlockContainerCell cell = new BlockContainerCell(j, i);
				data[i][j] = cell;
			}
		}
	}

	public boolean canMoveDown(Block block)
	{
		for (int i = 0; i < block.transformedVertices.length; i+=3) 
		{
			int x = (int)block.transformedVertices[i];
			int y = rows - (int)block.transformedVertices[i+1];
			
			if(x < 0 || x >= columns)
			{
				continue;
			}
			
			if(y < 0)
			{
				continue;
			}
			
			if(y >= rows - 1)
			{
				return false;
			}
			
			BlockContainerCell cell = data[y][x];
			switch (cell.state)
			{
				case OCCUPIED:
					return false;
			}
		}
		
		return true;	
	}
}
