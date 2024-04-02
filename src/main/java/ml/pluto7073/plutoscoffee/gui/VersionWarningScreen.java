package ml.pluto7073.plutoscoffee.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Environment(EnvType.CLIENT)
public class VersionWarningScreen extends WarningScreen {

    private static final Component HEADER;
    private static final Component MESSAGE;
    private static final Component NARRATED;

    private static final String LATEST_RELEASE_LINK = "https://pluto7073.github.io/files/latest-coffeemod-release.html";

    private URI link;

    public VersionWarningScreen() {
        super(HEADER, MESSAGE, NARRATED);
    }

    @Override
    protected void initButtons(int yOffset) {
        this.addRenderableWidget(new Button.Builder(Component.translatable("plutosmods.version.okay"),
                (buttonWidget) -> {
            if (this.minecraft != null)
                this.minecraft.setScreen(null);
        }).bounds(this.width / 2 - 155, 100 + yOffset, 150, 20).build());
        this.addRenderableWidget(new Button.Builder(Component.translatable("plutosmods.version.open_mod_page"),
                (buttonWidget) -> {
                    URI uRI;
                    try {
                        uRI = new URI(LATEST_RELEASE_LINK);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    //noinspection DataFlowIssue
                    if (this.minecraft.options.chatLinksPrompt().get()) {
                        this.link = uRI;
                        this.minecraft.setScreen(new ConfirmLinkScreen(this::confirmLink, LATEST_RELEASE_LINK, false));
                    } else {
                        this.openLink(uRI);
                        this.minecraft.setScreen(null);
                    }
        }).bounds(this.width / 2 + 5, 100 + yOffset, 150, 20).build());
    }

    @Override
    protected void renderTitle(GuiGraphics context) {
        context.drawString(this.font, this.title, (this.width / 2) - (this.font.width(this.title) / 2), 30, 16777215);
    }

    private void confirmLink(boolean open) {
        if (open) {
            this.openLink(this.link);
        }

        this.link = null;
        if (this.minecraft != null)
            this.minecraft.setScreen(null);
    }

    private void openLink(URI link) {
        Util.getPlatform().openUri(link);
    }

    static {
        HEADER = Component.translatable("plutosmods.version.header.plutoscoffee").withStyle(ChatFormatting.BOLD);
        MESSAGE = Component.translatable("plutosmods.version.message");
        NARRATED = HEADER.copy().append("\n").append(MESSAGE);
    }

}
