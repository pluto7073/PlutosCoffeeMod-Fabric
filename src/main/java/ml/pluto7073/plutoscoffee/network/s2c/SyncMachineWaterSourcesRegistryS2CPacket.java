package ml.pluto7073.plutoscoffee.network.s2c;

import ml.pluto7073.plutoscoffee.PlutosCoffee;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSource;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record SyncMachineWaterSourcesRegistryS2CPacket(Map<ResourceLocation, MachineWaterSource> map) implements FabricPacket {

    public static final PacketType<SyncMachineWaterSourcesRegistryS2CPacket> TYPE =
            PacketType.create(PlutosCoffee.asId("s2c/sync_machine_water_sources"), SyncMachineWaterSourcesRegistryS2CPacket::read);

    private static SyncMachineWaterSourcesRegistryS2CPacket read(FriendlyByteBuf buf) {
        return new SyncMachineWaterSourcesRegistryS2CPacket(buf.readMap(FriendlyByteBuf::readResourceLocation, MachineWaterSource::read));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(map, FriendlyByteBuf::writeResourceLocation, MachineWaterSource::write);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
