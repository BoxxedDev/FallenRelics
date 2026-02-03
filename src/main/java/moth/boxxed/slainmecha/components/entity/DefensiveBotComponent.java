package moth.boxxed.slainmecha.components.entity;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class DefensiveBotComponent implements Component<EntityStore> {
    public static final BuilderCodec<DefensiveBotComponent> CODEC =
            BuilderCodec.<DefensiveBotComponent>builder(DefensiveBotComponent.class, DefensiveBotComponent::new)
                    .append(
                            new KeyedCodec<>("OwnerUuid", Codec.UUID_STRING),
                            (o, i) -> o.ownerUUID = i,
                            (o) -> o.ownerUUID
                    ).add()
                    .append(
                            new KeyedCodec<>("GuardingPosition", Vector3i.CODEC),
                            (o, i) -> o.guardingPosition = i,
                            (o) -> o.guardingPosition
                    ).add()
                    .append(
                            new KeyedCodec<>("ShouldFollow", Codec.BOOLEAN),
                            (o, i) -> o.shouldFollow = i,
                            (o) -> o.shouldFollow
                    ).add()
            .build();

    @Setter @Getter private UUID ownerUUID;
    @Setter @Getter private Vector3i guardingPosition = new Vector3i();
    @Setter @Getter private boolean shouldFollow = false;

    public DefensiveBotComponent() {}

    public DefensiveBotComponent(UUID ownerUUID, Vector3i guardingPosition, boolean shouldFollow) {
        this.ownerUUID = ownerUUID;
        this.guardingPosition = guardingPosition;
        this.shouldFollow = shouldFollow;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new DefensiveBotComponent(this.ownerUUID, this.guardingPosition, this.shouldFollow);
    }
}
