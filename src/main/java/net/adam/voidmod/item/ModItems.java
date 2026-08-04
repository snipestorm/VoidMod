package net.adam.voidmod.item;

import net.adam.voidmod.VoidMod;
import net.adam.voidmod.item.custom.VoidCompassItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;


import java.util.function.Function;


public class ModItems {

    public static final Item VOID_COMPASS = registerItem("void_compass", setting -> new VoidCompassItem(setting.rarity(Rarity.EPIC).durability(10)));
    public static final Item VOID_SOUL = registerItem("void_soul", setting -> new Item(setting.rarity(Rarity.EPIC)));


    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(VoidMod.MOD_ID, name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VoidMod.MOD_ID, name)))));
    }

    public static void registerModItems() {
        VoidMod.LOGGER.info("Registering All Items For" + VoidMod.MOD_ID);
    }

}

//This should only appear on 26.1//