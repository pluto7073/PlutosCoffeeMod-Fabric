package ml.pluto7073.plutoscoffee.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import ml.pluto7073.pdapi.client.gui.PDConfigScreen;
import ml.pluto7073.pdapi.config.PDClientConfig;
import ml.pluto7073.plutoscoffee.Client;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CoffeeModMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PDConfigScreen.INSTANCE::apply;
    }

}
