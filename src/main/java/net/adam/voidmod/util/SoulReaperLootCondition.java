package net.adam.voidmod.util;

import com.mojang.serialization.MapCodec;
import net.adam.voidmod.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Set;

public class SoulReaperLootCondition implements LootItemCondition {

    public static final MapCodec<SoulReaperLootCondition> CODEC =
            MapCodec.unit(SoulReaperLootCondition::new);

    public static Builder builder() {
        return SoulReaperLootCondition::new;
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(LootContext context) {

        var damageSource = context.getParameter(LootContextParams.DAMAGE_SOURCE);
        if (damageSource == null) {
            return false;
        }

        if (!(damageSource.getEntity() instanceof Player player)) {
            return false;
        }

        ItemStack weapon = player.getMainHandItem();
        if (weapon.isEmpty()) {
            return false;
        }

        ItemEnchantments enchantments = weapon.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null) {
            return false;
        }

        Holder<?> soulReaper = player.registryAccess()
                .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                .getOrThrow(ModEnchantments.SOUL_REAPER);

        return enchantments.getLevel((Holder) soulReaper) > 0;
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.DAMAGE_SOURCE);
    }
}