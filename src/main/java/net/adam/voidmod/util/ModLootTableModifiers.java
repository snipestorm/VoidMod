package net.adam.voidmod.util;

import net.adam.voidmod.VoidMod;
import net.adam.voidmod.enchantment.ModEnchantmentEffects;
import net.adam.voidmod.enchantment.ModEnchantments;
import net.adam.voidmod.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;


public class ModLootTableModifiers {

    private static final Identifier ENDERMAN_ID
            = Identifier.fromNamespaceAndPath("minecraft", "entities/enderman");


    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, builder, source, registry) -> {

            Holder<Enchantment> soulReaper =
                    registry.lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(ModEnchantments.SOUL_REAPER);

            if (BuiltInLootTables.END_CITY_TREASURE.equals(key)) {

                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.2f))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK)
                                .apply(new SetEnchantmentsFunction.Builder().withEnchantment(soulReaper, ConstantValue.exactly(1))))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)).build());

                builder.pool(poolBuilder.build());
            }

            if (key.identifier().equals(ENDERMAN_ID)) {

                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(SoulReaperLootCondition.builder().build())// custom condition
                        .when(LootItemRandomChanceCondition.randomChance(1.0f)) // Drops 1% of the time
                        .add(LootItem.lootTableItem(ModItems.VOID_SOUL))
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)).build());

                builder.pool(poolBuilder.build());
            }

        });
    }

    public static void registerLootModifiers() {
        VoidMod.LOGGER.info("Registering Loot Modifiers for" + VoidMod.MOD_ID);
        modifyLootTables();
    }
}