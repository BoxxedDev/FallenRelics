package moth.boxxed.slainmecha.relic;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.components.entity.DefensiveBotComponent;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class BotRelicBlock implements Component<ChunkStore> {
    public static final BuilderCodec<BotRelicBlock> CODEC =
            BuilderCodec.<BotRelicBlock>builder(BotRelicBlock.class, BotRelicBlock::new)
                    .append(new KeyedCodec<>("RelicEntity", Codec.STRING), (o, i) -> o.relicEntity = i, (o) -> o.relicEntity).add()
                    .append(new KeyedCodec<>("RelicType", new EnumCodec<>(RelicType.class)), (o, i) -> o.relicType = i, (o) -> o.relicType).add()
                    .append(new KeyedCodec<>("SpawnOffset", Vector3d.CODEC), (o, i) -> o.spawnOffset = i, (o) -> o.spawnOffset).add()
                    .build();

    @Getter @Setter private String relicEntity = "";
    @Getter @Setter private RelicType relicType = RelicType.EMPTY;
    @Getter @Setter private Vector3d spawnOffset = Vector3d.ZERO;

    public BotRelicBlock() {
    }

    public BotRelicBlock(String relicEntity, RelicType type, Vector3d spawnOffset) {
        this.relicEntity = relicEntity;
        this.relicType = type;
        this.spawnOffset = spawnOffset;
    }

    @Override
    public @Nullable Component<ChunkStore> clone() {
        return new BotRelicBlock(this.relicEntity, this.relicType, this.spawnOffset);
    }
}
