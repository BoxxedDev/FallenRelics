package moth.boxxed.slainmecha.interaction;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.WaitForDataFrom;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import it.unimi.dsi.fastutil.Pair;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.block.BotRelicBlock;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.annotation.Nonnull;

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
        if (stack == null) return;
        world.sendMessage(Message.raw(stack.getItemId()));
        if (!stack.getItemId().equals("Mechanical_Heart")) return;

        Store<ChunkStore> store = world.getChunkStore().getStore();
        long chunkIndex = ChunkUtil.indexChunkFromBlock(target.getX(), target.getZ());
        BlockComponentChunk blockComponentChunk = store.getExternalData().getChunkComponent(chunkIndex, BlockComponentChunk.getComponentType());
        if (blockComponentChunk == null) return;
        int blockIndex = ChunkUtil.indexBlockInColumn(target.getX(), target.getY(), target.getZ());

        BotRelicBlock block = blockComponentChunk.getComponent(blockIndex, SlainMecha.get().getBotRelicBlockComponentType());
        if (block == null) return;

        world.sendMessage(Message.raw("Put Mechanical Heart In"));

        world.setBlock(target.getX(), target.getY(), target.getZ(), "Empty");
        cmd.run(_store -> {
            Pair<Ref<EntityStore>, INonPlayerCharacter> refPair = NPCPlugin.get().spawnNPC(
                    world.getEntityStore().getStore(),
                    block.getRelicEntity(),
                    null,
                    target.clone().toVector3d().add(0.5, 0.5, 0.5),
                    Vector3f.ZERO
            );
        });
    }

    @Override
    protected void simulateInteractWithBlock(@NonNull InteractionType var1, @NonNull InteractionContext var2, @Nullable ItemStack var3, @NonNull World var4, @NonNull Vector3i var5) {

    }
}
