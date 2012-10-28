package puzzle.game;

import android.app.Activity;
import android.os.Bundle;



public class PuzzleBubble extends Activity
{
    
     private BubbleSurfaceView StartGame;   
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.StartGame = (BubbleSurfaceView) findViewById(R.id.mysurface);
 
    }
}



