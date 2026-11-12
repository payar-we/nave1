package com.navecraft;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Objetos y crafteos para armar la Nave Espacial (la nave en si viene del mod ghostsafio). */
public class NaveCraft implements ModInitializer {
	public static final String MOD_ID = "navecraft";

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	private static Item item(String name) {
		return Registry.register(BuiltInRegistries.ITEM, id(name), new Item(new Item.Properties()));
	}

	public static final ResourceLocation NAVE_ID = ResourceLocation.fromNamespaceAndPath("ghostsafio", "nave");

	/** Plataforma donde se coloca la nave (la nave solo se puede poner sobre ella). */
	public static final Block PLATAFORMA_NAVE = Registry.register(BuiltInRegistries.BLOCK, id("plataforma_nave"),
			new Block(BlockBehaviour.Properties.of().strength(3.0F, 6.0F).sound(SoundType.METAL)));
	public static final Item PLATAFORMA_NAVE_ITEM = Registry.register(BuiltInRegistries.ITEM, id("plataforma_nave"),
			new BlockItem(PLATAFORMA_NAVE, new Item.Properties()));

	public static final Item DILITIO = item("dilitio");
	public static final Item AMBAR_CON_INSECTO = item("ambar_con_insecto");
	public static final Item CAPSULA_DILITIO = item("capsula_dilitio");
	public static final Item CABINA_NAVE = item("cabina_nave");
	public static final Item ALA_IZQUIERDA_NAVE = item("ala_izquierda_nave");
	public static final Item ALA_DERECHA_NAVE = item("ala_derecha_nave");
	public static final Item FUSELAJE_NAVE = item("fuselaje_nave");
	public static final Item MOTOR_NAVE = item("motor_nave");
	public static final Item MOTOR_MEJORADO_NAVE = item("motor_mejorado_nave");

	@Override
	public void onInitialize() {
		// la nave solo se puede colocar sobre la cara de arriba de la plataforma
		UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
			ItemStack stack = player.getItemInHand(hand);
			if (!BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(NAVE_ID)) return InteractionResult.PASS;
			boolean sobrePlataforma = hit.getDirection() == Direction.UP
					&& level.getBlockState(hit.getBlockPos()).is(PLATAFORMA_NAVE);
			if (sobrePlataforma) return InteractionResult.PASS;
			if (!level.isClientSide) {
				player.displayClientMessage(Component.translatable("message.navecraft.necesita_plataforma"), true);
			}
			return InteractionResult.FAIL;
		});

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("nave"),
				FabricItemGroup.builder()
						.title(Component.translatable("itemGroup.navecraft.nave"))
						.icon(() -> new ItemStack(MOTOR_NAVE))
						.displayItems((params, out) -> {
							out.accept(PLATAFORMA_NAVE_ITEM);
							out.accept(DILITIO);
							out.accept(AMBAR_CON_INSECTO);
							out.accept(CAPSULA_DILITIO);
							out.accept(CABINA_NAVE);
							out.accept(ALA_IZQUIERDA_NAVE);
							out.accept(ALA_DERECHA_NAVE);
							out.accept(FUSELAJE_NAVE);
							out.accept(MOTOR_NAVE);
							out.accept(MOTOR_MEJORADO_NAVE);
							// la nave que viene de ghostsafio
							Item nave = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("ghostsafio", "nave"));
							if (nave != Items.AIR) out.accept(nave);
						})
						.build());
	}
}
