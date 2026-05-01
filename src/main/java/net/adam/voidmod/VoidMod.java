package net.adam.voidmod;

import net.adam.voidmod.enchantment.ModEnchantmentEffects;
import net.adam.voidmod.item.ModItemGroups;
import net.adam.voidmod.item.ModItems;
import net.adam.voidmod.util.ModLootTableModifiers;
import net.adam.voidmod.util.SoulReaperLootCondition;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoidMod implements ModInitializer {
	public static final String MOD_ID = "voidmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		SoulReaperLootCondition.registerLootConditions();
		ModEnchantmentEffects.registerEnchantmentEffects();
		ModLootTableModifiers.registerLootModifiers();





	}
}