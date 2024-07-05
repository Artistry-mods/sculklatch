package chaos.sculklatch.blocks.custom;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.damagetype.ModDamageSources;
import chaos.sculklatch.tags.ModTags;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SculkJawBlock extends SculkBlock implements SculkSpreadable {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty IS_SCARED = BooleanProperty.of("is_scared");
    public static final BooleanProperty IS_EATING = BooleanProperty.of("is_eating");
    public static final IntProperty HEALTH = IntProperty.of("health", 0, SculkLatch.SCULK_JAW_HEALTH);
    private static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    private static final VoxelShape FULL_COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    public SculkJawBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(IS_SCARED, false).with(HEALTH, SculkLatch.SCULK_JAW_HEALTH).with(IS_EATING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(IS_SCARED);
        builder.add(IS_EATING);
        builder.add(HEALTH);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context instanceof EntityShapeContext) {
            Entity entity = ((EntityShapeContext) context).getEntity();

            if (entity != null) {
                if (entity.getType().isIn(ModTags.SCULK_JAW_IMMUNE)) {
                    return FULL_COLLISION_SHAPE;
                }
                if (entity.isSneaking() && entity.squaredDistanceTo(pos.toCenterPos().add(0, 0.5,0)) > 0.039) {
                    return FULL_COLLISION_SHAPE;
                }
            }
        }
        if (state.get(IS_SCARED) || (!context.isAbove(COLLISION_SHAPE.offset(0, 0.3, 0), pos, true))) {
            return FULL_COLLISION_SHAPE;
        }
        return COLLISION_SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && !state.get(IS_SCARED) && !(entity.getType().isIn(ModTags.SCULK_JAW_IMMUNE))) {
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getAbilities().flying) {
                return;
            }
            entity.setSneaking(false);
            entity.damage(world.getDamageSources().create(ModDamageSources.SCULK_JAW), 1.0F);
            entity.addVelocity(pos.toCenterPos().add(0, -0.3, 0).subtract(entity.getPos()).multiply(0.3));
            entity.slowMovement(state, new Vec3d(0.5, 0.6, 0.5));
        }
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (state.get(IS_SCARED)) {
            world.setBlockState(pos, state.with(IS_SCARED, false).with(HEALTH, SculkLatch.SCULK_JAW_HEALTH));
        }
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!state.get(IS_SCARED)) {
            damageSculkJaw(player, pos, state);
            if (state.get(HEALTH) <= 0) {
                if (!world.isClient()) {
                    world.scheduleBlockTick(pos, this, 600);
                    world.setBlockState(pos, state.with(IS_SCARED, true).with(HEALTH, SculkLatch.SCULK_JAW_HEALTH));
                }
            }
        }
        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(IS_EATING) && !world.getEntitiesByClass(Entity.class, Box.of(pos.toCenterPos(), 0.5, 0.5, 0.5), EntityPredicates.EXCEPT_SPECTATOR).isEmpty()) {
            world.setBlockState(pos, state.with(IS_EATING, true));
        }
        if (state.get(IS_EATING) && world.getEntitiesByClass(Entity.class, Box.of(pos.toCenterPos(), 0.5, 0.5, 0.5), EntityPredicates.EXCEPT_SPECTATOR).isEmpty()) {
            world.setBlockState(pos, state.with(IS_EATING, false));
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    public void damageSculkJaw(PlayerEntity player, BlockPos pos, BlockState state) {
        float f = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        float g;
        g = EnchantmentHelper.getAttackDamage(player.getMainHandStack(), EntityGroup.UNDEAD);
        float h = player.getAttackCooldownProgress(0.5F);
        f *= 0.2F + h * h * 0.8F;
        g *= h;
        player.resetLastAttackedTicks();
        if (f > 0.0F || g > 0.0F) {
            boolean bl = h > 0.9F;
            boolean bl2 = player.isSprinting() && bl;
            boolean bl3 = bl && player.fallDistance > 0.0F && !player.isOnGround() && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
            bl3 = bl3 && !player.isSprinting();
            if (bl3) {
                f *= 1.5F;
            }

            f += g;
            boolean bl4 = false;
            double d = player.horizontalSpeed - player.prevHorizontalSpeed;
            if (bl && !bl3 && !bl2 && player.isOnGround() && d < (double) player.getMovementSpeed()) {
                ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);
                if (itemStack.getItem() instanceof SwordItem) {
                    bl4 = true;
                }
            }

            if (bl4) {
                float l = 1.0F + EnchantmentHelper.getSweepingMultiplier(player) * f;

                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                player.spawnSweepAttackParticles();


                if (player.squaredDistanceTo(pos.toCenterPos()) < 9.0) {
                    player.getWorld().setBlockState(pos, state.with(HEALTH, Math.max(0, state.get(HEALTH) - ((int) l * 5))));
                }
            }

            if (bl3) {
                player.getWorld().setBlockState(pos, state.with(HEALTH, Math.max(0, state.get(HEALTH) - ((int) f))));
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
            }

            if (!bl3 && !bl4) {
                player.getWorld().emitGameEvent(GameEvent.ENTITY_DAMAGE, pos, new GameEvent.Emitter(player, null));
                if (bl) {
                    player.getWorld().setBlockState(pos, state.with(HEALTH, Math.max(0, state.get(HEALTH) - ((int) f))));
                    player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
                } else {
                    player.getWorld().setBlockState(pos, state.with(HEALTH, Math.max(0, state.get(HEALTH) - ((int) f))));
                    player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
                }
            }

            if (!player.getWorld().isClient) {
                ((ServerWorld) player.getWorld()).spawnParticles(ParticleTypes.DAMAGE_INDICATOR, pos.getX(), pos.getY(), pos.getZ(), (int) f, 0.1, 0.0, 0.1, 0.2);
            }

            player.addExhaustion(0.1F);
        }
    }

}
