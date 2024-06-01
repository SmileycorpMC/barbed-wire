package net.smileycorp.barbedwire.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.smileycorp.barbedwire.common.BarbedWireContent;
import net.smileycorp.barbedwire.common.BarbedWireMaterial;
import net.smileycorp.barbedwire.common.Constants;
import net.smileycorp.barbedwire.common.tile.TileBarbedWire;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBarbedWire extends Block implements ITileEntityProvider {
    
    public static PropertyEnum<EnumAxis> AXIS = PropertyEnum.create("axis", EnumAxis.class);
    public static Properties.PropertyAdapter<Boolean> IS_ENCHANTED = new Properties.PropertyAdapter<>(PropertyBool.create("is_enchanted"));
    
    private final BarbedWireMaterial material;
    
    public BlockBarbedWire(BarbedWireMaterial material) {
        super(Material.ROCK);
        setCreativeTab(BarbedWireContent.CREATIVE_TAB);
        String name = material.getUnlocalisedName() + "_Barbed_Wire";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setDefaultState(blockState.getBaseState().withProperty(AXIS, EnumAxis.X));
        setHarvestLevel("pickaxe", material.getHarvestLevel());
        setHardness(0.3F);
        this.material = material;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.removeTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return material.createTileEntity();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        //slow entities
        entity.setInWeb();
        if (world.getTileEntity(pos) instanceof TileBarbedWire & !world.isRemote) {
            //tick damage on server
            TileBarbedWire te = (TileBarbedWire) world.getTileEntity(pos);
            if (entity instanceof EntityXPOrb && te.hasEnchantment(Enchantments.MENDING)) {
                EntityXPOrb orb = (EntityXPOrb) entity;
                int i = Math.min(roundAverage(orb.xpValue * 2), te.getDurability());
                orb.xpValue -= roundAverage(i / 2);
                te.setDurability(te.getDurability() - i);
            }
            if (te.getOrUpdateCooldown() == 0) te.causeDamage();
            //break barbed wire
            if (te.getDurability() <= 0) world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }
    
    private static int roundAverage(float value) {
        double floor = Math.floor(value);
        return (int) floor + (Math.random() < value - floor ? 1 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{AXIS}, new IUnlistedProperty[]{IS_ENCHANTED});
    }

    //hook for enchanted barbed wire rendering, probably not needed as we now use a tesr instead of baked model
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileBarbedWire) {
            return ((IExtendedBlockState) state).withProperty(IS_ENCHANTED, ((TileBarbedWire) te).isEnchanted());
        }
        return ((IExtendedBlockState) state).withProperty(IS_ENCHANTED, false);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.getTileEntity(pos) instanceof TileBarbedWire & !placer.world.isRemote) {
            TileBarbedWire tile = ((TileBarbedWire) world.getTileEntity(pos));
            if (stack.hasTagCompound()) {
                NBTTagCompound nbt = stack.getTagCompound();
                //sync durability with item
                tile.setDurability(stack.getItemDamage());
                //add enchantments from item
                if (nbt.hasKey("ench")) {
                    for (NBTBase tag : nbt.getTagList("ench", 10)) {
                        int level = ((NBTTagCompound) tag).getShort("lvl");
                        Enchantment enchant = Enchantment.getEnchantmentByID(((NBTTagCompound) tag).getShort("id"));
                        tile.applyEnchantment(enchant, level);
                    }
                }
            }
            //set the player as the owner to remove self and team damage
            if (placer instanceof EntityPlayer) tile.setOwner((EntityPlayer) placer);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
        EnumAxis axis = EnumAxis.fromVector(player.getLookVec());
        return getStateFromMeta(player.getHeldItem(hand).getMetadata()).withProperty(AXIS, axis);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumAxis axis = meta > 2 ? EnumAxis.Z : EnumAxis.X;
        return getDefaultState().withProperty(AXIS, axis);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AXIS).ordinal();
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }


    //replace vanilla behaviour so we can modify the dropped items
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);
        //drop barbed wire with nbt when silk touch is used
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            ItemStack drop = ((TileBarbedWire) te).getDrop();
            spawnAsEntity(world, pos, drop);
        } else {
            //drop an amount of nuggets based on amount of remaining durability
            ItemStack item = material.getDrop();
            item.setCount((int)((double) ((TileBarbedWire) te).getDurability() / (double) material.getDurability() * 7d));
            spawnAsEntity(world, pos, item);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    @Override
    public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity) {
        return (entity instanceof EntityVindicator ? PathNodeType.DAMAGE_CACTUS : super.getAiPathNodeType(state, world, pos, entity));
    }
    
    public BarbedWireMaterial getMaterial() {
        return material;
    }
    
    public enum EnumAxis implements IStringSerializable {
        X,
        Z;
    
        @Override
        public String getName() {
            return this.toString().toLowerCase();
        }
    
        public static EnumAxis fromVector(Vec3d vec) {
            return fromFacing(EnumFacing.getFacingFromVector((float) vec.x, 0f, (float) vec.z));
        }
    
        public static EnumAxis fromFacing(EnumFacing facing) {
            if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) return Z;
            return X;
        }
    
    }
}
