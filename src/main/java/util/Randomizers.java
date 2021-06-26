package util;

public class Randomizers {
    public static float getByPos(int x, int y, int seed){
        return (float)((Math.pow(Math.sin(x*7487.7487 + y*6571.6571 + seed * 6701.6701),2)*7459.7459)%1);
    }
}
