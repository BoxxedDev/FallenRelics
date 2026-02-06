package moth.boxxed.fallenrelics.relic;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

@FunctionalInterface
public interface IBotRelicComponentCreator<T extends Component<EntityStore>> {
    T operate(PlayerRef playerRef, Store<EntityStore> store, Vector3i blockPosition);
}
