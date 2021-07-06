package serializers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SWorldMapChunksRow implements Serializable {
    private List<SWorldMapChunk> chunks = new ArrayList<>();

    private int pos;

    public SWorldMapChunksRow(int pos) {
        this.pos = pos;
    }

    public List<SWorldMapChunk> getChunks() {
        return chunks;
    }

    public int getPos() {
        return pos;
    }
}
