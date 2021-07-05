package serializers;

import java.util.ArrayList;
import java.util.List;

public class SWorldMap {
    private List<SWorldMapChunksRow> chunkRows = new ArrayList<>();

    public List<SWorldMapChunksRow> getChunkRows() {
        return chunkRows;
    }
}
