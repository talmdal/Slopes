/*
 * This file is part of slopes.
 *
 * Copyright (c) 2012, Tim Almdal <http://www.timalmdal.com/>
 * slopes is licensed under the GNU Lesser General Public License.
 * This version of slopes is derived from Kaevator's Superslopes (http://goo.gl/Rd7io)
 * and retsrif's original Spout port (https://github.com/retsrif/Slopes)
 *
 * slopes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * slopes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.timalmdal.bukkit.slopes;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;

import com.timalmdal.bukkit.slopes.blocks.AbstractBlock;
import com.timalmdal.bukkit.slopes.blocks.CeilingAngle;
import com.timalmdal.bukkit.slopes.blocks.CeilingSlantedCorner;
import com.timalmdal.bukkit.slopes.blocks.CeilingStairs;
import com.timalmdal.bukkit.slopes.blocks.Corner;
import com.timalmdal.bukkit.slopes.blocks.Intersection;
import com.timalmdal.bukkit.slopes.blocks.InvertedIntersection;
import com.timalmdal.bukkit.slopes.blocks.InvertedObliqueSlope;
import com.timalmdal.bukkit.slopes.blocks.ObliqueSlope;
import com.timalmdal.bukkit.slopes.blocks.SlantedCorner;
import com.timalmdal.bukkit.slopes.blocks.SlopedAngle;
import com.timalmdal.bukkit.slopes.blocks.SlopedCeiling;
import com.timalmdal.bukkit.slopes.blocks.SlopedFloor;
import com.timalmdal.bukkit.slopes.blocks.Stairs;
import com.timalmdal.bukkit.slopes.util.FileUtilities;
import com.timalmdal.bukkit.slopes.util.SlopeSubTexture;

public class SlopesPlugin extends JavaPlugin {
	private static final String TEXTURE_IMAGES = "Slopes.png";

	/**
	 * Internal class that overrides get to lazily create the list of blocks for
	 * this material if it doesn't exist.
	 */
	private static class SlopeBlockMap extends HashMap<SlopeSubTexture, List<AbstractBlock>> {
		private static final long serialVersionUID = 1L;

		@Override
		public List<AbstractBlock> get(final Object material) {
			List<AbstractBlock> materialBlocks = super.get(material);
			if (materialBlocks == null) {
				materialBlocks = new ArrayList<AbstractBlock>();
				super.put((SlopeSubTexture) material, materialBlocks);
			}
			return materialBlocks;
		}
	}

	private static SlopeBlockMap blockMap = new SlopeBlockMap();

	private static SlopesPlugin instance;

	public static SlopesPlugin getPlugin() {
		return instance;
	}

	private Texture texture;
	private String textureImagePath;

	public SlopesPlugin() {
		super();
		instance = this;
	}

	@Override
	public void onDisable() {
		if (textureImagePath != null) {
			SpoutManager.getFileManager().removeFromCache(this, textureImagePath);
			textureImagePath = null;
		}
		getLogger().info("[Slopes] disabled.");
	}

	@Override
	public void onEnable() {
		try {
			textureImagePath = FileUtilities.loadAndCacheResource(this, TEXTURE_IMAGES);
			texture = new Texture(this, textureImagePath, 256, 64, 16);

			// Generate the blocks that are similiar to minecraft and only need to
			// generate in other materials
			generateBlocksForMaterials(SlopeSubTexture.getExtendedMaterialSet(), Stairs.class, CeilingStairs.class);

			// Generate the blocks that have no minecraft equivalent and need all
			// material generated
			generateBlocksForMaterials(SlopeSubTexture.getAllMaterialSet(),
				Corner.class, Intersection.class, InvertedIntersection.class, ObliqueSlope.class, InvertedObliqueSlope.class,
				SlantedCorner.class, CeilingSlantedCorner.class, SlopedAngle.class, CeilingAngle.class,
				SlopedFloor.class, SlopedCeiling.class);

			getLogger().info("[Slopes] enabled.");
		} catch (final IOException e) {
			getLogger().log(Level.SEVERE, "Problem initializing textures for [Slopes]: ", e);
		}
	}

	/**
	 * Generate the specified blocks for all the materials
	 * 
	 * @param blockClasses
	 *            list of block classes to generate
	 */
	@SuppressWarnings("unchecked")
	private void generateBlocksForMaterials(final EnumSet<SlopeSubTexture> materialSet, final Class<?>... blockClasses) {
		for (final SlopeSubTexture material : materialSet) {
			final List<AbstractBlock> materialBlocks = blockMap.get(material);
			for (final Class<?> blockClass : blockClasses) {
				materialBlocks.add(createBlockForSubTexture((Class<? extends AbstractBlock>) blockClass, material));
			}
		}
	}

	/**
	 * Generate a block represented by blockClass using the specified material
	 * 
	 * @param blockClass
	 *            Class to instantiate
	 * @param material
	 *            material the block is made from
	 * @return prototype block.
	 */
	private <T extends AbstractBlock> T createBlockForSubTexture(final Class<T> blockClass, final SlopeSubTexture material) {
		try {
			final Constructor<T> constructor = blockClass.getConstructor(JavaPlugin.class, Texture.class, SlopeSubTexture.class);
			final T slopeBlock = constructor.newInstance(this, texture, material);

			final ItemStack itemStack = new SpoutItemStack(slopeBlock, 4);
			SpoutManager.getMaterialManager().registerSpoutRecipe(new SpoutShapedRecipe(itemStack).shape(slopeBlock.getRecipe()).setIngredient('A', material.getSourceBlock()));

			return slopeBlock;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			getLogger().log(Level.SEVERE, "Problem creating block: " + blockClass.getSimpleName(), e);
		}
		return null;
	}
}