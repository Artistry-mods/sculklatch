package chaos.sculklatch.blocks.custom;

import chaos.sculklatch.blocks.blockEntities.ModBlockEntities;
import chaos.sculklatch.blocks.blockEntities.custom.SculkChestBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SculkChestBlock extends ChestBlock implements BlockEntityProvider {

    public static final BooleanProperty IS_SCARED = BooleanProperty.of("is_scared");
    private ServerPlayerEntity lastPlayerToOpen;
    public SculkChestBlock(Settings settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityTypeSupplier) {
        super(settings, blockEntityTypeSupplier);
        this.setDefaultState(this.stateManager.getDefaultState().with(IS_SCARED, false).with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(IS_SCARED);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (!state.get(IS_SCARED) && this.lastPlayerToOpen != null) {
            this.lastPlayerToOpen.closeHandledScreen();
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SculkChestBlockEntity(pos, state);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE, SculkChestBlockEntity::tick);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(IS_SCARED)) {
            if (!world.isClient()) {
                this.lastPlayerToOpen = (ServerPlayerEntity) player;
            }
            return super.onUse(state, world, pos, player, hand, hit);
        }
        return ActionResult.PASS;
    }
}
