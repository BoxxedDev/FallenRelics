package moth.boxxed.slainmecha.NPC.builders;

import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import moth.boxxed.slainmecha.NPC.actions.ActionOpenDefensiveBot;
import org.jspecify.annotations.Nullable;

public class BuilderActionOpenDefensiveBot extends BuilderActionBase {
    @Override
    public @Nullable String getShortDescription() {
        return "Open the defensive bot UI for controlling it";
    }

    @Override
    public @Nullable String getLongDescription() {
        return this.getShortDescription();
    }

    @Override
    public @Nullable Action build(BuilderSupport support) {
        return new ActionOpenDefensiveBot(this, support);
    }

    @Override
    public @Nullable BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }
}
