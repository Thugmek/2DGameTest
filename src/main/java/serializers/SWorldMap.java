package serializers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SWorldMap implements Serializable {
    private List<SWorldMapChunksRow> chunkRows = new ArrayList<>();

    public List<SWorldMapChunksRow> getChunkRows() {
        return chunkRows;
    }
}
