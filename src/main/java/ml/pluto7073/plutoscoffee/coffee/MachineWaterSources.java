package ml.pluto7073.plutoscoffee.coffee;

import com.google.gson.JsonObject;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.network.s2c.SyncMachineWaterSourcesRegistryS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MachineWaterSources implements SimpleSynchronousResourceReloadListener {

    private static final ResourceLocation PHASE = PlutosCoffee.asId("phase/water_sources");
    private static final HashMap<ResourceLocation, MachineWaterSource> REGISTRY = new HashMap<>();

    public MachineWaterSources() {
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(PHASE, (player, joined) -> send(player));
    }

    public static void resetRegistry() {
        REGISTRY.clear();
    }

    public static MachineWaterSource register(ResourceLocation id, MachineWaterSource source) {
        REGISTRY.put(id, source);
        return source;
    }

    public static MachineWaterSource get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Ingredient asIngredient() {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (MachineWaterSource s : REGISTRY.values()) {
            list.addAll(Stream.of(s.ingredient().getItems()).peek(stack -> {
                if (stack.is(ConventionalItemTags.POTIONS)) {
                    PotionUtils.setPotion(stack, Potions.WATER);
                }
            }).toList());
        }
        return Ingredient.of(list.stream());
    }

    public static int getWaterAmount(ItemStack item) {
        for (MachineWaterSource s : REGISTRY.values()) {
            if (s.ingredient().test(item)) return s.waterAmount();
        }
        return 0;
    }

    public static void send(ServerPlayer player) {
        ServerPlayNetworking.send(player, new SyncMachineWaterSourcesRegistryS2CPacket(REGISTRY));
    }

    @Override
    public ResourceLocation getFabricId() {
        return PHASE;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        REGISTRY.clear();

        for (Map.Entry<ResourceLocation, Resource> e :
                resourceManager.listResources("machine_water_sources", id -> id.getPath().endsWith(".json")).entrySet()) {
            ResourceLocation id = e.getKey().withPath(path -> path.replace("machine_water_sources/", "").replace(".json", ""));
            try (InputStream stream = e.getValue().open()) {
                JsonObject data = GsonHelper.parse(new InputStreamReader(stream));
                MachineWaterSource source = fromJson(data);
                register(id, source);
            } catch (Exception ex) {
                PlutosCoffee.LOGGER.error("Failed to load Coffee Machine Water Source {}", id, ex);
            }
        }
    }

    private static MachineWaterSource fromJson(JsonObject data) {
        Ingredient source = Ingredient.fromJson(GsonHelper.getAsJsonObject(data, "source"));
        int water = GsonHelper.getAsInt(data, "water");
        return new MachineWaterSource(source, water);
    }

}
