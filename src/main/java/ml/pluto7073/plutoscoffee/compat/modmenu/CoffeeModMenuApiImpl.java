package ml.pluto7073.plutoscoffee.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pluto7073.plutoscoffee.Client;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.File;

public class CoffeeModMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.translatable("title.plutoscoffee.config"))
                    .setSavingRunnable(() -> Client.CONFIG.save())
                    .setDefaultBackgroundTexture(new ResourceLocation("minecraft:textures/gui/options_background.png"));

            ConfigCategory category = builder.getOrCreateCategory(Component.translatable("title.plutoscoffee.config"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.plutoscoffee.shouldShowCoffeeBar"), Client.CONFIG.shouldShowCoffeeBar())
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("config.plutoscoffee.shouldShowCoffeeBar.tooltip"))
                    .setSaveConsumer(newVal -> Client.CONFIG.setShouldShowCoffeeBar(newVal))
                    .build());

            return builder.build();
        };
    }

}
