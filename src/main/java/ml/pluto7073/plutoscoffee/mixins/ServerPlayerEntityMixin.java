package ml.pluto7073.plutoscoffee.mixins;

import ml.pluto7073.plutoscoffee.Utils;
import ml.pluto7073.plutoscoffee.registry.ModMisc;
import ml.pluto7073.plutoscoffee.registry.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void plutoscoffee_readPlayerCoffeeData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound coffeeData;
        if (!nbt.contains("CoffeeData")) return;
        else {
            coffeeData = nbt.getCompound("CoffeeData");
        }
        
        // Caffeine Content
        if (coffeeData.contains("CaffeineContent")) {
            float caffeine = coffeeData.getFloat("CaffeineContent");
            this.dataTracker.set(ModMisc.PLAYER_CAFFEINE_AMOUNT, caffeine);
        }

        // Original Caffeine Content (for math purposes)
        if (coffeeData.contains("OriginalCaffeineContent")) {
            float originalCaffeine = coffeeData.getFloat("OriginalCaffeineContent");
            this.dataTracker.set(ModMisc.PLAYER_ORIGINAL_CAFFEINE_AMOUNT, originalCaffeine);
        }

        // Ticks Since Last Caffeine
        if (coffeeData.contains("TicksSinceLastCaffeine")) {
            int ticks = coffeeData.getInt("TicksSinceLastCaffeine");
            this.dataTracker.set(ModMisc.PLAYER_TICKS_SINCE_LAST_CAFFEINE, ticks);
        }
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void plutoscoffee_writePlayerCoffeeData(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound coffeeData = new NbtCompound();

        // Caffeine Content
        float caffeine = this.dataTracker.get(ModMisc.PLAYER_CAFFEINE_AMOUNT);
        coffeeData.putFloat("CaffeineContent", caffeine);

        // Original Caffeine Content (for math purposes)
        float originalCaffeine = this.dataTracker.get(ModMisc.PLAYER_ORIGINAL_CAFFEINE_AMOUNT);
        coffeeData.putFloat("OriginalCaffeineContent", originalCaffeine);

        // Ticks Since Last Caffeine
        int ticksSinceLast = this.dataTracker.get(ModMisc.PLAYER_TICKS_SINCE_LAST_CAFFEINE);
        coffeeData.putInt("TicksSinceLastCaffeine", ticksSinceLast);

        nbt.put("CoffeeData", coffeeData);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void plutoscoffee_updateCaffeineContent(CallbackInfo ci) {
        int ticks = this.dataTracker.get(ModMisc.PLAYER_TICKS_SINCE_LAST_CAFFEINE);
        ticks++;
        float originalCaffeine = this.dataTracker.get(ModMisc.PLAYER_ORIGINAL_CAFFEINE_AMOUNT);
        float newCaffeine = Utils.calculateCaffeineDecay(ticks, originalCaffeine);
        this.dataTracker.set(ModMisc.PLAYER_CAFFEINE_AMOUNT, newCaffeine);
        this.dataTracker.set(ModMisc.PLAYER_TICKS_SINCE_LAST_CAFFEINE, ticks);
    }
    
    @Inject(at = @At("TAIL"), method = "tick")
    public void plutoscoffee_caffeineLevelEffects(CallbackInfo ci) {
        float caffeine = this.dataTracker.get(ModMisc.PLAYER_CAFFEINE_AMOUNT);
        if (caffeine >= 3000.0F && !getAbilities().creativeMode) {
            this.addStatusEffect(new StatusEffectInstance(ModStatusEffects.CAFFEINE_OVERDOSE, 20 * 60));
        }
    }
    
}
