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
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
    
/**
 *
 * @author guido
 */

public class BubbleSurfaceView extends SurfaceView implements Callback {
        
        private EjemploThread thread;
	private Context mContext;
	
	public BubbleSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);		
            mContext = context;
            // lo registro para se entere si hay cambios
            SurfaceHolder holder = getHolder();
            holder.addCallback(this);        
            // solo creo el hilo, se inicia en surfaceCreated()
            createThread(holder);
            setFocusable(true);
	}

	private void createThread(SurfaceHolder holder) {
        thread = new EjemploThread(holder, mContext, new Handler() {
            @Override
            public void handleMessage(Message m) {
            	//
            }        	
        });        
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		thread.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
    	//Inicio la ejecución del hilo
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        // finalizo la ejecución del hilo
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}
	
	public EjemploThread getThread() {
		return thread;
	}
        
        
        //CLASE THREAD
        
        class EjemploThread extends Thread {
		
            private SurfaceHolder mSurfaceHolder;
            private int mCanvasHeight = 1;
            private int mCanvasWidth = 1;        
            private Bitmap ballImage;
            private Bitmap arrowImage;
            private int posX = 140;
            private int posY = 340;
            private boolean mRun = true;
            private long mLastTime;

        public EjemploThread(SurfaceHolder surfaceHolder, Context context,Handler handler) {

            mSurfaceHolder = surfaceHolder;
            mContext = context;           
            Resources res = context.getResources();            
            ballImage = BitmapFactory.decodeResource(res, R.drawable.icon);
            arrowImage = BitmapFactory.decodeResource(res, R.drawable.flecha);
						
		}
		
        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {                        
                    	updatePhysics();
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
            			
//            if (posX<mCanvasWidth)
//				posX+=5;
//			else
//				posX=0;canvas.drawOval(null, mPainter);
            
		}		
		
		//dibuja la pantalla
		private void doDraw(Canvas canvas) {        	
                //limpio la pantalla
                canvas.drawColor(Color.BLACK);
                //dibujo al jugador
                canvas.drawBitmap(ballImage, posX, posY, null);	
                canvas.drawBitmap(arrowImage, 140, 300,null);
        }
		
		
        /* se invoca cuando cambia el tamaño de la pantalla */
        public void setSurfaceSize(int width, int height) {            
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
                ballImage = Bitmap.createScaledBitmap(ballImage, 50, 50, true);
                arrowImage = Bitmap.createScaledBitmap(arrowImage, 50, 50, true);
            }
        }		
		
		
	}
        
        
        
        
        
        

}