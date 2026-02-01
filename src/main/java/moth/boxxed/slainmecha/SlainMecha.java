package moth.boxxed.slainmecha;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import lombok.Getter;
import moth.boxxed.slainmecha.components.block.MechanicalHeartBlock;
import moth.boxxed.slainmecha.components.entity.DefensiveBotComponent;
import moth.boxxed.slainmecha.components.entity.SorterBotComponent;
import moth.boxxed.slainmecha.interaction.PutEssenceInHeartInteraction;
import moth.boxxed.slainmecha.systems.MechanicalHeartSystems;

import java.io.ObjectInputFilter;

public class SlainMecha extends JavaPlugin {
    private static SlainMecha instance;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    @Getter private final Config<SlainMechaConfig> config = this.withConfig("SlainMechaConfig", SlainMechaConfig.CODEC);

    @Getter private final ComponentType<EntityStore, SorterBotComponent> sorterBotComponentType;
    @Getter private final ComponentType<EntityStore, DefensiveBotComponent> defensiveBotComponentType;

    @Getter private final ComponentType<ChunkStore, MechanicalHeartBlock> mechanicalHeartComponentType;

    public static SlainMecha get() {
        return instance;
    }

    public SlainMecha(JavaPluginInit init) {
        super(init);
        instance = this;
        
        this.sorterBotComponentType = this.getEntityStoreRegistry().registerComponent(SorterBotComponent.class, "SorterBot", SorterBotComponent.CODEC);
        this.defensiveBotComponentType = this.getEntityStoreRegistry().registerComponent(DefensiveBotComponent.class, "DefensiveBot", DefensiveBotComponent.CODEC);

        this.mechanicalHeartComponentType = this.getChunkStoreRegistry().registerComponent(MechanicalHeartBlock.class, "MechanicalHeart", MechanicalHeartBlock.CODEC);

        this.getChunkStoreRegistry().registerSystem(new MechanicalHeartSystems.BreakBlockEventSystem());
        this.getChunkStoreRegistry().registerSystem(new MechanicalHeartSystems.PlaceBlockEventSystem());

        this.getCodecRegistry(Interaction.CODEC).register("PutEssenceInHeart", PutEssenceInHeartInteraction.class, PutEssenceInHeartInteraction.CODEC);
    }

    @Override
    protected void setup() {
        config.save();
    }
}
