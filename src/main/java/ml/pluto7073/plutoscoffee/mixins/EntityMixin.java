package ml.pluto7073.plutoscoffee.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow @Final protected DataTracker dataTracker;
}
