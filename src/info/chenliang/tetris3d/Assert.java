package info.chenliang.tetris3d;

public class Assert {
	public static void verify(boolean condition, String message)
	{
		if(!condition)
		{
			throw new RuntimeException(message);
		}
	}
}
