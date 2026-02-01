package moth.boxxed.slainmecha;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import lombok.Getter;
import lombok.Setter;

public class SlainMechaConfig {
    public static final BuilderCodec<SlainMechaConfig> CODEC =
            BuilderCodec.builder(SlainMechaConfig.class, SlainMechaConfig::new)
                    .append(
                            new KeyedCodec<>("EssenceConversion", Codec.INTEGER),
                            (o, i) -> o.essenceConversion = i,
                            (o) -> o.essenceConversion
                    )
                    .addValidator(Validators.greaterThanOrEqual(1))
                    .add()
                    .append(
                            new KeyedCodec<>("EssenceConversion", Codec.INTEGER),
                            (o, i) -> o.essenceConversion = i,
                            (o) -> o.essenceConversion
                    )
                    .addValidator(Validators.greaterThanOrEqual(100))
                    .add()
                    .build();

    @Getter @Setter private int essenceConversion = 1;
    @Getter @Setter private int heartMaximumEssence = 1000;

    public SlainMechaConfig() {}
}
