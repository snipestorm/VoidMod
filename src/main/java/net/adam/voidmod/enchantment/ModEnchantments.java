package net.adam.voidmod.enchantment;

import net.adam.voidmod.VoidMod;
import net.adam.voidmod.enchantment.custom.SoulReaperEnchantmentEffect;
import net.minecraft.world.item.enchantment.Enchantment;


public class ModEnchantments {
    public static final RegistryKey<Enchantment> SOUL_REAPER =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(VoidMod.MOD_ID, "soul_reaper"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, SOUL_REAPER, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORDS
                        ), 5, 2, Enchantment.leveledCost(5, 7), Enchantment.leveledCost(25, 9), 2, AttributeModifierSlot.MAINHAND))
                .exclusiveSet(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new SoulReaperEnchantmentEffect()));
    }


    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }

}
