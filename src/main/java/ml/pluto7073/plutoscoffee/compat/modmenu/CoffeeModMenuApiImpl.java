package ml.pluto7073.plutoscoffee.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ml.pluto7073.pdapi.client.gui.PDConfigScreen;

public class CoffeeModMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PDConfigScreen.INSTANCE::apply;
    }

}
