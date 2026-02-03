package moth.boxxed.slainmecha.components.block;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.codec.PairCodec;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.entity.DefensiveBotComponent;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class BotRelicBlock implements Component<ChunkStore> {
    public static final BuilderCodec<BotRelicBlock> CODEC =
            BuilderCodec.<BotRelicBlock>builder(BotRelicBlock.class, BotRelicBlock::new)
                    .append(new KeyedCodec<>("RelicEntity", Codec.STRING), (o, i) -> o.relicEntity = i, (o) -> o.relicEntity).add()
                    .append(new KeyedCodec<>("RelicType", new EnumCodec<>(RelicType.class)), (o, i) -> o.relicType = i, (o) -> o.relicType).add()
                    .build();

    @Getter @Setter private String relicEntity = "";
    @Getter @Setter private RelicType relicType = RelicType.EMPTY;

    public BotRelicBlock() {
    }

    public BotRelicBlock(String relicEntity, RelicType type) {
        this.relicEntity =relicEntity;
        this.relicType = type;
    }

    @Override
    public @Nullable Component<ChunkStore> clone() {
        return new BotRelicBlock(this.relicEntity, this.relicType);
    }

    @Getter
    public enum RelicType {
        EMPTY(null, null),
        DEFENSIVE_BOT(
                SlainMecha.get().getDefensiveBotComponentType(),
                (playerRef, store, blockPosition) -> {
                    UUID playerUUID = playerRef.getWorldUuid();
                    return new DefensiveBotComponent(playerUUID, blockPosition, false);
                }
                );

        private final ComponentType<EntityStore, ? extends Component<EntityStore>> componentType;
        private final IBotRelicComponentCreator<Component<EntityStore>> componentCreator;
        RelicType(ComponentType<EntityStore, ? extends Component<EntityStore>> componentType, IBotRelicComponentCreator<Component<EntityStore>> componentCreator) {
            this.componentType = componentType;
            this.componentCreator = componentCreator;
        }

    }
}
