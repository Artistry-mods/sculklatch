package chaos.sculklatch.blocks.blockEntities.custom;

import chaos.sculklatch.SculkLatch;
import chaos.sculklatch.blocks.blockEntities.ModBlockEntities;
import chaos.sculklatch.blocks.custom.SculkChestBlock;
import chaos.sculklatch.gameevent.ModGameEvents;
import chaos.sculklatch.items.custom.AmethystBellItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SculkChestBlockEntity extends ChestBlockEntity implements GameEventListener.Holder<Vibrations.VibrationListener>, Vibrations {
    private final Vibrations.Callback callback;
    private final Vibrations.VibrationListener listener;
    private final ChestLidAnimator lidAnimator;
    private final Vibrations.ListenerData listenerData;
    private int scaredTimer = 0;

    public SculkChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE, pos, state);
        this.callback = this.createCallback();
        this.listenerData = new Vibrations.ListenerData();
        this.listener = new Vibrations.VibrationListener(this);
        this.lidAnimator = new ChestLidAnimator();
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, SculkChestBlockEntity sculkChestBlockEntity) {
        sculkChestBlockEntity.lidAnimator.step();
        if (!blockState.isAir()) {
            if (sculkChestBlockEntity.scaredTimer > 0) {
                sculkChestBlockEntity.scaredTimer--;
                if (sculkChestBlockEntity.scaredTimer == 0) {
                    world.setBlockState(blockPos, blockState.with(SculkChestBlock.IS_SCARED, false));
                }
            }
        }
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.lidAnimator.setOpen(data > 0);
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    @Override
    public float getAnimationProgress(float tickDelta) {
        return this.lidAnimator.getProgress(tickDelta);
    }

    public Vibrations.Callback createCallback() {
        return new VibrationCallback(this.pos);
    }

    @Override
    public Vibrations.VibrationListener getEventListener() {
        return this.listener;
    }

    @Override
    public Vibrations.ListenerData getVibrationListenerData() {
        return this.listenerData;
    }

    public Vibrations.Callback getVibrationCallback() {
        return this.callback;
    }


    protected class VibrationCallback implements Vibrations.Callback {
        public static final int RANGE = SculkLatch.SCULK_CHEST_RANGE;
        protected final BlockPos pos;
        private final PositionSource positionSource;

        public VibrationCallback(BlockPos pos) {
            this.pos = pos;
            this.positionSource = new BlockPositionSource(pos);
        }

        public int getRange() {
            return RANGE;
        }

        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean accepts(ServerWorld world, BlockPos pos, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter) {
            if (Objects.equals(event, ModGameEvents.AMETHYST_BELL_HIT) && emitter != null && emitter.sourceEntity() instanceof LivingEntity) {
                for (ItemStack itemStack : ((LivingEntity) emitter.sourceEntity()).getHandItems()) {
                    if (itemStack.getItem() instanceof AmethystBellItem) {
                        if (!SculkChestBlockEntity.this.hasCustomName()) {
                            world.setBlockState(this.pos, world.getBlockState(this.pos).with(SculkChestBlock.IS_SCARED, true));
                            world.playSound(null, this.pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 1F, 1.2F);
                            SculkChestBlockEntity.this.scaredTimer = SculkLatch.SCARED_TIMER;
                            return (!pos.equals(this.pos) || event != GameEvent.BLOCK_DESTROY && event != GameEvent.BLOCK_PLACE);
                        }
                        if (Objects.equals(Objects.requireNonNull(SculkChestBlockEntity.this.getCustomName()).getString(), itemStack.getName().getString())) {
                            world.setBlockState(this.pos, world.getBlockState(this.pos).with(SculkChestBlock.IS_SCARED, true));
                            world.playSound(null, this.pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS, 1F, 1.2F);
                            SculkChestBlockEntity.this.scaredTimer = 40;
                            return (!pos.equals(this.pos) || event != GameEvent.BLOCK_DESTROY && event != GameEvent.BLOCK_PLACE);
                        }
                    }
                }
            }
            return (!pos.equals(this.pos) || event != GameEvent.BLOCK_DESTROY && event != GameEvent.BLOCK_PLACE);
        }

        @Override
        public void accept(ServerWorld world, BlockPos pos, RegistryEntry<GameEvent> event, @Nullable Entity sourceEntity, @Nullable Entity entity, float distance) {

        }

        @Override
        public boolean canAccept(RegistryEntry<GameEvent> gameEvent, GameEvent.Emitter emitter) {
            if (!gameEvent.isIn(this.getTag())) {
                return false;
            } else {
                Entity entity = emitter.sourceEntity();
                if (entity != null) {
                    if (entity.isSpectator()) {
                        return false;
                    }

                    if (entity.occludeVibrationSignals()) {
                        return false;
                    }
                }

                if (emitter.affectedState() != null) {
                    return !emitter.affectedState().isIn(BlockTags.DAMPENS_VIBRATIONS);
                } else {
                    return true;
                }
            }
        }

        public boolean triggersAvoidCriterion() {
            return true;
        }

        public void onListen() {
            SculkChestBlockEntity.this.markDirty();
        }

        public boolean requiresTickingChunksAround() {
            return true;
        }
    }
}
