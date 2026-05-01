package net.adam.voidmod.item.custom;

import net.adam.voidmod.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.CopperGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.Collections;
import java.util.Optional;

import static net.minecraft.entity.EntityType.*;

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

    private static final Text VOID_COMPASS_NAME = Text.translatable("item.voidmod.linked_void_compass");

    public VoidCompassItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.contains(DataComponentTypes.LODESTONE_TRACKER) || super.hasGlint(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        if (!world.getBlockState(blockPos).isOf(Blocks.LODESTONE)) {
            return super.useOnBlock(context);
        } else {
            world.playSound(null, blockPos, SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            PlayerEntity playerEntity = context.getPlayer();
            ItemStack itemStack = context.getStack();
            boolean bl = !playerEntity.isInCreativeMode() && itemStack.getCount() == 1;
            LodestoneTrackerComponent lodestoneTrackerComponent = new LodestoneTrackerComponent(Optional.of(GlobalPos.create(world.getRegistryKey(), blockPos)), true);
            if (bl) {
                itemStack.set(DataComponentTypes.LODESTONE_TRACKER, lodestoneTrackerComponent);
            } else {
                ItemStack itemStack1 = new ItemStack(ModItems.VOID_COMPASS, 1);
                itemStack.decrementUnlessCreative(1, playerEntity);
                itemStack1.set(DataComponentTypes.LODESTONE_TRACKER, lodestoneTrackerComponent);
                if (!playerEntity.getInventory().insertStack(itemStack1)) {
                    playerEntity.dropItem(itemStack1, false);
                }
            }
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient() && hasGlint(itemStack)) {
            BlockPos pos = getLodestonePosition(itemStack);
            RegistryKey<World> dimension = getLodestoneDimension(itemStack);
            ServerPlayerEntity player = (ServerPlayerEntity) user;
            ServerWorld targetWorld = player.getEntityWorld().getServer().getWorld(dimension);

            // Above Gets Lodestone Posistion and Dimension, Below is the Teleport Action //

            user.teleport(targetWorld, pos.getX(), pos.getY(), pos.getZ(), Collections.EMPTY_SET, 0, 0, false);
            targetWorld.playSound((PlayerEntity) null, pos.getX() + 1, pos.getY(), pos.getZ() + 1, SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.NEUTRAL, 1F, 1F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            itemStack.damage(1, user, hand.getEquipmentSlot());
            teleported = true;
            System.out.println(user.getName().getString() + " Used a Void Compass in " + user.getGameMode().asString());

        }
        return ActionResult.SUCCESS;

    }


    @Override
    public Text getName(ItemStack stack) {
        return stack.contains(DataComponentTypes.LODESTONE_TRACKER) ? VOID_COMPASS_NAME : super.getName(stack);
    }

    private BlockPos getLodestonePosition(ItemStack stack) {
        LodestoneTrackerComponent tracker = stack.get(DataComponentTypes.LODESTONE_TRACKER);

        if (tracker != null && tracker.target().isPresent()) {
            return tracker.target().get().pos();
        }
        return null;
    }

    public RegistryKey<World> getLodestoneDimension(ItemStack stack) {
        LodestoneTrackerComponent tracker = stack.get(DataComponentTypes.LODESTONE_TRACKER);

        if (tracker != null) {
            Optional<GlobalPos> target = tracker.target();
            if (target.isPresent()) {
                return target.get().dimension();
            }
        }
        return null;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {

        if (entity.isPlayer()) {

            if (teleported) {

                PlayerEntity playerEntity = (ServerPlayerEntity) entity;
                Hand hand = playerEntity.getActiveHand();
                ItemStack itemStack = playerEntity.getStackInHand(hand);
                BlockPos pos = getLodestonePosition(itemStack);

                if (countFinished()) {
                    teleported = false;
                    count = 0;
                    ((ServerWorld) world).spawnParticles(ParticleTypes.PORTAL,
                            pos.getX(), pos.getY() + 1.5, pos.getZ(),
                            world.random.nextBetweenExclusive(1000, 5000), 0, 0, 0, 2.5);

                } else {
                    countUp();
                }
            }
        } else {
        }
    }
}