package serializers;

import java.util.ArrayList;
import java.util.List;

public class SWorldMapChunksRow {
    private List<SWorldMapChunk> chunks = new ArrayList<>();

    private int pos;

    public SWorldMapChunksRow(int pos) {
        this.pos = pos;
    }

    public List<SWorldMapChunk> getChunks() {
        return chunks;
    }
}
