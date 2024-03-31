package ml.pluto7073.plutoscoffee.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pluto7073.plutoscoffee.Client;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;

public class CoffeeModMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.plutoscoffee.config"))
                    .setSavingRunnable(() -> Client.CONFIG.save(new File("config/plutoscoffee.properties")))
                    .setDefaultBackgroundTexture(new Identifier("minecraft:textures/gui/options_background.png"));

            ConfigCategory category = builder.getOrCreateCategory(Text.translatable("title.plutoscoffee.config"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.plutoscoffee.shouldShowCoffeeBar"), Client.CONFIG.shouldShowCoffeeBar())
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("config.plutoscoffee.shouldShowCoffeeBar.tooltip"))
                    .setSaveConsumer(newVal -> Client.CONFIG.setShouldShowCoffeeBar(newVal))
                    .build());

            return builder.build();
        };
    }

}
