package resources;

public class TextureDefinition {
    private String name;
    private String path;

    public TextureDefinition(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
