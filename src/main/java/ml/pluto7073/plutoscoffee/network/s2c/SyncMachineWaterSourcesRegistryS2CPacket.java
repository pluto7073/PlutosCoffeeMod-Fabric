package ml.pluto7073.plutoscoffee.network.s2c;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSource;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
public record SyncMachineWaterSourcesRegistryS2CPacket(Map<ResourceLocation, MachineWaterSource> map) implements CustomPacketPayload {

    public static final Type<SyncMachineWaterSourcesRegistryS2CPacket> TYPE = new Type<>(PlutosCoffee.asId("s2c/update_machine_water_sources"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMachineWaterSourcesRegistryS2CPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, MachineWaterSource.STREAM_CODEC),
            SyncMachineWaterSourcesRegistryS2CPacket::map,
            SyncMachineWaterSourcesRegistryS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void init() {
        PayloadTypeRegistry.playS2C().register(TYPE, CODEC);
    }

}
