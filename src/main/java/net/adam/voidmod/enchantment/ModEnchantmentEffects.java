package net.adam.voidmod.enchantment;

import com.mojang.serialization.MapCodec;
import net.adam.voidmod.VoidMod;
import net.adam.voidmod.enchantment.custom.SoulReaperEnchantmentEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;


public class ModEnchantmentEffects {

    public static final MapCodec<? extends EnchantmentEntityEffect> SOUL_REAPER = registerEntityEffect("soul_reaper.json", SoulReaperEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.fromNamespaceAndPath(VoidMod.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        VoidMod.LOGGER.info("Registering Mod Enchantment Effects For" + VoidMod.MOD_ID);
    }
}
