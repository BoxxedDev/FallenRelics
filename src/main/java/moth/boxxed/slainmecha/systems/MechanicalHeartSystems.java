package moth.boxxed.slainmecha.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.block.MechanicalHeartBlock;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class MechanicalHeartSystems {
    public static class BreakBlockEventSystem extends EntityEventSystem<ChunkStore, BreakBlockEvent> {
        public BreakBlockEventSystem() {
            super(BreakBlockEvent.class);
        }

        @Override
        public void handle(
                int i,
                @NonNull ArchetypeChunk<ChunkStore> archetypeChunk,
                @NonNull Store<ChunkStore> store,
                @NonNull CommandBuffer<ChunkStore> cmd,
                @NonNull BreakBlockEvent event
        ) {
            MechanicalHeartBlock mechanicalHeartBlock = archetypeChunk.getComponent(i, SlainMecha.get().getMechanicalHeartComponentType());
            if (mechanicalHeartBlock == null) return;

            World world = store.getExternalData().getWorld();
            Store<EntityStore> entityStore = world.getEntityStore().getStore();
            List<ItemStack> remainder = mechanicalHeartBlock.getRemainderEssence();

            BlockModule.BlockStateInfo info = archetypeChunk.getComponent(i, BlockModule.BlockStateInfo.getComponentType());

            if (info == null) return;

            WorldChunk chunk = store.getComponent(info.getChunkRef(), WorldChunk.getComponentType());

            if (chunk == null) return;

            int localX = ChunkUtil.worldCoordFromLocalCoord(chunk.getX(), ChunkUtil.xFromBlockInColumn(i));
            int localY = ChunkUtil.yFromBlockInColumn(i);
            int localZ = ChunkUtil.worldCoordFromLocalCoord(chunk.getZ(), ChunkUtil.zFromBlockInColumn(i));

            Holder<EntityStore>[] itemHolders = ItemComponent.generateItemDrops(entityStore, remainder,
                    new Vector3d(localX, localY, localZ).add(0.5, 0.5, 0.5), Vector3f.ZERO);

            Holder<EntityStore> itemHolder = ItemComponent.generateItemDrop(entityStore, new ItemStack(
                    "Mechanical_Heart", 1, new BsonDocument("Essence", new BsonInt32(mechanicalHeartBlock.getEssence()))
            ), new Vector3d(localX, localY, localZ).add(0.5, 0.5, 0.5), Vector3f.ZERO, 0, 0, 0);

            if (itemHolders.length > 0) {
                world.execute(() -> {
                    entityStore.addEntities(itemHolders, AddReason.SPAWN);
                });
            }
        }

        @Override
        public @Nullable Query<ChunkStore> getQuery() {
            return SlainMecha.get().getMechanicalHeartComponentType();
        }
    }

    public static class PlaceBlockEventSystem extends EntityEventSystem<ChunkStore, PlaceBlockEvent> {
        public PlaceBlockEventSystem() {
            super(PlaceBlockEvent.class);
        }

        @Override
        public void handle(
                int i,
                @NonNull ArchetypeChunk<ChunkStore> archetypeChunk,
                @NonNull Store<ChunkStore> store,
                @NonNull CommandBuffer<ChunkStore> cmd,
                @NonNull PlaceBlockEvent event
        ) {
            MechanicalHeartBlock mechanicalHeartBlock = archetypeChunk.getComponent(i, SlainMecha.get().getMechanicalHeartComponentType());
            if (mechanicalHeartBlock == null) return;

            ItemStack stack = event.getItemInHand();
            BsonDocument document = stack.getMetadata();

            if (document == null) return;

            if (document.containsKey("Essence")) {
                mechanicalHeartBlock.setEssence(document.get("Essence").asInt32().getValue());
            }
        }

        @Override
        public @Nullable Query<ChunkStore> getQuery() {
            return SlainMecha.get().getMechanicalHeartComponentType();
        }
    }
}
