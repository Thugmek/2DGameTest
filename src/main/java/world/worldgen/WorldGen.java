package world.worldgen;

import org.joml.Vector2f;
import util.Randomizers;
import world.Biome;
import world.WorldMapTile;

public interface WorldGen {
    WorldMapTile getTile(int x, int y);
}
