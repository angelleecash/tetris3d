package info.chenliang.tetris3d;

import android.graphics.Point;

public class BlockContainer {
	
	public int rows, columns;
	
	public BlockContainerCell[][] cells;
	
	public BlockContainer(int rows, int columns) {
		super();
		this.rows = rows;
		this.columns = columns;
		
		init();
	}
	
	private void init()
	{
		cells = new BlockContainerCell[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				BlockContainerCell cell = new BlockContainerCell(j, i);
				cells[i][j] = cell;
			}
		}
	}

	public boolean canBePlacedtAt(BlockFrame blockFrame, int containerX, int containerY)
	{
		boolean result = true;
		
		detection:for (int i = 0; i < blockFrame.vertices.length; i+=3*8) 
		{
			int originalX = containerX + (int)blockFrame.vertices[i];
			int originalY = containerY - (int)blockFrame.vertices[i+1];
			
			if(originalX %2 != 0) throw new RuntimeException("");
			if(originalY %2 != 0) throw new RuntimeException("");
			
			int x = (originalX) / 2;
			int y = (originalY) / 2;
			
			if(x < 0 || x >= columns)
			{
				result = false;
				break;
			}
			
			if(y < 0)
			{
				continue;
			}
			
			if(y >= rows)
			{
				result = false;
				break;
			}
			
			BlockContainerCell cell = cells[y][x];
			switch (cell.state)
			{
				case OCCUPIED:
					result = false;
					break detection;
			}
		}
	
		return result;
	}
	
	public boolean canMoveDown(Block block)
	{
		BlockFrame blockFrame = block.getCurrentFrame();
		return canBePlacedtAt(blockFrame, block.containerX, block.containerY+2);	
	}
	
	public boolean canMoveLeft(Block block)
	{
		BlockFrame blockFrame = block.getCurrentFrame();
		return canBePlacedtAt(blockFrame, block.containerX-2, block.containerY);
	}
	
	public boolean canMoveRight(Block block)
	{
		BlockFrame blockFrame = block.getCurrentFrame();
		return canBePlacedtAt(blockFrame, block.containerX+2, block.containerY);
	}
	
	public boolean canRotate(Block block) 
	{
		return canBePlacedtAt(block.getNextFrame(), block.containerX, block.containerY);
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
//		containerX = 7;
//		containerY = 3;
		return new Point(containerX, containerY);
	}
}
