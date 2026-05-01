package net.adam.voidmod.enchantment;

import com.mojang.serialization.MapCodec;
import net.adam.voidmod.VoidMod;
import net.adam.voidmod.enchantment.custom.SoulReaperEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;



public class ModEnchantmentEffects {

    public static final MapCodec<? extends EnchantmentEntityEffect> SOUL_REAPER = registerEntityEffect("soul_reaper.json", SoulReaperEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(VoidMod.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        VoidMod.LOGGER.info("Registering Mod Enchantment Effects For" + VoidMod.MOD_ID);
    }
}
