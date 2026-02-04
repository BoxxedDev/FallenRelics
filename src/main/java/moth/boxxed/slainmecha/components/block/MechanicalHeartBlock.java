package moth.boxxed.slainmecha.components.block;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import lombok.Getter;
import lombok.Setter;
import moth.boxxed.slainmecha.SlainMecha;
import moth.boxxed.slainmecha.SlainMechaConfig;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MechanicalHeartBlock implements Component<ChunkStore> {
    public static final KeyedCodec<Integer> ITEM_KEYED_CODEC = new KeyedCodec<>("Essence", Codec.INTEGER);
    public static final BuilderCodec<MechanicalHeartBlock> CODEC =
            BuilderCodec.builder(MechanicalHeartBlock.class, MechanicalHeartBlock::new)
                    .append(
                            new KeyedCodec<>("Essence", Codec.INTEGER),
                            (o, i) -> o.essence = i,
                            (o) -> o.essence
                    )
                    .addValidator(Validators.greaterThanOrEqual(0))
                    .add()
                    .build();

    @Getter @Setter private int essence = 0;

    public MechanicalHeartBlock() {}

    public MechanicalHeartBlock(int essence) {
        this.essence = essence;
    }

    public void addEssence(ItemStack stack) {
        if (!(stack.getItemId().equals("Ingredient_Life_Essence") || stack.getItemId().equals("Ingredient_Life_Essence_Concentrated"))) return;

        SlainMechaConfig config = SlainMecha.get().getConfig().get();

        boolean isConcentratedEssence = stack.getItemId().equals("Ingredient_Life_Essence_Concentrated");
        int mult = isConcentratedEssence ? 100 : 1;
        int maxEssence = config.getHeartMaximumEssence();
        int essenceConversionMult = config.getEssenceConversion();

        int before = stack.getQuantity()*mult*essenceConversionMult + this.essence;

//        if (before > maxEssence) {
//            int remainder = before - maxEssence;
//
//            int stackAmount = Math.floorDiv(remainder, 100);
//            for (int i=0; i<stackAmount; i++) {
//                this.remainderEssence.add(new ItemStack("Ingredient_Life_Essence", 100));
//            }
//            if (remainder - (stackAmount*100) > 0) {
//                this.remainderEssence.add(new ItemStack("Ingredient_Life_Essence", remainder - (stackAmount*100)));
//            }
//        }
        this.essence = Math.max(Math.min(before, maxEssence), 0);
    }

    @Override
    public @Nullable Component<ChunkStore> clone() {
        return new MechanicalHeartBlock(this.essence);
    }
}
