package info.chenliang.tetris3d;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Tetris3d extends Activity {

	private Window window;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setFullScreen();
        
        Tetris3dView view = new Tetris3dView(this);
        view.setEGLContextClientVersion(2);
        view.setRenderer(new OpenglRenderer());
        
        setContentView(view);
    }
    
    private void setFullScreen()
    {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tetris3d, menu);
        return true;
    }
}
