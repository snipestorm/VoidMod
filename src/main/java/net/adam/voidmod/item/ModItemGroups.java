package net.adam.voidmod.item;

import net.adam.voidmod.VoidMod;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final CreativeModeTab VOID = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(VoidMod.MOD_ID,"void_mod_items"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.VOID_COMPASS))
                    .title(Component.translatable("itemgroup.voidmod.void"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.VOID_COMPASS);
                        output.accept(ModItems.VOID_SOUL);
                    }) .build());

    public static void registerItemGroups() {
        VoidMod.LOGGER.info("Registering Item Groups For" + VoidMod.MOD_ID);
    }
}
