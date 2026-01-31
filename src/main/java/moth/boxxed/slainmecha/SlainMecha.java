package moth.boxxed.slainmecha;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import moth.boxxed.slainmecha.components.entity.SorterBotComponent;

public class SlainMecha extends JavaPlugin {
    private static SlainMecha instance;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private ComponentType<EntityStore, SorterBotComponent> sorterBotComponentType;
    
    public static final SlainMecha get() {
        return instance;
    }

    public SlainMecha(JavaPluginInit init) {
        super(init);
        instance = this;
        
        this.sorterBotComponentType = this.getEntityStoreRegistry().registerComponent(SorterBotComponent.class, "SorterBot", SorterBotComponent.CODEC);
    }

    @Override
    protected void setup() {
    }
}
