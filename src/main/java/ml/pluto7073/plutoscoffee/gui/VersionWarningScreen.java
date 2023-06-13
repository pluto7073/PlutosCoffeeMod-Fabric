package ml.pluto7073.plutoscoffee.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Environment(EnvType.CLIENT)
public class VersionWarningScreen extends WarningScreen {

    private static final Text HEADER;
    private static final Text MESSAGE;
    private static final Text NARRATED;

    private static final String LATEST_RELEASE_LINK = "https://pluto7073.github.io/files/latest-coffeemod-release.html";

    private URI link;

    public VersionWarningScreen() {
        super(HEADER, MESSAGE, NARRATED);
    }

    @Override
    protected void initButtons(int yOffset) {
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("plutosmods.version.okay"),
                (buttonWidget) -> {
            this.client.setScreen(null);
        }).dimensions(this.width / 2 - 155, 100 + yOffset, 150, 20).build());
        this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("plutosmods.version.open_mod_page"),
                (buttonWidget) -> {
                    URI uRI;
                    try {
                        uRI = new URI(LATEST_RELEASE_LINK);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    if (this.client.options.getChatLinksPrompt().getValue()) {
                        this.link = uRI;
                        this.client.setScreen(new ConfirmLinkScreen(this::confirmLink, LATEST_RELEASE_LINK, false));
                    } else {
                        this.openLink(uRI);
                        this.client.setScreen(null);
                    }
        }).dimensions(this.width / 2 + 5, 100 + yOffset, 150, 20).build());
    }

    @Override
    protected void drawTitle(DrawContext context) {
        context.drawTextWithShadow(this.textRenderer, this.title, (this.width / 2) - (this.textRenderer.getWidth(this.title) / 2), 30, 16777215);
    }

    private void confirmLink(boolean open) {
        if (open) {
            this.openLink(this.link);
        }

        this.link = null;
        this.client.setScreen(null);
    }

    private void openLink(URI link) {
        Util.getOperatingSystem().open(link);
    }

    static {
        HEADER = Text.translatable("plutosmods.version.header.plutoscoffee").formatted(Formatting.BOLD);
        MESSAGE = Text.translatable("plutosmods.version.message");
        NARRATED = HEADER.copy().append("\n").append(MESSAGE);
    }

}
