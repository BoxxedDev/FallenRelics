package moth.boxxed.fallenrelics.components.entity;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import lombok.Setter;
import moth.boxxed.fallenrelics.FallenRelics;
import org.jetbrains.annotations.Nullable;

public class BaseRelicComponent implements Component<EntityStore> {
    public static final BuilderCodec<BaseRelicComponent> CODEC =
            BuilderCodec.<BaseRelicComponent>builder(BaseRelicComponent.class, BaseRelicComponent::new)
                    .append(
                            new KeyedCodec<>("Essence", Codec.FLOAT),
                            (o, i) -> o.essence = i,
                            (o) -> o.essence
                    ).addValidator(Validators.greaterThanOrEqual(0f)).add()
                    .append(
                            new KeyedCodec<>("Heart", ItemStack.CODEC),
                            (o, i) -> o.heart = i,
                            (o) -> o.heart
                    ).add()
                    .build();

    @Getter @Setter private ItemStack heart = ItemStack.EMPTY;
    @Getter @Setter private float essence = 0;

    public BaseRelicComponent() {}

    public BaseRelicComponent(float essence, ItemStack heart) {
        this.essence = essence;
        this.heart = heart;
    }

    public void decreaseEssence(float essence) {
        this.essence = Math.max(0, this.essence - essence);
    }

    public void increaseEssence(float essence) {
        this.essence = Math.min(FallenRelics.get().getConfig().get().getHeartMaximumEssence(), this.essence + essence);
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new BaseRelicComponent(this.essence, this.heart);
    }
}
