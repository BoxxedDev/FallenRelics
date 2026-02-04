package moth.boxxed.slainmecha;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.ResourceType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.hypixel.hytale.server.npc.NPCPlugin;
import lombok.Getter;
import moth.boxxed.slainmecha.NPC.builders.BuilderActionOpenDefensiveBot;
import moth.boxxed.slainmecha.components.entity.BaseRelicComponent;
import moth.boxxed.slainmecha.relic.BotRelicBlock;
import moth.boxxed.slainmecha.components.block.MechanicalHeartBlock;
import moth.boxxed.slainmecha.components.entity.DefensiveBotComponent;
import moth.boxxed.slainmecha.interaction.PutEssenceInHeartInteraction;
import moth.boxxed.slainmecha.interaction.PutHeartInRelicInteraction;
import moth.boxxed.slainmecha.resources.MechanicalHeartPlaceMap;
import moth.boxxed.slainmecha.systems.DefensiveBotSystems;
import moth.boxxed.slainmecha.systems.MechanicalHeartSystems;

public class SlainMecha extends JavaPlugin {
    private static SlainMecha instance;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    @Getter private final Config<SlainMechaConfig> config = this.withConfig("SlainMechaConfig", SlainMechaConfig.CODEC);

    @Getter private final ComponentType<EntityStore, BaseRelicComponent> baseRelicComponentType;
    @Getter private final ComponentType<EntityStore, DefensiveBotComponent> defensiveBotComponentType;

    @Getter private final ComponentType<ChunkStore, MechanicalHeartBlock> mechanicalHeartComponentType;
    @Getter private final ComponentType<ChunkStore, BotRelicBlock> botRelicBlockComponentType;

    @Getter private final ResourceType<ChunkStore, MechanicalHeartPlaceMap> heartPlaceMapResourceType;

    public static SlainMecha get() {
        return instance;
    }

    public SlainMecha(JavaPluginInit init) {
        super(init);
        instance = this;
        
        this.baseRelicComponentType = this.getEntityStoreRegistry().registerComponent(BaseRelicComponent.class, "BaseRelic", BaseRelicComponent.CODEC);
        this.defensiveBotComponentType = this.getEntityStoreRegistry().registerComponent(DefensiveBotComponent.class, "DefensiveBot", DefensiveBotComponent.CODEC);

        this.mechanicalHeartComponentType = this.getChunkStoreRegistry().registerComponent(MechanicalHeartBlock.class, "MechanicalHeart", MechanicalHeartBlock.CODEC);
        this.botRelicBlockComponentType = this.getChunkStoreRegistry().registerComponent(BotRelicBlock.class, "BotRelic", BotRelicBlock.CODEC);

        this.heartPlaceMapResourceType = this.getChunkStoreRegistry().registerResource(MechanicalHeartPlaceMap.class, "MechanicalHeartPlaceMap", MechanicalHeartPlaceMap.CODEC);

        this.getEntityStoreRegistry().registerSystem(new DefensiveBotSystems.RefChangeSystem());

        this.getEntityStoreRegistry().registerSystem(new MechanicalHeartSystems.BreakBlockEventSystem());
        this.getEntityStoreRegistry().registerSystem(new MechanicalHeartSystems.PlaceBlockEventSystem());
        this.getChunkStoreRegistry().registerSystem(new MechanicalHeartSystems.RefSystem());

        this.getCodecRegistry(Interaction.CODEC).register("PutEssenceInHeart", PutEssenceInHeartInteraction.class, PutEssenceInHeartInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register("PutHeartInRelic", PutHeartInRelicInteraction.class, PutHeartInRelicInteraction.CODEC);
    }

    @Override
    protected void start() {
        NPCPlugin.get().registerCoreComponentType("OpenDefensiveBot", BuilderActionOpenDefensiveBot::new);
    }

    @Override
    protected void setup() {
        config.save();
    }
}
