package net.adam.voidmod.item.custom;

import net.adam.voidmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;


public class VoidCompassItem extends CompassItem {

    public int count = 0;
    private int maxCount = 15; // 0.75 Secs
    public boolean teleported;

    public boolean countFinished() {
        return count >= maxCount;
    }

    private void countUp() {
        count++;
    }

    private static final Component VOID_COMPASS_NAME = Component.translatable("item.voidmod.linked_void_compass");

    public VoidCompassItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(final ItemStack itemStack) {
        return itemStack.has(DataComponents.LODESTONE_TRACKER) || super.isFoil(itemStack);
    }


    @Override
    public InteractionResult useOn(final UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        if (!level.getBlockState(blockPos).is(Blocks.LODESTONE)) {
            return super.useOn(context);
        } else {
            level.playSound(null, blockPos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            Player player = context.getPlayer();
            ItemStack itemStack = context.getItemInHand();
            boolean replaceExistingStack = !player.hasInfiniteMaterials() && itemStack.getCount() == 1;
            LodestoneTracker target = new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), blockPos)), true);
            if (replaceExistingStack) {
                itemStack.set(DataComponents.LODESTONE_TRACKER, target);
            } else {
                ItemStack voidCompass = itemStack.transmuteCopy(ModItems.VOID_COMPASS, 1);
                itemStack.consume(1, player);
                voidCompass.set(DataComponents.LODESTONE_TRACKER, target);
                if (!player.getInventory().add(voidCompass)) {
                    player.drop(voidCompass, false);
                }
            }

            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        if (!level.isClientSide() && isFoil(itemStack)) {
            BlockPos pos = getLodestonePosition(itemStack);
            ResourceKey<Level> dimension = getLodestoneDimension(itemStack);
            ServerPlayer player = (ServerPlayer) user;
            ServerLevel targetWorld = player.level().getLevel().getServer().getLevel(dimension);

            // Above Gets Lodestone Posistion and Dimension, Below is the Teleport Action //

            user.teleportTo(targetWorld, pos.getX(), pos.getY(), pos.getZ(), Collections.EMPTY_SET, 0, 0, false);
            targetWorld.playSound((Player) null, pos.getX() + 1, pos.getY(), pos.getZ() + 1, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1F, 1F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            user.resetStat(Stats.ITEM_USED.get(this));
            itemStack.setDamageValue(1);
            teleported = true;
            System.out.println(user.getName().getString() + " Used a Void Compass in " + user.getGameProfile().toString());

        }
        return InteractionResult.SUCCESS;

    }


    @Override
    public Component getName(ItemStack stack) {
        return stack.has(DataComponents.LODESTONE_TRACKER) ? VOID_COMPASS_NAME : super.getName(stack);
    }

    private BlockPos getLodestonePosition(ItemStack stack) {
        LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);

        if (tracker != null && tracker.target().isPresent()) {
            return tracker.target().get().pos();
        }
        return null;
    }

    public ResourceKey<Level> getLodestoneDimension(ItemStack stack) {
        LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);

        if (tracker != null) {
            Optional<GlobalPos> target = tracker.target();
            if (target.isPresent()) {
                return target.get().dimension();
            }
        }
        return null;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {

        Player playerEntity = (ServerPlayer) entity;
        if (teleported) {

            InteractionHand hand = playerEntity.getUsedItemHand();
            ItemStack itemStack = playerEntity.getItemInHand(hand);
            BlockPos pos = getLodestonePosition(itemStack);

            if (countFinished()) {
                teleported = false;
                count = 0;
                ((ServerLevel) world).sendParticles(ParticleTypes.PORTAL,
                        pos.getX(), pos.getY() + 1.5, pos.getZ(),
                        world.getRandom().nextIntBetweenInclusive(1000, 5000), 0, 0, 0, 2.5);

            } else {
                countUp();
            }
        }
    }
}