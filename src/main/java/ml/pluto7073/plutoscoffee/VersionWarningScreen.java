package ml.pluto7073.plutoscoffee;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.text.Text;

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

    }

    static {
        HEADER = Text.translatable("plutoscoffee.version.header");
        MESSAGE = Text.translatable("plutoscoffee.version.message");
        NARRATED = HEADER.copy().append("\n").append(MESSAGE);
    }

}
