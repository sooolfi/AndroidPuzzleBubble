/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle.game;

import java.util.Vector;

/**
 *
 * @author guido
 */
public class CalculateScreenBall {
    
    private Vector pointsInScreen;
    private Vector pointsInUse;  //0 no tiene bola, 1 tiene bola
    private int heightCanvas;
    private int widthCanvas;
    private int widthBall;
    private int heightBall;
    private int rows;

    
    public CalculateScreenBall() {
       rows=4; //utilizar heightCanvas
       pointsInScreen = new Vector();
    }
    
    void setParameters(int mCanvasWidth, int mCanvasHeight, int width, int height) {
       this.heightCanvas = mCanvasHeight;
       this.widthCanvas = mCanvasWidth;
       this.widthBall = width;
       this.heightBall = height;
       calculatePoints();
    }

    private void calculatePoints() {
        int radiusBall = heightBall /2;
        double distHexSphere =(radiusBall/Math.cos(Math.PI/6) -radiusBall);        
        int posx= 0;
        int posy = (int) distHexSphere;
        
        for(int i=0; i<rows;i++){
            while(posx < widthCanvas-heightBall){
                Vector aux = new Vector();
                aux.addElement(posx);
                aux.addElement(posy);
                pointsInScreen.addElement(aux);
                posx +=2*radiusBall;
            } 
            //si es fila par desplazamos x            
            if (i%2==0)
            posx = radiusBall;
            else
            posx = 0;
            posy += (distHexSphere  + radiusBall + (radiusBall + distHexSphere)*Math.sin(Math.PI/6));
                }
        //inicializo la pantalla en blanco
        pointsInUse = new Vector();
        for(int j=0;j<pointsInScreen.size();j++)
            pointsInUse.addElement("0");
        
    }
    
    public Vector getCoord(int numberBall){
        return (Vector) pointsInScreen.get(numberBall);
    }
    public int getSize(){
        return pointsInScreen.size();
    }
    
    public int[] calculateDistance(int x, int y){       
        double distancia = heightCanvas;
        int indice = -1;
        int x1,y2;
            for (int i =0; i<pointsInScreen.size();i++){
                Vector aux2 = (Vector) pointsInScreen.get(i);
                x1= Integer.parseInt(aux2.get(0).toString());
                y2 = Integer.parseInt(aux2.get(1).toString());
                double distAux = Math.sqrt( Math.pow((x1-x),2) + Math.pow((y2 - y),2));
                if(distAux < distancia){
                    distancia = distAux;
                    indice = i;
                }
            }             
            int dist =(int)distancia; 
            return new int[] {indice, dist};
            
    }
    
    public int ballsCollide(int x, int y, int indiceMenorDistancia,int distancia){
        //si llega al techo la ubico
        if(distancia < widthBall && pointsInUse.get(indiceMenorDistancia)=="0"&& y <=5)
            //si choca con una bola la reubico. calculo la menor distancia cercana que no tiene bola
            return indiceMenorDistancia;
        else
            if(distancia < widthBall && pointsInUse.get(indiceMenorDistancia)=="1")
                //buscar el segundo mas cercano
                return calculateDistanceNotUsed(x, y);
            else
                return -1;

        //si hay colision devuelve el indice del punto de colision, sino retorna -1
    }

    public boolean isInUse(int i) {
        if(pointsInUse.get(i)=="1")
            return true;
        else
            return false;
    }

    void setBallPosition(int result) {
        pointsInUse.set(result, "1");
    }

    private int calculateDistanceNotUsed(int x, int y) {
       double distancia = heightCanvas;
        int indice = -1;
        int x1,y2;
            for (int i =0; i<pointsInScreen.size();i++){
                Vector aux2 = (Vector) pointsInScreen.get(i);
                x1= Integer.parseInt(aux2.get(0).toString());
                y2 = Integer.parseInt(aux2.get(1).toString());
                double distAux = Math.sqrt( Math.pow((x1-x),2) + Math.pow((y2 - y),2));
                if(distAux < distancia && pointsInUse.get(i)=="0"){
                    distancia = distAux;
                    indice = i;
                }
            }             
            return indice;
    }
    
}
