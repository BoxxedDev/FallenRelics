package moth.boxxed.slainmecha.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import moth.boxxed.slainmecha.SlainMecha;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DefensiveBotSystem extends EntityTickingSystem<EntityStore> {
    @Override
    public void tick(float dt, int i, @NonNull ArchetypeChunk<EntityStore> chunk, @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> cmd) {
        store.getExternalData().getWorld().sendMessage(Message.raw("WAWAWA"));
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return SlainMecha.get().getDefensiveBotComponentType();
    }
}
