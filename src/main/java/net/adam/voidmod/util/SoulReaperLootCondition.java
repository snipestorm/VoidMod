package net.adam.voidmod.util;

import com.mojang.serialization.MapCodec;
import net.adam.voidmod.VoidMod;
import net.adam.voidmod.enchantment.ModEnchantments;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SoulReaperLootCondition implements LootCondition {

    public static final MapCodec<SoulReaperLootCondition> CODEC =
            MapCodec.unit(new SoulReaperLootCondition());

    public static final LootConditionType TYPE =
            new LootConditionType(CODEC);

    public static SoulReaperLootCondition builder() {
        return new SoulReaperLootCondition();
    }

    @Override
    public LootConditionType getType() {
        return TYPE;
    }
    @Override
    public boolean test(LootContext context) {

        System.out.println("SoulReaperLootCondition triggered");

        var damageSource = context.get(LootContextParameters.DAMAGE_SOURCE);
        if (damageSource == null) {
            System.out.println("No damage source");
            return false;
        }

        var attacker = damageSource.getAttacker();
        if (!(attacker instanceof ServerPlayerEntity player)) {
            System.out.println("Not killed by player");
            return false;
        }

        ItemStack weapon = player.getMainHandStack();
        System.out.println("Weapon: " + weapon);

        if (weapon.isEmpty()) {
            System.out.println("Weapon empty");
            return false;
        }

        var enchants = weapon.get(DataComponentTypes.ENCHANTMENTS);
        if (enchants == null) {
            System.out.println("No enchantments component");
            return false;
        }

        var registry = player.getEntityWorld()
                .getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT);

        var soulReaper = registry.getEntry(ModEnchantments.SOUL_REAPER.getValue());

        if (soulReaper.isEmpty()) {
            System.out.println("Soul Reaper not found in registry");
            return false;
        }

        int level = enchants.getLevel(soulReaper.get());
        System.out.println("Soul Reaper level: " + level);

        return level > 0;
    }

    public static void registerLootConditions() {
        Registry.register(
                Registries.LOOT_CONDITION_TYPE,
                Identifier.of(VoidMod.MOD_ID, "soul_reaper_check"),
                TYPE
        );
    }
}
