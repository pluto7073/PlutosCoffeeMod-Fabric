package ml.pluto7073.plutoscoffee.compat.rei;

import com.mojang.blaze3d.systems.RenderSystem;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ItemIcon implements Renderer {

    private final Supplier<ItemStack> stackSupplier;
    private ItemStack stack;

    public ItemIcon(Supplier<ItemStack> stack) {
        this.stackSupplier = stack;
    }

    @Override
    public void render(DrawContext context, Rectangle bounds, int mouseX, int mouseY, float delta) {
        if (stack == null) {
            stack = stackSupplier.get();
        }

        int xOffset = bounds.x;
        int yOffset = bounds.y;

        RenderSystem.enableDepthTest();

        context.drawItem(stack, xOffset, yOffset);
    }
}
