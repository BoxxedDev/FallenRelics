package moth.boxxed.slainmecha.interaction;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.block.MechanicalHeartBlock;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PutEssenceInHeartInteraction extends SimpleBlockInteraction {
    @Override
    protected void interactWithBlock(@NonNull World world, @NonNull CommandBuffer<EntityStore> cmd,
                                     @NonNull InteractionType interactionType, @NonNull InteractionContext interactionCtx,
                                     @Nullable ItemStack stack, @NonNull Vector3i target, @NonNull CooldownHandler cooldownHandler) {
        if (stack == null) return;
        if (!(stack.getItemId().equals("Ingredient_Life_Essence") || stack.getItemId().equals("Ingredient_Life_Essence_Concentrated"))) return;

        Store<ChunkStore> store = world.getChunkStore().getStore();

        long chunkIndex = ChunkUtil.indexChunkFromBlock(target.getX(), target.getZ());
        BlockComponentChunk blockComponentChunk = store.getExternalData().getChunkComponent(chunkIndex, BlockComponentChunk.getComponentType());
        if (blockComponentChunk == null) return;
        int blockIndex = ChunkUtil.indexBlockInColumn(target.getX(), target.getY(), target.getZ());
        MechanicalHeartBlock heart = blockComponentChunk.getComponent(blockIndex, SlainMecha.get().getMechanicalHeartComponentType());
        if (heart == null) return;
        heart.addEssence(stack);
    }

    @Override
    protected void simulateInteractWithBlock(@NonNull InteractionType interactionType, @NonNull InteractionContext interactionCtx,
                                             @Nullable ItemStack stack, @NonNull World world, @NonNull Vector3i target) {

    }
}
