package serializers;

import java.io.Serializable;
import java.nio.file.Path;

public class SaveDefinition implements Serializable {

    String name;

    public SaveDefinition(Path path){
        name = path.getFileName().toString();
        System.out.println(name);
    }

    public String getName() {
        return name;
    }
}
