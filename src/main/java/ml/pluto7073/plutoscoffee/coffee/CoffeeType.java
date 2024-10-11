package ml.pluto7073.plutoscoffee.coffee;

import com.mojang.serialization.Codec;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class CoffeeType {

    public static final Codec<CoffeeType> BY_ID_CODEC =
            ResourceLocation.CODEC.xmap(CoffeeTypes.REGISTRY::get, CoffeeTypes::getIdentifier);

    public static final StreamCodec<RegistryFriendlyByteBuf, CoffeeType> BY_ID_STREAM_CODEC =
            StreamCodec.of(RegistryFriendlyByteBuf::writeResourceLocation, RegistryFriendlyByteBuf::readResourceLocation)
                    .map(CoffeeTypes.REGISTRY::get, CoffeeTypes::getIdentifier);

    private final String baseName;
    private final Item grounds;
    private final int caffeineContent;

    @SuppressWarnings("unused")
    public static final String ZELDA = null;

    public static CoffeeType byId(String id) {
        CoffeeType type;
        if (id.contains(":")) {
            type =  CoffeeTypes.REGISTRY.get(new ResourceLocation(id.replace("\"", "")));
        } else {
            type = CoffeeTypes.REGISTRY.get(new ResourceLocation(PlutosCoffee.MOD_ID, id.replace("\"", "")));
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

}
