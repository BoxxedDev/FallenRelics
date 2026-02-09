package moth.boxxed.fallenrelics.components.block.relic;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import moth.boxxed.fallenrelics.FallenRelics;
import moth.boxxed.fallenrelics.components.entity.DefensiveBotComponent;
import moth.boxxed.fallenrelics.components.entity.SorterBotComponent;

@Getter
public enum RelicType {
    EMPTY(null, null, null),
    DEFENSIVE(
            DefensiveBotComponent.class,
            FallenRelics.get().getDefensiveBotComponentType(),
            (playerRef, entityRef, store, blockPosition) -> new DefensiveBotComponent(blockPosition, false)
    ),
    SORTER(
            SorterBotComponent.class,
            FallenRelics.get().getSorterBotComponentType(),
            (playerRef, entityRef, store, blockPosition) -> new SorterBotComponent()
    )
    ;

    private final Class<? extends Component<EntityStore>> clazz;
    private final ComponentType<EntityStore, ? extends Component<EntityStore>> componentType;
    private final IBotRelicComponentCreator<? extends Component<EntityStore>> componentCreator;
    RelicType(Class<? extends Component<EntityStore>> clazz, ComponentType<EntityStore, ? extends Component<EntityStore>> componentType, IBotRelicComponentCreator<? extends Component<EntityStore>> componentCreator) {
        this.clazz = clazz;
        this.componentType = componentType;
        this.componentCreator = componentCreator;
    }
}
