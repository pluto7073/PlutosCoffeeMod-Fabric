package ml.pluto7073.plutoscoffee.network;

import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.network.s2c.SyncMachineWaterSourcesRegistryS2CPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.player.LocalPlayer;

public class ModPacketsS2C {

    public static void registerPackets() {
        PayloadTypeRegistry.playS2C().register(SyncMachineWaterSourcesRegistryS2CPacket.TYPE, SyncMachineWaterSourcesRegistryS2CPacket.STREAM_CODEC);
    }

    @Environment(EnvType.CLIENT)
    public static void registerReceivers() {

        ClientPlayConnectionEvents.INIT.register((handler, client) ->
                ClientPlayNetworking.registerGlobalReceiver(SyncMachineWaterSourcesRegistryS2CPacket.TYPE, ModPacketsS2C::receiveWaterSources));

    }

    @Environment(EnvType.CLIENT)
    private static void receiveWaterSources(SyncMachineWaterSourcesRegistryS2CPacket packet, ClientPlayNetworking.Context context) {
        MachineWaterSources.resetRegistry();

        packet.map().forEach(MachineWaterSources::register);
    }

}
