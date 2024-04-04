package ml.pluto7073.plutoscoffee.network;

import ml.pluto7073.plutoscoffee.coffee.MachineWaterSource;
import ml.pluto7073.plutoscoffee.coffee.MachineWaterSources;
import ml.pluto7073.plutoscoffee.network.s2c.SyncMachineWaterSourcesRegistryS2CPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;

public class ModPacketsS2C {

    @Environment(EnvType.CLIENT)
    public static void register() {

        ClientPlayConnectionEvents.INIT.register((handler, client) ->
                ClientPlayNetworking.registerGlobalReceiver(SyncMachineWaterSourcesRegistryS2CPacket.TYPE, ModPacketsS2C::receiveWaterSources));

    }

    @Environment(EnvType.CLIENT)
    private static void receiveWaterSources(SyncMachineWaterSourcesRegistryS2CPacket packet, LocalPlayer player, PacketSender sender) {
        MachineWaterSources.resetRegistry();

        packet.map().forEach(MachineWaterSources::register);
    }

}
