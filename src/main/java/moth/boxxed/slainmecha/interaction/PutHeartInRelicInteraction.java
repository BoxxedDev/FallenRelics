package moth.boxxed.slainmecha.interaction;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.WaitForDataFrom;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
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
    protected void interactWithBlock(@NonNull World var1, @NonNull CommandBuffer<EntityStore> var2, @NonNull InteractionType var3, @NonNull InteractionContext var4, @Nullable ItemStack var5, @NonNull Vector3i var6, @NonNull CooldownHandler var7) {

    }

    @Override
    protected void simulateInteractWithBlock(@NonNull InteractionType var1, @NonNull InteractionContext var2, @Nullable ItemStack var3, @NonNull World var4, @NonNull Vector3i var5) {

    }
}
