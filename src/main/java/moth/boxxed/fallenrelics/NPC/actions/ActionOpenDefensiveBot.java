package moth.boxxed.fallenrelics.NPC.actions;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import moth.boxxed.fallenrelics.FallenRelics;
import moth.boxxed.fallenrelics.components.entity.DefensiveBotComponent;
import org.jspecify.annotations.NonNull;

public class ActionOpenDefensiveBot extends ActionBase {
    public ActionOpenDefensiveBot(@NonNull BuilderActionBase builderActionBase, BuilderSupport support) {
        super(builderActionBase);
    }

    @Override
    public boolean canExecute(@NonNull Ref<EntityStore> ref, @NonNull Role role, InfoProvider sensorInfo, double dt, @NonNull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store) &&
                role.getStateSupport().getInteractionIterationTarget() != null;
    }

    @Override
    public boolean execute(@NonNull Ref<EntityStore> ref, @NonNull Role role, InfoProvider sensorInfo, double dt, @NonNull Store<EntityStore> store) {
        super.execute(ref, role, sensorInfo, dt, store);
        Ref<EntityStore> playerReference = role.getStateSupport().getInteractionIterationTarget();

        if (playerReference == null) return false;

        PlayerRef playerRefComponent = store.getComponent(playerReference, PlayerRef.getComponentType());
        if (playerRefComponent == null) return false;

        Player player = store.getComponent(playerReference, Player.getComponentType());
        if (player == null) return false;

        player.sendMessage(Message.raw("AWAWAWA"));

        DefensiveBotComponent defensiveBot = store.getComponent(ref, FallenRelics.get().getDefensiveBotComponentType());
        if (defensiveBot == null) return false;

        player.sendMessage(Message.raw(String.valueOf(defensiveBot.getOwnerUUID())));
        return true;
    }
}
