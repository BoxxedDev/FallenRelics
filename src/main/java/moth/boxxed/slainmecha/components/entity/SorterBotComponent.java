package moth.boxxed.slainmecha.components.entity;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SorterBotComponent implements Component<EntityStore> {
    public static final BuilderCodec<SorterBotComponent> CODEC =
            BuilderCodec.<SorterBotComponent>builder(SorterBotComponent.class, SorterBotComponent::new)
                    .append(new KeyedCodec<>("SortSources", new ArrayCodec<>(Vector3i.CODEC, Vector3i[]::new)),
                            (o, i) -> o.sortSources = new ArrayList<>(Arrays.asList(i)),
                            (o) -> o.sortSources.toArray(Vector3i[]::new)).add()
                    .append(new KeyedCodec<>("SortDestinations", new MapCodec<>(Vector3i.CODEC, HashMap::new, false)),
                            (o, i) -> o.sortDestinations = i,
                            (o) -> o.sortDestinations).add()
                    .build();

    //Persistent
    @Setter @Getter private List<Vector3i> sortSources = new ArrayList<>();
    @Setter @Getter private Map<String, Vector3i> sortDestinations = new HashMap<>();

    //Non Persistent
    @Setter @Getter private Queue<Pair<String, Vector3i>> sortDestinationQueue = new ArrayDeque<>();

    public SorterBotComponent() {}

    public SorterBotComponent(List<Vector3i> sortSources, Map<String, Vector3i> sortDestinations) {
        this.sortSources = sortSources;
        this.sortDestinations = sortDestinations;
    }

    public boolean addSortDestination(String item, Vector3i position) {
        return this.sortDestinationQueue.add(Pair.of(item, position));
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new SorterBotComponent(this.sortSources, this.sortDestinations);
    }
}
