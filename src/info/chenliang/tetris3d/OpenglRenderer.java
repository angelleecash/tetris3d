package info.chenliang.tetris3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

public class OpenglRenderer implements Renderer {

	Game game;
	
	public OpenglRenderer(Game game) {
		super();
		this.game = game;
	}

	@Override
	public void onDrawFrame(GL10 gl10) {
		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f/10000.0f)*((int)time);
		
		Matrix.setIdentityM(game.modelMatrix, 0);
		Matrix.rotateM(game.modelMatrix, 0, angleInDegrees, 0, 0, 1);
		
		Matrix.multiplyMM(game.finalMatrix, 0, game.viewMatrix, 0, game.modelMatrix, 0);
		Matrix.multiplyMM(game.finalMatrix, 0, game.projectionMatrix, 0, game.finalMatrix, 0);
		
		jniDrawFrame(game);
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int width, int height) {
		
		float aspectRatio = width*1.0f/height;
		float left = -aspectRatio;
		float right = aspectRatio;
		float bottom = -1.0f;
		float top = 1.0f;
		float near = 1.0f;
		float far = 10f;
		
		Matrix.frustumM(game.projectionMatrix, 0, left, right, bottom, top, near, far);
		
		jniSurfaceChanged(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
		jniSurfaceCreated();
	}
	
	static
	{
		System.loadLibrary("opengles");
	}
	
	private native void jniSurfaceCreated();
	private native void jniSurfaceChanged(int width, int height);
	private native void jniDrawFrame(Game game);
}
