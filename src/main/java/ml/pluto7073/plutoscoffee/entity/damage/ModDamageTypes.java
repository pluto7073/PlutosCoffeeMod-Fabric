package ml.pluto7073.plutoscoffee.entity.damage;

import com.mojang.serialization.Lifecycle;
import ml.pluto7073.plutoscoffee.PlutosCoffee;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface ModDamageTypes {

    RegistryKey<DamageType> CAFFEINE_OVERDOSE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(PlutosCoffee.MOD_ID, "caffeine_overdose"));

    static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

}
