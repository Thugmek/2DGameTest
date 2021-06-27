package resources;

public class Texture {
    private float[] uvs;
    private int width;
    private int height;
    private float depth;
    private int maxWidth;
    private int maxHeight;

    private int subtextures = 1;
    private int subtexturesInRow = 6;

    public Texture(int width, int height, int index, int n) {
        this.width = width;
        this.height = height;
        this.depth = (float)index/(n)+0.00001f;
    }

    public void generate(int x, int y){

        maxHeight = y;
        maxWidth = x;

        float xf = (float)width/x;
        float yf = (float)height/y;

        uvs = new float[]{
                0,yf,depth,
                xf,yf,depth,
                0,0,depth,

                xf,0,depth,
                xf,yf,depth,
                0,0,depth,
        };
    }

    public float[] getUVs(){
        return uvs;
    }

    public void setSubtexturesParameters(int n, int inRow){
        subtextures = n;
        subtexturesInRow = inRow;
    }

    public float[] getUVs(int i){

        int row = i/subtexturesInRow;
        int cell = i%subtexturesInRow;

        float a = (float)width/(maxWidth*subtexturesInRow);

        float[] res = new float[]{
                (cell)*a,(row+1)*a,depth,
                (cell+1)*a,(row+1)*a,depth,
                (cell)*a,(row)*a,depth,

                (cell+1)*a,(row)*a,depth,
                (cell+1)*a,(row+1)*a,depth,
                (cell)*a,(row)*a,depth,
        };

        return res;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getIndex() {
        return depth;
    }
}
