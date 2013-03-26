package info.chenliang.tetris3d;

import android.graphics.Point;

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
		BlockFrame blockFrame = block.currentFrame();
		
		for (int i = 0; i < blockFrame.vertices.length; i+=3) 
		{
			int x = (block.containerX + (int)blockFrame.vertices[i]) / 2;
			int y = (block.containerY - (int)blockFrame.vertices[i+1]) / 2;
			
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
	
	public Point generateStartPosition(BlockFrame blockFrame)
	{
		int containerX = 8;
		int containerY = 2;
		
		for (int i = 0; i < blockFrame.vertices.length; i+=3) {
			int x = (int)blockFrame.vertices[i];
			int y = (int)blockFrame.vertices[i+1];
			
			if(x % 2 == 1)
			{
				containerX = 7;
			}
			
			if(y % 2 == 1)
			{
				containerY = 3;
			}
			
		}
		
		return new Point(containerX, containerY);
	}
}
