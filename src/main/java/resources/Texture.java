package resources;

public class Texture {
    private float[] uvs;
    private int width;
    private int height;
    private float depth;
    private int maxWidth;
    private int maxHeight;

    private int subtextures = 1;
    private int subtexturesInRow = 9;
    private String name;

    public Texture(int width, int height, int index, int n, String name) {
        this.width = width;
        this.height = height;
        this.depth = (float)index/(n)+0.00001f;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void generate(int x, int y){

        maxHeight = y;
        maxWidth = x;

        float xf = (float)width/x;
        float yf = (float)height/y;

        uvs = new float[]{
                0,xf,depth,
                yf,xf,depth,
                0,0,depth,

                yf,0,depth,
                yf,xf,depth,
                0,0,depth,
        };
    }

    public float[] getUVs(){
        return uvs;
    }

    public void setSubtexturesParameters(int n, int inRow){
        //subtextures = n;
        //subtexturesInRow = inRow;
    }

    public float[] getUVs(int i){

        int row = i/subtexturesInRow;
        int cell = i%subtexturesInRow;

        float a = (float)width/(maxWidth*subtexturesInRow);

        //System.out.println(String.format("subtexturesInRow: %d, width: %d, height: %d, maxWidth: %d, maxHeight: %d, a: %f",subtexturesInRow,width,height,maxWidth,maxHeight, a));

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
