package nl.civcraft.core.rendering;

import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Bob on 26-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class WorldEdgeVoxelFilter implements RenderedVoxelFilter {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private WorldManager worldManager;

    @Override
    public List<Voxel> filter(List<Voxel> unoptimizedVoxels) {
        List<Chunk> worldEdgeChunks = worldManager.getWorld().getChunks().stream().filter(c -> c.getNeighbours().size() != 4).collect(Collectors.toList());
        for (Chunk chunk : worldEdgeChunks) {
            if (chunk.getNeighbours().size() == 3) {
                for (Face face : Face.values()) {
                    basicEdgeFiltering(unoptimizedVoxels, chunk, face);
                }
            }
            if (chunk.getNeighbours().size() == 2) {
                for (Face face : Face.values()) {
                    Chunk neighbour = chunk.getNeighbour(face);
                    basicEdgeFiltering(unoptimizedVoxels, chunk, face);
                    if (neighbour == null) {
                        Stream<Voxel> stream = chunk.getFaceVoxels(face).stream();
                        stream.filter(voxel ->
                                // Edge y front left
                                ((voxel.findLocalChunkTranslation().z == 0) && (voxel.findLocalChunkTranslation().x == 0) && face.equals(Face.FRONT) && voxel.getNeighbours().size() == 4) ||
                                        // Edge y back left
                                        ((voxel.findLocalChunkTranslation().z == 0) && (voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && face.equals(Face.FRONT) && voxel.getNeighbours().size() == 4) ||
                                        // Edge y front right
                                        ((voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().x == 0) && face.equals(Face.BACK) && voxel.getNeighbours().size() == 4) ||
                                        // Edge y back right
                                        ((voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && face.equals(Face.BACK) && voxel.getNeighbours().size() == 4)

                        ).forEach(unoptimizedVoxels::remove);
                    }
                }
            }
        }
        return unoptimizedVoxels;
    }

    private void basicEdgeFiltering(List<Voxel> unoptimizedVoxels, Chunk chunk, Face face) {
        Chunk neighbour = chunk.getNeighbour(face);
        if (neighbour == null) {
            Stream<Voxel> stream = chunk.getFaceVoxels(face).stream();
            stream.filter(voxel -> voxel.getNeighbours().size() == 5 ||
                    // Face x y
                    ((voxel.findLocalChunkTranslation().x == 0 || voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().y == 0 || voxel.findLocalChunkTranslation().y == World.CHUNK_SIZE - 1) && voxel.getNeighbours().size() == 5) ||
                    // Face y z
                    ((voxel.findLocalChunkTranslation().y == 0 || voxel.findLocalChunkTranslation().y == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().z == 0 || voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && voxel.getNeighbours().size() == 5) ||
                    // Face x z
                    ((voxel.findLocalChunkTranslation().z == 0 || voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().x == 0 || voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && voxel.getNeighbours().size() == 5) ||
                    // Edge x
                    ((voxel.findLocalChunkTranslation().x == 0 || voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().y == 0) && voxel.getNeighbours().size() == 4) ||
                    // Edge z
                    ((voxel.findLocalChunkTranslation().z == 0 || voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().y == 0) && voxel.getNeighbours().size() == 4) ||
                    //Corners
                    ((voxel.findLocalChunkTranslation().x == 0 || voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && ((voxel.findLocalChunkTranslation().z == 0 || voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().y == 0 || voxel.findLocalChunkTranslation().y == World.CHUNK_SIZE - 1) && voxel.getNeighbours().size() == 3))
            ).forEach(unoptimizedVoxels::remove);
        }
    }


}
