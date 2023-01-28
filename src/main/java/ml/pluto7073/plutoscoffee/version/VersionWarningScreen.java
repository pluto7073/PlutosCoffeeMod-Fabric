package ml.pluto7073.plutoscoffee.version;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class VersionWarningScreen extends WarningScreen {

    private static final Text HEADER;
    private static final Text MESSAGE;
    private static final Text NARRATED;

    public VersionWarningScreen() {
        super(HEADER, MESSAGE, NARRATED);
    }

    @Override
    protected void initButtons(int yOffset) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, 100 + yOffset, 150, 20, Text.translatable("plutoscoffee.version.okay"),
                (buttonWidget) -> {
            this.client.setScreen(null);
        }));
    }

    @Override
    protected void drawTitle(MatrixStack matrices) {
        drawTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2 - (HEADER.getString().length() * 3), 30, 16777215);
    }

    static {
        HEADER = Text.translatable("plutoscoffee.version.header").formatted(Formatting.BOLD);
        MESSAGE = Text.translatable("plutoscoffee.version.message");
        NARRATED = HEADER.copy().append("\n").append(MESSAGE);
    }

}
