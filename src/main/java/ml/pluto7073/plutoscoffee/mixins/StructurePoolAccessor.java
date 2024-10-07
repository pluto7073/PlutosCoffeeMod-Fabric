package ml.pluto7073.plutoscoffee.mixins;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * Borrowed from FriendsAndFoes by Faboslav
 */
@Mixin(StructureTemplatePool.class)
public interface StructurePoolAccessor {

    @Accessor("rawTemplates")
    List<Pair<StructurePoolElement, Integer>> getRawTemplates();

    @Mutable
    @Accessor("rawTemplates")
    void setRawTemplates(List<Pair<StructurePoolElement, Integer>> rawTemplates);

    @Accessor("templates")
    ObjectArrayList<StructurePoolElement> getTemplates();

    @Mutable
    @Accessor("templates")
    void setTemplates(ObjectArrayList<StructurePoolElement> elements);

}
