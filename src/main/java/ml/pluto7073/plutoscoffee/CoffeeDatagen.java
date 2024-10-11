package ml.pluto7073.plutoscoffee;

import ml.pluto7073.plutoscoffee.datagen.CoffeeAdditionProvider;
import ml.pluto7073.plutoscoffee.datagen.CoffeeAdvancementProvider;
import ml.pluto7073.plutoscoffee.datagen.CoffeeRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CoffeeDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();
        pack.addProvider(CoffeeAdvancementProvider::new);
        pack.addProvider(CoffeeAdditionProvider::new);
        pack.addProvider(CoffeeRecipeProvider::new);
    }

}
