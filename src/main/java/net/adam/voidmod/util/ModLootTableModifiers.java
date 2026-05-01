package net.adam.voidmod.util;

import net.adam.voidmod.VoidMod;
import net.adam.voidmod.enchantment.ModEnchantmentEffects;
import net.adam.voidmod.enchantment.ModEnchantments;
import net.adam.voidmod.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;

import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;



public class ModLootTableModifiers {

    private static final Identifier ENDERMAN_ID
            = Identifier.of("minecraft", "entities/enderman");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {

            if(LootTables.END_CITY_TREASURE_CHEST.equals(key)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.2f)) // Drops 20% of the time
                        .with(ItemEntry.builder(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsLootFunction.Builder().enchantment(registry.getEntryOrThrow(ModEnchantments.SOUL_REAPER),ConstantLootNumberProvider.create(1))))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder.build());
            }

            if (key.getValue().equals(ENDERMAN_ID)) {

                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .conditionally(SoulReaperLootCondition.builder()) // custom condition
                        .conditionally(RandomChanceLootCondition.builder(1.0f)) // Drops 1% of the time
                        .with(ItemEntry.builder(ModItems.VOID_SOUL)
                                .apply(SetCountLootFunction.builder(
                                        ConstantLootNumberProvider.create(1))));

                tableBuilder.pool(poolBuilder.build());
            }

        });
    }

    public static void registerLootModifiers() {
        VoidMod.LOGGER.info("Registering Loot Modifiers for" + VoidMod.MOD_ID);
        modifyLootTables();
    }
}