package moth.boxxed.fallenrelics.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import moth.boxxed.fallenrelics.FallenRelics;
import moth.boxxed.fallenrelics.components.entity.BaseRelicComponent;
import moth.boxxed.fallenrelics.components.entity.DefensiveBotComponent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class DefensiveBotSystems {
    public static class DamageSystem extends DamageEventSystem {
        @Override
        public void handle(
                int i,
                @NotNull ArchetypeChunk<EntityStore> archetypeChunk,
                @NotNull Store<EntityStore> store,
                @NotNull CommandBuffer<EntityStore> commandBuffer,
                @NotNull Damage damage
        ) {
            Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
            NPCEntity npc = store.getComponent(ref, Objects.requireNonNull(NPCEntity.getComponentType()));
            EntityStatMap statMap = store.getComponent(ref, EntityStatMap.getComponentType());
            DefensiveBotComponent defensiveBot = store.getComponent(ref, FallenRelics.get().getDefensiveBotComponentType());
            BaseRelicComponent baseRelic = store.getComponent(ref, FallenRelics.get().getBaseRelicComponentType());

            if (npc == null || statMap == null || defensiveBot == null || baseRelic == null) return;

            EntityStatValue statValue = statMap.get(DefaultEntityStatTypes.getHealth());
            if (statValue == null) return;

            float healthDiff = statValue.getMax()-damage.getAmount();
            statMap.maximizeStatValue(DefaultEntityStatTypes.getHealth());


        }

        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return Query.and(
                    FallenRelics.get().getDefensiveBotComponentType(),
                    FallenRelics.get().getBaseRelicComponentType()
            );
        }
    }

    public static class RefChangeSystem extends com.hypixel.hytale.component.system.RefChangeSystem<EntityStore, DefensiveBotComponent> {
        @Override
        public @Nullable Query<EntityStore> getQuery() {
            return FallenRelics.get().getDefensiveBotComponentType();
        }

        @Override
        public @NotNull ComponentType<EntityStore, DefensiveBotComponent> componentType() {
            return FallenRelics.get().getDefensiveBotComponentType();
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
}
