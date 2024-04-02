package ml.pluto7073.plutoscoffee.compat.rei;

import com.mojang.blaze3d.systems.RenderSystem;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ItemIcon implements Renderer {

    private final Supplier<ItemStack> stackSupplier;
    private ItemStack stack;

    public ItemIcon(Supplier<ItemStack> stack) {
        this.stackSupplier = stack;
    }

    @Override
    public void render(GuiGraphics context, Rectangle bounds, int mouseX, int mouseY, float delta) {
        if (stack == null) {
            stack = stackSupplier.get();
        }

        int xOffset = bounds.x;
        int yOffset = bounds.y;

        RenderSystem.enableDepthTest();

        context.renderItem(stack, xOffset, yOffset);
    }
}
