/*
 * This file is part of slopes.
 *
 * Copyright (c) 2012, Tim Almdal <http://www.timalmdal.com/slopes/>
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
package com.timalmdal.bukkit.slopes.util;

import java.util.EnumSet;

import org.getspout.spoutapi.material.Block;
import org.getspout.spoutapi.material.MaterialData;

public enum SlopeSubTexture {
	WOOD(0, MaterialData.wood),
	COBBLESTONE(1, MaterialData.cobblestone),
	GLASS(2, MaterialData.glass),
	SANDSTONE(3, MaterialData.sandstone),
	DIRT(4, MaterialData.dirt),
	STONE(5, MaterialData.stone),
	SAND(6, MaterialData.sand),
	SNOW(7, MaterialData.snow),
	GRASS(8, MaterialData.grass),
	BRICK(9, MaterialData.brick),
	GRAVEL(10, MaterialData.gravel),
	GOLD(11, MaterialData.goldBlock),
	IRON(12, MaterialData.ironBlock),
	OBSIDIAN(13, MaterialData.obsidian),
	DIAMOND(14, MaterialData.diamondBlock),
	NETHERBRICK(15, MaterialData.netherBrick);

	private final int textureIndex;
	private final Block sourceBlock;

	private SlopeSubTexture(final int textureIndex, final Block sourceBlock) {
		this.textureIndex = textureIndex;
		this.sourceBlock = sourceBlock;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public Block getSourceBlock() {
		return sourceBlock;
	}

	/**
	 * Format the display name by inserting the material name into the specified pattern.
	 * @param namePattern
	 * @return material name
	 */
	public String getDisplayName(final String namePattern) {
		final StringBuilder displayName = new StringBuilder(name().toLowerCase());
		displayName.setCharAt(0, Character.toUpperCase(displayName.charAt(0)));
		return String.format(namePattern, displayName.toString());
	}

	public boolean isOpaque() {
		return sourceBlock.isOpaque();
	}

	public static EnumSet<SlopeSubTexture> getExtendedMaterialSet() {
		return EnumSet.of(
						SlopeSubTexture.GLASS, SlopeSubTexture.SANDSTONE, SlopeSubTexture.DIRT, SlopeSubTexture.SAND,
						SlopeSubTexture.SNOW, SlopeSubTexture.GRASS, SlopeSubTexture.GRAVEL, SlopeSubTexture.GOLD,
						SlopeSubTexture.IRON, SlopeSubTexture.OBSIDIAN, SlopeSubTexture.DIAMOND, SlopeSubTexture.STONE);
	}

	public static EnumSet<SlopeSubTexture> getAllMaterialSet() {
		return EnumSet.allOf(SlopeSubTexture.class);
	}

}
