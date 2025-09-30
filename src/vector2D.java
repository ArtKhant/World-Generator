public class vector2D {

    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private double length;
    private double angle;

    public vector2D(int x, int y, int x1, int y1){

        startX = x;
        startY = y;
        endX = x1;
        endY = y1;

        //calculating changes of x and y
        int dx = endX-startX;
        int dy = y1-startY;

        //calculating angle and lenght

        if(dx ==0  && dy == 0){
            length = 0;
            angle = 0;
        }else {
            length = Math.sqrt(dx * dx + dy * dy);
            angle = Math.atan2(dy,dx);
        }
    }

    public int getDX(){
        return endX-startX;
    }

    public int getDY(){
        return endY-startY;
    }

    public double getAngle(){
        return angle;
    }

    public double getLength(){
        return length;
    }

    public String toString() {
        return String.format("Vector2D[start=(%d,%d), end=(%d,%d), length=%.2f, angle=%.2f rad]",
                startX, startY, endX, endY, length, angle);
    }
}
