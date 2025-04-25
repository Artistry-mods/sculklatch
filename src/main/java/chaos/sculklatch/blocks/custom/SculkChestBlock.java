package chaos.sculklatch.blocks.custom;

import chaos.sculklatch.blocks.entities.ModBlockEntities;
import chaos.sculklatch.blocks.entities.custom.SculkChestBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SculkChestBlock extends ChestBlock implements BlockEntityProvider, SculkSpreadable {

    public static final BooleanProperty IS_SCARED = BooleanProperty.of("is_scared");
    private ServerPlayerEntity lastPlayerToOpen;

    public SculkChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityTypeSupplier, Settings settings) {
        super(blockEntityTypeSupplier, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(IS_SCARED, false).with(FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(IS_SCARED);
    }


    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        if (state.get(IS_SCARED)) {
            System.out.println("breaking");
            return super.calcBlockBreakingDelta(state, player, world, pos) * 20f; // Hardness when powered
        } else {
            return super.calcBlockBreakingDelta(state, player, world, pos); // Default hardness
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (!state.get(IS_SCARED) && this.lastPlayerToOpen != null) {
            this.lastPlayerToOpen.closeHandledScreen();
        }
    }

    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        return Items.CHEST.getDefaultStack();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SculkChestBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.SCULK_CHEST_BLOCK_ENTITY_TYPE, SculkChestBlockEntity::tick);
    }



    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(IS_SCARED)) {
            if (!world.isClient()) {
                this.lastPlayerToOpen = (ServerPlayerEntity) player;
            }
            return super.onUse(state, world, pos, player, hit);
        }
        return ActionResult.PASS;
    }

    @Override
    public int spread(SculkSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, SculkSpreadManager spreadManager, boolean shouldConvertToBlock) {
        return cursor.getCharge();
    }
}
