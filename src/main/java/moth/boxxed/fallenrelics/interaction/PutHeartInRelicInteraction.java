package moth.boxxed.fallenrelics.interaction;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.WaitForDataFrom;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import it.unimi.dsi.fastutil.Pair;
import moth.boxxed.fallenrelics.FallenRelics;
import moth.boxxed.fallenrelics.components.block.MechanicalHeartBlock;
import moth.boxxed.fallenrelics.components.entity.BaseRelicComponent;
import moth.boxxed.fallenrelics.components.block.relic.BotRelicBlock;
import moth.boxxed.fallenrelics.components.block.relic.RelicType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;
import java.awt.*;

public class PutHeartInRelicInteraction extends SimpleBlockInteraction {
    @Nonnull
    public static final BuilderCodec<PutHeartInRelicInteraction> CODEC =
            BuilderCodec.builder(PutHeartInRelicInteraction.class, PutHeartInRelicInteraction::new, SimpleBlockInteraction.CODEC)
                    .build();

    @Nonnull
    public WaitForDataFrom getWaitForDataFrom() {
        return WaitForDataFrom.Client;
    }

    @Override
    protected void interactWithBlock(
            @NonNull World world,
            @NonNull CommandBuffer<EntityStore> cmd,
            @NonNull InteractionType interactionType,
            @NonNull InteractionContext context,
            @Nullable ItemStack stack,
            @NonNull Vector3i target,
            @NonNull CooldownHandler cooldownHandler) {
        Store<EntityStore> entityStore = world.getEntityStore().getStore();
        Ref<EntityStore> playerRef = context.getEntity();
        PlayerRef playerRefComponent = entityStore.getComponent(playerRef, PlayerRef.getComponentType());
        if (playerRefComponent == null) return;
        Player player = entityStore.getComponent(playerRef, Player.getComponentType());
        if (player == null) return;

        if (stack == null) return;
        if (!stack.getItemId().equals("Mechanical_Heart")) return;

        Integer essence = stack.getFromMetadataOrNull(MechanicalHeartBlock.ITEM_KEYED_CODEC);
        if (essence == null || essence == 0) {
            player.sendMessage(Message.translation("server.general.notify.empty_heart").color(new Color(255, 101, 101,220)).italic(true));
            return;
        }

        Inventory inventory = player.getInventory();
        ItemContainer storageContainer = inventory.getStorage();

        Store<ChunkStore> store = world.getChunkStore().getStore();
        long chunkIndex = ChunkUtil.indexChunkFromBlock(target.getX(), target.getZ());
        BlockComponentChunk blockComponentChunk = store.getExternalData().getChunkComponent(chunkIndex, BlockComponentChunk.getComponentType());
        if (blockComponentChunk == null) return;
        int blockIndex = ChunkUtil.indexBlockInColumn(target.getX(), target.getY(), target.getZ());

        BotRelicBlock relic = blockComponentChunk.getComponent(blockIndex, FallenRelics.get().getBotRelicBlockComponentType());
        if (relic == null) return;

        world.execute(() -> {
            if (relic.getRelicType().equals(RelicType.EMPTY)) return;

            Pair<Ref<EntityStore>, INonPlayerCharacter> refPair = NPCPlugin.get().spawnNPC(
                    world.getEntityStore().getStore(),
                    relic.getRelicEntity(),
                    null,
                    target.clone().toVector3d().add(0.5, 0.5, 0.5).add(relic.getSpawnOffset()),
                    new Vector3f(0, 0, 0)
            );

            if (refPair == null) return;

            Ref<EntityStore> ref = refPair.first();

            cmd.getStore().putComponent(
                    ref,
                    ComponentType.class.cast(relic.getRelicType().getComponentType()),
                    relic.getRelicType().getComponentCreator().operate(playerRef, ref, entityStore, target)
            );
            cmd.getStore().putComponent(
                    ref,
                    FallenRelics.get().getBaseRelicComponentType(),
                    new BaseRelicComponent(playerRefComponent.getUuid(), (float) essence, stack)
            );
            world.setBlock(target.getX(), target.getY(), target.getZ(), "Empty");
            storageContainer.removeItemStackFromSlot(inventory.getActiveHotbarSlot());
        });
    }

    @Override
    protected void simulateInteractWithBlock(@NonNull InteractionType var1, @NonNull InteractionContext var2, @Nullable ItemStack var3, @NonNull World var4, @NonNull Vector3i var5) {

    }
}
