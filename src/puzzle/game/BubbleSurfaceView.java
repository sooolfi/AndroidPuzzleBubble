/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.util.Vector;
    
/**
 *
 * @author guido
 */

public class BubbleSurfaceView extends SurfaceView implements Callback {
        
        private EjemploThread thread;
        private int signoAngulo=0; //1 es positivo , -1 es negativo
	private float angle = 0;
        private int posX = 130;
        private int posY = 320;        
        private Context mContext;
        private Bitmap ballImage,arrowImage;
        private boolean mRun = true;  
        private long mLastTime;
        private boolean shutBall = false;  //se activa cuando lanzo la bola
        private Matrix moveMatrix = new Matrix(); // para rotar bitmap de la flecha
        private Paint myPaint;
        private CalculateScreenBall points;
        private boolean crash = false;
        
        class EjemploThread extends Thread {
            private SurfaceHolder mSurfaceHolder;
            private int mCanvasHeight = 1;
            private int mCanvasWidth = 1;        
            private boolean mRun = true;
            private long mLastTime;

            
        public EjemploThread(SurfaceHolder surfaceHolder, Context context,Handler handler) {
            mSurfaceHolder = surfaceHolder;
            mContext = context;           
            Resources res = context.getResources();            
            ballImage = BitmapFactory.decodeResource(res, R.drawable.icon);
            arrowImage = BitmapFactory.decodeResource(res, R.drawable.tiro2);						
            points = new CalculateScreenBall();

        }
		
        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {                          
                    	if(shutBall){                            
                            updatePhysics(); 
                            checkCrash();                            
                            }
                        doDraw(c);
                        
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
		}
		
        private void updatePhysics() {            
	 long now = System.currentTimeMillis();
            // No hace nada si mLastTime es en el futuro
            if (mLastTime > now) return;
            //chequear si no se fue de la pantalla 
            if(posY>0 && crash == false)
                if(angle>=0) //se va para la derecha
                    if(posX <mCanvasWidth-ballImage.getWidth()){ //si no toco la pared
                        posX+= 5*Math.cos((Math.PI/180)*(90-angle));
                        posY-= 5*Math.sin((Math.PI/180)*(90-angle));
                    }else{                                                
                        posX-= 5*Math.cos((Math.PI/180)*(90 -angle));
                        posY-= 5*Math.sin((Math.PI/180)*(90 -angle));
                        angle = -angle;//cambiamos el angulo  cuando choca la pared                                            
                    }
                else{ //se va para la izquierda
                    if(posX>0){
                        posX-= 5*Math.cos((Math.PI/180)*(90 +angle));
                        posY-= 5*Math.sin((Math.PI/180)*(90 +angle));
                    }
                    else{
                        
                        posX+= 5*Math.cos((Math.PI/180)*(90 -angle));
                        posY-= 5*Math.sin((Math.PI/180)*(90 -angle));
                        angle = -angle;
                    }}

            else {
                shutBall = false;
                posY = 320;
                posX = 130;
                crash = false;
                signoAngulo = 0;
            }
         
            }		
		
        //dibuja la pantalla
        private void doDraw(Canvas canvas) {        	            
            //limpio la pantalla
            canvas.drawColor(Color.BLACK);
            if(!shutBall){ 
            //si no hay un tiro dibujo la flecha                            
            //rutina para recuperar el angulo original de la flecha
            if(signoAngulo == 1)
                angle = Math.abs(angle);
            else
                if(signoAngulo == -1)
                    angle = -1* Math.abs(angle);            
            moveMatrix.reset();
            moveMatrix.postRotate(angle,29,81);
            moveMatrix.postTranslate(130,270);
            canvas.drawBitmap(arrowImage, moveMatrix, myPaint);                       
            } else
                canvas.drawBitmap(ballImage, posX,posY, myPaint);                                   
            //dibujar la pantalla 
            Vector y = new Vector();
            for(int i=0;i<points.getSize();i++){
                if(points.isInUse(i)){
                y = points.getCoord(i);
                int x1 = Integer.parseInt(y.get(0).toString());
                int x2 = Integer.parseInt(y.get(1).toString());    
                canvas.drawBitmap(ballImage, x1,x2, myPaint); 
                    }               
            }
        }
		
		
        /* se invoca cuando cambia el tamaño de la pantalla */
        public void setSurfaceSize(int width, int height) {            
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
                ballImage = Bitmap.createScaledBitmap(ballImage, 55, 55, true);
                arrowImage = Bitmap.createScaledBitmap(arrowImage,60, 112, true);
                //seteo los parametros para calcular la grilla hexagonal
                points.setParameters(mCanvasWidth,mCanvasHeight,ballImage.getWidth(),ballImage.getHeight());
            }
        }

        public void checkCrash() {
            //calculo la menor distancia
            int datos[]=points.calculateDistance(posX, posY);            
            //datos contiene el indice y la distancia menor
            //calculo si cboco con algo
            int result = points.ballsCollide(posX, posY,datos[0],datos[1]);
            if(result!=-1){
                points.setBallPosition(result);
                crash = true;
        }}
		
		
	}
        
	
        
        
	public BubbleSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);		
            mContext = context;
            // lo registro para se entere si hay cambios
            SurfaceHolder holder = getHolder();
            holder.addCallback(this);  
            myPaint = new Paint();
            //para suavizar bitmap cuando lo giro
            myPaint.setFilterBitmap(true);
            // solo creo el hilo, se inicia en surfaceCreated()
            thread = new EjemploThread(holder, mContext,null);
           // createThread(holder);
            setFocusable(true);
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		thread.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
	}
        
	public EjemploThread getThread() {
		return thread;
	}
        //cambia el angulo de la flecha
        void changeArrowAngle(float angles) {
            if(!shutBall)
            angle = angles;
        }
        //es true cuando se dispara la bola
        void shutBall() {
            //chequear que el tiro se puede hacer y si hay lugares 
            shutBall = true;
            if(angle >= 0)
                signoAngulo = 1;
            else
                signoAngulo =-1;
        }
        
        
        //CLASE THREAD
        
        
        
        
        
        
        
        

}