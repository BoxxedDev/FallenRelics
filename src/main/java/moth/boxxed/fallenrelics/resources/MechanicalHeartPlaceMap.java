package moth.boxxed.fallenrelics.resources;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.Object2IntMapCodec;
import com.hypixel.hytale.component.Resource;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

public class MechanicalHeartPlaceMap implements Resource<ChunkStore> {
    public static final BuilderCodec<MechanicalHeartPlaceMap> CODEC =
            BuilderCodec.<MechanicalHeartPlaceMap>builder(MechanicalHeartPlaceMap.class, MechanicalHeartPlaceMap::new)
                    .append(
                            new KeyedCodec<>("Map", new Object2IntMapCodec<>(
                                    Vector3i.CODEC, Object2IntOpenHashMap::new, false
                            )
                            ),
                            (o, i) -> o.map = i,
                            (o) -> o.map
                    ).add()
                    .build();

    @Getter private Object2IntMap<Vector3i> map = new Object2IntOpenHashMap<>();

    public MechanicalHeartPlaceMap() {}

    public MechanicalHeartPlaceMap(Object2IntMap<Vector3i> map) {
        this.map = map;
    }

    public void addToMap(Vector3i position, int val) {
        this.map.put(position, val);
    }

    @Override
    public @Nullable Resource<ChunkStore> clone() {
        return new MechanicalHeartPlaceMap(this.map);
    }
}
