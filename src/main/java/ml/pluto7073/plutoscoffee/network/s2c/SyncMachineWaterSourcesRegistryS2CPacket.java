package ml.pluto7073.plutoscoffee.network.s2c;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record SyncMachineWaterSourcesRegistryS2CPacket(HashMap<ResourceLocation, MachineWaterSource> map) implements CustomPacketPayload {

    public static final Type<SyncMachineWaterSourcesRegistryS2CPacket> TYPE =
            new Type<>(PlutosCoffee.asId("s2c/sync_machine_water_sources"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMachineWaterSourcesRegistryS2CPacket> STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, MachineWaterSource.STREAM_CODEC)
                    .map(SyncMachineWaterSourcesRegistryS2CPacket::new, SyncMachineWaterSourcesRegistryS2CPacket::map);

    @Override
    public @NotNull Type<?> type() {
        return TYPE;
    }
}
