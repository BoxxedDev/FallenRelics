package moth.boxxed.slainmecha.components.block;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import lombok.Getter;
import lombok.Setter;
import moth.boxxed.slainmecha.components.entity.DefensiveBotComponent;
import org.jspecify.annotations.Nullable;

public class DefensiveBotBlock implements Component<ChunkStore> {
    public static final BuilderCodec<DefensiveBotBlock> CODEC =
            BuilderCodec.<DefensiveBotBlock>builder(DefensiveBotBlock.class, DefensiveBotBlock::new)
                    .append(new KeyedCodec<>("Heart", ItemStack.CODEC), (o, i) -> o.heartStack = i, (o) -> o.heartStack).add()
                    .append(new KeyedCodec<>("Essence", ItemStack.CODEC), (o, i) -> o.essenceStack = i, (o) -> o.essenceStack).add()
                    .build();

    @Getter private ItemStack heartStack;
    @Getter private ItemStack essenceStack;

    public DefensiveBotBlock() {}

    public DefensiveBotBlock(ItemStack heartStack, ItemStack essenceStack) {
        this.heartStack = heartStack;
        this.essenceStack = essenceStack;
    }

    public void setHeartStack(ItemStack stack) {
        if (stack.isEquivalentType(new ItemStack("Mechanical_Heart"))) {
            this.heartStack = stack;
        }
    }

    public void setEssenceStack(ItemStack stack) {
        if (stack.isEquivalentType(new ItemStack("Ingredient_Life_Essence")) ||
                stack.isEquivalentType(new ItemStack("Ingredient_Life_Essence_Concentrated"))) {
            this.heartStack = stack;
        }
    }

    @Override
    public @Nullable Component<ChunkStore> clone() {
        return new DefensiveBotBlock(this.heartStack, this.essenceStack);
    }
}
