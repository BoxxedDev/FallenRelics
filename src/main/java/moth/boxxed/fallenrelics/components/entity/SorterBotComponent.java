package moth.boxxed.fallenrelics.components.entity;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.Nullable;

public class SorterBotComponent implements Component<EntityStore> {
    public static final BuilderCodec<SorterBotComponent> CODEC =
            BuilderCodec.<SorterBotComponent>builder(SorterBotComponent.class, SorterBotComponent::new)
                    .build();

    public SorterBotComponent() {}

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new SorterBotComponent();
    }
}
