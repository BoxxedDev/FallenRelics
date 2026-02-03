package moth.boxxed.slainmecha.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.entity.DefensiveBotComponent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DefensiveBotSystem extends RefChangeSystem<EntityStore, DefensiveBotComponent> {
    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return SlainMecha.get().getDefensiveBotComponentType();
    }

    @Override
    public @NotNull ComponentType<EntityStore, DefensiveBotComponent> componentType() {
        return SlainMecha.get().getDefensiveBotComponentType();
    }

    @Override
    public void onComponentAdded(@NotNull Ref<EntityStore> ref, @NotNull DefensiveBotComponent defensiveBotComponent, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        store.getExternalData().getWorld().sendMessage(Message.raw("Added"));
    }

    @Override
    public void onComponentSet(@NotNull Ref<EntityStore> ref, @org.jetbrains.annotations.Nullable DefensiveBotComponent defensiveBotComponent, @NotNull DefensiveBotComponent t1, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        store.getExternalData().getWorld().sendMessage(Message.raw("Set"));
    }

    @Override
    public void onComponentRemoved(@NotNull Ref<EntityStore> ref, @NotNull DefensiveBotComponent defensiveBotComponent, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        store.getExternalData().getWorld().sendMessage(Message.raw("Removed"));
    }
}
