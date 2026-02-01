package moth.boxxed.slainmecha.components.entity;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

public class DefensiveBotComponent implements Component<EntityStore> {
    public static final BuilderCodec<DefensiveBotComponent> CODEC = BuilderCodec.<DefensiveBotComponent>builder(
            DefensiveBotComponent.class, DefensiveBotComponent::new
    ).build();

    @Setter @Getter private Vector3i guardingPosition = new Vector3i();

    public DefensiveBotComponent() {}

    @Override
    public @Nullable Component<EntityStore> clone() {
        return null;
    }
}
