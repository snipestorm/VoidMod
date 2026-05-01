package net.adam.voidmod.item;

import net.adam.voidmod.VoidMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup VOID = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(VoidMod.MOD_ID,"void_mod_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.VOID_COMPASS))
                    .displayName(Text.translatable("itemgroup.voidmod.void"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.VOID_COMPASS);
                        entries.add(ModItems.VOID_SOUL);
                    }) .build());

    public static void registerItemGroups() {
        VoidMod.LOGGER.info("Registering Item Groups For" + VoidMod.MOD_ID);
    }
}
