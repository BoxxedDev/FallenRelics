package moth.boxxed.slainmecha.systems;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.block.MechanicalHeartBlock;
import moth.boxxed.slainmecha.resources.MechanicalHeartPlaceMap;
import moth.boxxed.slainmecha.util.BlockUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MechanicalHeartSystems {
    public static class BreakBlockEventSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {
        public BreakBlockEventSystem() {
            super(BreakBlockEvent.class);
        }

        @Override
        public void handle(
                int i,
                @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                @NonNull Store<EntityStore> store,
                @NonNull CommandBuffer<EntityStore> cmd,
                @NonNull BreakBlockEvent event
        ) {
            World world = store.getExternalData().getWorld();
            Store<ChunkStore> chunkStore = world.getChunkStore().getStore();
            Ref<ChunkStore> ref = BlockUtil.getBlock(event.getTargetBlock(), chunkStore);
            if (ref == null) return;

            MechanicalHeartBlock mechanicalHeart = chunkStore.getComponent(ref, SlainMecha.get().getMechanicalHeartComponentType());
            if (mechanicalHeart == null) return;

            ItemStack stack = new ItemStack("Mechanical_Heart", 1);
            stack = stack.withMetadata(MechanicalHeartBlock.ITEM_KEYED_CODEC, mechanicalHeart.getEssence());

            Holder<EntityStore> itemDrop = ItemComponent.generateItemDrop(
                    store, stack, event.getTargetBlock().toVector3d().add(0.5, 0.5, 0.5), Vector3f.ZERO, 0, 0, 0
            );
            world.execute(() -> {
                store.addEntity(itemDrop, AddReason.SPAWN);
            });
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return Archetype.empty();
        }
    }

    public static class PlaceBlockEventSystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {
        public PlaceBlockEventSystem() {
            super(PlaceBlockEvent.class);
        }

        @Override
        public void handle(
                int i,
                @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
                @NonNull Store<EntityStore> store,
                @NonNull CommandBuffer<EntityStore> cmd,
                @NonNull PlaceBlockEvent event
        ) {
            ItemStack stack = event.getItemInHand();
            if (stack == null) return;
            if (!stack.getItemId().equals("Mechanical_Heart")) return;

            World world = store.getExternalData().getWorld();
            Store<ChunkStore> chunkStore = world.getChunkStore().getStore();
            Vector3i target = event.getTargetBlock();
            Integer essence = stack.getFromMetadataOrNull(MechanicalHeartBlock.ITEM_KEYED_CODEC);
            if (essence == null) return;
            chunkStore.getResource(SlainMecha.get().getHeartPlaceMapResourceType()).addToMap(target, essence);
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return Archetype.empty();
        }
    }

    public static class RefSystem extends com.hypixel.hytale.component.system.RefSystem<ChunkStore> {
        @Override
        public void onEntityAdded(
                @NonNull Ref<ChunkStore> ref,
                @NonNull AddReason addReason,
                @NonNull Store<ChunkStore> store,
                @NonNull CommandBuffer<ChunkStore> cmd) {
            World world = store.getExternalData().getWorld();
            BlockUtil.BlockLocation location = BlockUtil.getBlockLocation(ref, store);
            if (location == null) return;

            MechanicalHeartPlaceMap map = store.getResource(SlainMecha.get().getHeartPlaceMapResourceType());
            int essence = map.getMap().getOrDefault(location.getWorldPos(), 0);

            world.execute(() -> {
                store.replaceComponent(ref, SlainMecha.get().getMechanicalHeartComponentType(), new MechanicalHeartBlock(essence));
                map.getMap().remove(location.getWorldPos(), essence);
            });
        }

        @Override
        public void onEntityRemove(@NonNull Ref<ChunkStore> var1, @NonNull RemoveReason var2, @NonNull Store<ChunkStore> var3, @NonNull CommandBuffer<ChunkStore> var4) {

        }

        @Override
        public @Nullable Query<ChunkStore> getQuery() {
            return SlainMecha.get().getMechanicalHeartComponentType();
        }
    }
}
