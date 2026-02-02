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
import com.hypixel.hytale.server.core.modules.entity.system.SnapshotSystems;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.block.MechanicalHeartBlock;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonValue;
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
            Store<ChunkStore> chunkStore = store.getExternalData().getWorld().getChunkStore().getStore();
            Vector3i target = event.getTargetBlock();
            long chunkIndex = ChunkUtil.indexChunkFromBlock(target.getX(), target.getZ());
            BlockComponentChunk blockComponentChunk = chunkStore.getExternalData().getChunkComponent(chunkIndex, BlockComponentChunk.getComponentType());
            if (blockComponentChunk == null) return;
            int blockIndex = ChunkUtil.indexBlockInColumn(target.getX(), target.getY(), target.getZ());
            MechanicalHeartBlock heart = blockComponentChunk.getComponent(blockIndex, SlainMecha.get().getMechanicalHeartComponentType());
            if (heart == null) return;

            World world = store.getExternalData().getWorld();
            Store<EntityStore> entityStore = world.getEntityStore().getStore();
            List<ItemStack> remainder = heart.getRemainderEssence();

            BlockModule.BlockStateInfo info = blockComponentChunk.getComponent(blockIndex, BlockModule.BlockStateInfo.getComponentType());

            if (info == null) return;

            Holder<EntityStore>[] itemHolders = ItemComponent.generateItemDrops(entityStore, remainder,
                    new Vector3d(target.getX(), target.getY(), target.getZ()).add(0.5, 0.5, 0.5), Vector3f.ZERO);

            ItemStack stack = new ItemStack("Mechanical_Heart", 1);
            Holder<EntityStore> heartHolder = ItemComponent.generateItemDrop(entityStore, stack.withMetadata(
                    new KeyedCodec<>("Essence", Codec.INTEGER), heart.getEssence()
                    ),
                    new Vector3d(target.getX(), target.getY(), target.getZ()).add(0.5, 0.5, 0.5), Vector3f.ZERO, 0, 0, 0);

            world.sendMessage(Message.raw("Broke heart"));

            if (itemHolders.length > 0) {
                world.execute(() -> {
                    entityStore.addEntities(itemHolders, AddReason.SPAWN);
                    entityStore.addEntity(heartHolder, AddReason.SPAWN);
                });
            }

            heart.setRemainderEssence(new ArrayList<>());
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
            store.getExternalData().getWorld().sendMessage(Message.raw(String.valueOf(stack.getFromMetadataOrNull(new KeyedCodec<>("Essence", Codec.INTEGER)))));

            Store<ChunkStore> chunkStore = store.getExternalData().getWorld().getChunkStore().getStore();
            Vector3i target = event.getTargetBlock();
            store.getExternalData().getWorld().sendMessage(Message.raw("Place Heart"));
            long chunkIndex = ChunkUtil.indexChunkFromBlock(target.getX(), target.getZ());
            BlockComponentChunk blockComponentChunk = chunkStore.getExternalData().getChunkComponent(chunkIndex, BlockComponentChunk.getComponentType());
            if (blockComponentChunk == null) return;
            int blockIndex = ChunkUtil.indexBlockInColumn(target.getX(), target.getY(), target.getZ());
            store.getExternalData().getWorld().sendMessage(Message.raw(String.valueOf(blockIndex)));
            MechanicalHeartBlock heart = blockComponentChunk.getComponent(blockIndex, SlainMecha.get().getMechanicalHeartComponentType());
            if (heart == null) return;

            store.getExternalData().getWorld().sendMessage(Message.raw(String.valueOf(heart.getEssence())));
            Integer essence = stack.getFromMetadataOrNull(new KeyedCodec<>("Essence", Codec.INTEGER));
            if (essence == null) return;
            store.getExternalData().getWorld().sendMessage(Message.raw(String.valueOf(essence)));
            heart.setEssence(essence);
        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return Archetype.empty();
        }
    }
}
