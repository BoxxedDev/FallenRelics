package moth.boxxed.slainmecha.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.jspecify.annotations.NonNull;

public class BlockUtil {
    public static Ref<ChunkStore> getBlock(Vector3i blockPos, Store<ChunkStore> store) {
        long chunkIndex = ChunkUtil.indexChunkFromBlock(blockPos.getX(), blockPos.getZ());
        BlockComponentChunk blockComponentChunk = store.getExternalData().getChunkComponent(chunkIndex, BlockComponentChunk.getComponentType());
        if (blockComponentChunk == null) return null;
        int blockIndex = ChunkUtil.indexBlockInColumn(blockPos.x, blockPos.y, blockPos.z);
        return blockComponentChunk.getEntityReference(blockIndex);
    }

    public static BlockLocation getBlockLocation(Ref<ChunkStore> blockRef, Store<ChunkStore> store) {
        var info = store.getComponent(blockRef, BlockModule.BlockStateInfo.getComponentType());
        if (info == null) return null;

        var worldChunk = store.getComponent(info.getChunkRef(), WorldChunk.getComponentType());
        if (worldChunk == null) return null;

        int blockIndex = info.getIndex();

        int localX = ChunkUtil.xFromBlockInColumn(blockIndex);
        int localY = ChunkUtil.yFromBlockInColumn(blockIndex);
        int localZ = ChunkUtil.zFromBlockInColumn(blockIndex);

        int worldX = ChunkUtil.worldCoordFromLocalCoord(worldChunk.getX(), localX);
        int worldZ = ChunkUtil.worldCoordFromLocalCoord(worldChunk.getZ(), localZ);


        return new BlockLocation(
                new Vector3i(worldX, localY, worldZ),
                new Vector3i(localX, localY, localZ),
                worldChunk.getX(),
                worldChunk.getZ()
        );
    }

    public static class BlockLocation {
        private final Vector3i worldPos;
        private final Vector3i localPos;
        private final int chunkX;
        private final int chunkZ;

        public BlockLocation(Vector3i worldPos, Vector3i localPos, int chunkX, int chunkZ) {
            this.worldPos = worldPos;
            this.localPos = localPos;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        @Override
        public @NonNull String toString() {
            return String.format("World[%d, %d, %d] | Local[%d, %d, %d] | Chunk[%d, %d]",
                    worldPos.x, worldPos.y, worldPos.z,
                    localPos.x, localPos.y, localPos.z,
                    chunkX, chunkZ);
        }

        public Vector3i getWorldPos() {
            return worldPos;
        }

        public Vector3i getLocalPos() {
            return localPos;
        }

        public int getChunkX() {
            return chunkX;
        }

        public int getChunkZ() {
            return chunkZ;
        }

        public int getBlockIndex() {
            return ChunkUtil.indexBlockInColumn(localPos.x, localPos.y, localPos.z);
        }

        public long getChunkIndex() {
            return ChunkUtil.indexChunkFromBlock(localPos.x, localPos.z);
        }
    }
}
