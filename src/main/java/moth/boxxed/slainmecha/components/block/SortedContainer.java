package moth.boxxed.slainmecha.components.block;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.asset.type.item.config.container.SingleItemDropContainer;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class SortedContainer implements Component<ChunkStore> {
    public static final BuilderCodec<SortedContainer> CODEC =
            BuilderCodec.<SortedContainer>builder(SortedContainer.class, SortedContainer::new)

                    .build();

    @Getter @Setter private SimpleItemContainer itemContainer;
    @Getter @Setter @Nullable private ItemStack filterStack;

    public SortedContainer() {
        this.itemContainer = ItemContainer.ensureContainerCapacity(this.itemContainer, (short) 54, SimpleItemContainer::new, new ArrayList<>());
        this.filterStack = null;
    }

    @Override
    public @Nullable Component<ChunkStore> clone() {
        return null;
    }
}
