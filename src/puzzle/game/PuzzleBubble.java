package puzzle.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class PuzzleBubble extends Activity
{
    
     private BubbleSurfaceView StartGame;
     private Button left;
     private Button right;
     private Button shut;
     private int angle;
     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        this.StartGame = (BubbleSurfaceView) findViewById(R.id.mysurface);   
        angle = 0;
        addListenerOnButton();
    }
    private void addListenerOnButton() {

       left = (Button) findViewById(R.id.left);
       left.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if(angle>-85){
                    angle -=2;
                    StartGame.changeArrowAngle(angle);}                    
                }});
       right = (Button) findViewById(R.id.right);
       right.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if(angle<85){
                    angle +=2;
                    StartGame.changeArrowAngle(angle);}                                     
                }});
       shut = (Button) findViewById(R.id.shut);
       shut.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    StartGame.shutBall();
                }});
       
       
       
       
    }
    
    
}



