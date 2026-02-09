package moth.boxxed.fallenrelics;

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
import moth.boxxed.fallenrelics.NPC.builders.BuilderActionOpenDefensiveBot;
import moth.boxxed.fallenrelics.components.entity.BaseRelicComponent;
import moth.boxxed.fallenrelics.components.entity.SorterBotComponent;
import moth.boxxed.fallenrelics.components.block.relic.BotRelicBlock;
import moth.boxxed.fallenrelics.components.block.MechanicalHeartBlock;
import moth.boxxed.fallenrelics.components.entity.DefensiveBotComponent;
import moth.boxxed.fallenrelics.interaction.PutEssenceInHeartInteraction;
import moth.boxxed.fallenrelics.interaction.PutHeartInRelicInteraction;
import moth.boxxed.fallenrelics.resources.MechanicalHeartPlaceMap;
import moth.boxxed.fallenrelics.systems.DefensiveBotSystems;
import moth.boxxed.fallenrelics.systems.MechanicalHeartSystems;

public class FallenRelics extends JavaPlugin {
    private static FallenRelics instance;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    @Getter private final Config<FallenRelicsConfig> config = this.withConfig("SlainMechaConfig", FallenRelicsConfig.CODEC);

    @Getter private final ComponentType<EntityStore, BaseRelicComponent> baseRelicComponentType;
    @Getter private final ComponentType<EntityStore, DefensiveBotComponent> defensiveBotComponentType;
    @Getter private final ComponentType<EntityStore, SorterBotComponent> sorterBotComponentType;

    @Getter private final ComponentType<ChunkStore, MechanicalHeartBlock> mechanicalHeartComponentType;
    @Getter private final ComponentType<ChunkStore, BotRelicBlock> botRelicBlockComponentType;

    @Getter private final ResourceType<ChunkStore, MechanicalHeartPlaceMap> heartPlaceMapResourceType;

    public static FallenRelics get() {
        return instance;
    }

    public FallenRelics(JavaPluginInit init) {
        super(init);
        instance = this;
        
        this.baseRelicComponentType = this.getEntityStoreRegistry().registerComponent(BaseRelicComponent.class, "BaseRelic", BaseRelicComponent.CODEC);
        this.defensiveBotComponentType = this.getEntityStoreRegistry().registerComponent(DefensiveBotComponent.class, "DefensiveBot", DefensiveBotComponent.CODEC);
        this.sorterBotComponentType = this.getEntityStoreRegistry().registerComponent(SorterBotComponent.class, "SorterBot", SorterBotComponent.CODEC);

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
