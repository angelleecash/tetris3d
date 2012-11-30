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

	int angle = 0;
	
	@Override
	public void onDrawFrame(GL10 gl10) {
		long time = SystemClock.uptimeMillis() % 10000L;
		angle += 1;
		angle %= 360;
		
		//float angleInDegrees = (360.0f/10000.0f)*((int)time);
		float angleInDegrees = angle;
		
		Matrix.setIdentityM(game.modelMatrix, 0);
		Matrix.rotateM(game.modelMatrix, 0, angleInDegrees, 0, 1, 0);
		
		//Matrix.setIdentityM(game.viewMatrix, 0);
		
		Matrix.multiplyMM(game.finalMatrix, 0, game.viewMatrix, 0, game.modelMatrix, 0);
		Matrix.multiplyMM(game.finalMatrix, 0, game.projectionMatrix, 0, game.finalMatrix, 0);

		jniDrawFrame(game);
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int width, int height) {
		
		 // Position the eye behind the origin.
	    final float eyeX = 40.0f;
	    final float eyeY = 40.0f;
	    final float eyeZ = 40.0f;
	 
	    // We are looking toward the distance
	    final float lookX = 0.0f;
	    final float lookY = 0.0f;
	    final float lookZ = 0.0f;
	 
	    // Set our up vector. This is where our head would be pointing were we holding the camera.
	    final float upX = 0.0f;
	    final float upY = 1.0f;
	    final float upZ = 0.0f;
	 
	    // Set the view matrix. This matrix can be said to represent the camera position.
	    // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
	    // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
	    Matrix.setLookAtM(game.viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		float aspectRatio = width*1.0f/height;
		float left = -aspectRatio;
		float right = aspectRatio;
		float bottom = -1.0f;
		float top = 1.0f;
		float near = 0.1f;
		float far = 75f;
		
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
