package moth.boxxed.slainmecha.relic;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.entity.DefensiveBotComponent;

import java.util.UUID;

@Getter
public enum RelicType {
    EMPTY(null, null, null),
    DEFENSIVE_BOT(
            DefensiveBotComponent.class,
            SlainMecha.get().getDefensiveBotComponentType(),
            (playerRef, store, blockPosition) -> {
                UUID playerUUID = playerRef.getWorldUuid();
                return new DefensiveBotComponent(playerUUID, blockPosition, false);
            }
    );

    private final Class<? extends Component<EntityStore>> clazz;
    private final ComponentType<EntityStore, ? extends Component<EntityStore>> componentType;
    private final IBotRelicComponentCreator<? extends Component<EntityStore>> componentCreator;
    RelicType(Class<? extends Component<EntityStore>> clazz, ComponentType<EntityStore, ? extends Component<EntityStore>> componentType, IBotRelicComponentCreator<? extends Component<EntityStore>> componentCreator) {
        this.clazz = clazz;
        this.componentType = componentType;
        this.componentCreator = componentCreator;
    }
}
