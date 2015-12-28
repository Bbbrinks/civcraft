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
        List<Chunk> worldEdgeChunks = worldManager.getWorld().getChunks().stream().filter(c -> c.getNeighbours().size() != 6).collect(Collectors.toList());
        for (Chunk chunk : worldEdgeChunks) {
            for (Face face : Face.values()) {
                Chunk neighbour = chunk.getNeighbour(face);
                if (neighbour == null) {
                    faceFiltering(unoptimizedVoxels, chunk, face);
                    edgeFiltering(unoptimizedVoxels, chunk, face);
                }
            }
        }
        return unoptimizedVoxels;
    }

    private void faceFiltering(List<Voxel> unoptimizedVoxels, Chunk chunk, Face face) {
        if (!face.equals(Face.TOP)) {
            Stream<Voxel> stream = chunk.getFaceVoxels(face).stream();
            stream.filter(voxel -> voxel.getNeighbours().size() == 5 ||
                    // Face x y
                    ((voxel.findLocalChunkTranslation().x == 0 || voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().y == 0 || voxel.findLocalChunkTranslation().y == World.CHUNK_SIZE - 1) && voxel.getNeighbours().size() == 5) ||
                    // Face y z
                    ((voxel.findLocalChunkTranslation().y == 0 || voxel.findLocalChunkTranslation().y == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().z == 0 || voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && voxel.getNeighbours().size() == 5) ||
                    // Face x z
                    ((voxel.findLocalChunkTranslation().z == 0 || voxel.findLocalChunkTranslation().z == World.CHUNK_SIZE - 1) && (voxel.findLocalChunkTranslation().x == 0 || voxel.findLocalChunkTranslation().x == World.CHUNK_SIZE - 1) && voxel.getNeighbours().size() == 5)
            ).forEach(unoptimizedVoxels::remove);
        }
    }

    private void edgeFiltering(List<Voxel> unoptimizedVoxels, Chunk chunk, Face face) {
        if (Face.BOTTOM.equals(face)) {
            Chunk leftNeighbour = chunk.getNeighbour(Face.LEFT);
            Chunk rightNeighbour = chunk.getNeighbour(Face.RIGHT);
            if (leftNeighbour == null || rightNeighbour == null) {
                chunk.getFaceVoxels(face).stream().filter(voxel -> voxel.getNeighbours().size() == 4 &&
                        (voxel.getY() == chunk.getY() &&
                                ((voxel.getX() == chunk.getX() && leftNeighbour == null) || (voxel.getX() == chunk.getX() + World.CHUNK_SIZE - 1 && rightNeighbour == null)))
                ).forEach(unoptimizedVoxels::remove);
            }
            Chunk frontNeighbour = chunk.getNeighbour(Face.FRONT);
            Chunk backNeighbour = chunk.getNeighbour(Face.BACK);
            if (frontNeighbour == null || backNeighbour == null) {
                chunk.getFaceVoxels(face).stream().filter(voxel -> voxel.getNeighbours().size() == 4 &&
                        (voxel.getY() == chunk.getY() &&
                                ((voxel.getZ() == chunk.getZ() && frontNeighbour == null) || (voxel.getZ() == chunk.getZ() + World.CHUNK_SIZE - 1 && backNeighbour == null)))
                ).forEach(unoptimizedVoxels::remove);
            }
        }
        if (Face.FRONT.equals(face)) {
            Chunk leftNeighbour = chunk.getNeighbour(Face.LEFT);
            Chunk rightNeighbour = chunk.getNeighbour(Face.RIGHT);
            if (leftNeighbour == null || rightNeighbour == null) {
                chunk.getFaceVoxels(face).stream().filter(voxel -> voxel.getNeighbours().size() == 4 &&
                        (voxel.getZ() == chunk.getZ() &&
                                ((voxel.getX() == chunk.getX() && leftNeighbour == null) || (voxel.getX() == chunk.getX() + World.CHUNK_SIZE - 1 && rightNeighbour == null)))
                ).forEach(unoptimizedVoxels::remove);
            }
        }

        if (Face.BACK.equals(face)) {
            Chunk leftNeighbour = chunk.getNeighbour(Face.LEFT);
            Chunk rightNeighbour = chunk.getNeighbour(Face.RIGHT);
            if (leftNeighbour == null || rightNeighbour == null) {
                chunk.getFaceVoxels(face).stream().filter(voxel -> voxel.getNeighbours().size() == 4 &&
                        (voxel.getZ() == chunk.getZ() + World.CHUNK_SIZE - 1 &&
                                ((voxel.getX() == chunk.getX() && leftNeighbour == null) || (voxel.getX() == chunk.getX() + World.CHUNK_SIZE - 1 && rightNeighbour == null)))
                ).forEach(unoptimizedVoxels::remove);
            }
        }
    }


}