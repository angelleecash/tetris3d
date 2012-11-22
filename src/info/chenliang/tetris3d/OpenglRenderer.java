package info.chenliang.tetris3d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class OpenglRenderer implements Renderer {

	Game game;
	
	public OpenglRenderer(Game game) {
		super();
		this.game = game;
	}

	@Override
	public void onDrawFrame(GL10 gl10) {
		jniDrawFrame();
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int width, int height) {
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
	private native void jniDrawFrame();
}
