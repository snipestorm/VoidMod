package net.adam.voidmod.item;

import net.adam.voidmod.VoidMod;
import net.adam.voidmod.item.custom.VoidCompassItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;


public class ModItems {

    public static final Item VOID_COMPASS = registerItem("void_compass", setting -> new VoidCompassItem(setting.rarity(Rarity.EPIC).maxDamage(10)));
    public static final Item VOID_SOUL = registerItem("void_soul", setting -> new Item(setting.rarity(Rarity.EPIC)));


    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(VoidMod.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(VoidMod.MOD_ID, name)))));
    }

    public static void registerModItems() {
        VoidMod.LOGGER.info("Registering All Items For" + VoidMod.MOD_ID);
    }

}
