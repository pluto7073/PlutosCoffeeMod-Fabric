package ml.pluto7073.plutoscoffee.coffee;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public class CoffeeType implements TooltipProvider {

    public static final Codec<CoffeeType> COMPONENT_CODEC =
            ResourceLocation.CODEC.xmap(CoffeeTypes::get, CoffeeTypes::getIdentifier);
    public static final StreamCodec<ByteBuf, CoffeeType> STREAM_COMPONENT_CODEC =
            ResourceLocation.STREAM_CODEC.map(CoffeeTypes::get, CoffeeTypes::getIdentifier);

    private final String baseName;
    private final Item grounds;
    private final int caffeineContent;

    @SuppressWarnings("unused")
    public static final String ZELDA = null;

    public static CoffeeType byId(String id) {
        CoffeeType type;
        if (id.contains(":")) {
            type =  CoffeeTypes.REGISTRY.get(ResourceLocation.parse(id.replace("\"", "")));
        } else {
            type = CoffeeTypes.REGISTRY.get(ResourceLocation.fromNamespaceAndPath(PlutosCoffee.MOD_ID, id.replace("\"", "")));
        }
        if (type == null) {
            type = CoffeeTypes.EMPTY;
        }
        return type;
    }

    /**
     *
     * @param baseName the id used in registering the coffee type
     * @param grounds the item to be used to brew the coffee
     * @param caffeineContent amount in mg of caffeine to add to the drinker.
     */
    public CoffeeType(String baseName, Item grounds, int caffeineContent) {
        this.baseName = baseName;
        this.grounds = grounds;
        this.caffeineContent = caffeineContent;
    }

    public String getTranslationKey() {
        return "coffee_type.plutoscoffee." + baseName;
    }

    public Item getGrounds() {
        return grounds;
    }

    public int getCaffeineContent() {
        return caffeineContent;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipConsumer, TooltipFlag config) {
        tooltipConsumer.accept(Component.translatable(getTranslationKey()));
    }
}
