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
package com.timalmdal.bukkit.slopes.blocks;

import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.block.design.Texture;

import com.timalmdal.bukkit.slopes.util.SlopeSubTexture;
import com.timalmdal.bukkit.slopes.util.RotatedTextureOffset;
import com.timalmdal.bukkit.utilities.blockdesign.Point;
import com.timalmdal.bukkit.utilities.blockdesign.QuadList;

public final class SlopedCeiling extends AbstractBlock {
	private static final QuadList QUAD_LIST = QuadList.quadBuilder()
		.add(RotatedTextureOffset.Top.getOffset(),
			new Point(0.0f, 1.0f, 0.0f), new Point(0.0f, 1.0f, 1.0f), new Point(1.0f, 1.0f, 1.0f), new Point(1.0f, 1.0f, 0.0f)) // top
		.add(new Point(1.0f, 1.0f, 0.0f), new Point(0.0f, 0.001f, 0.0f), new Point(0.0f, 0.0f, 0.0f), new Point(0.0f, 1.0f, 0.0f)) // north
		.add(new Point(0.0f, 1.0f, 0.0f), new Point(0.0f, 0.0f, 0.0f), new Point(0.0f, 0.0f, 1.0f), new Point(0.0f, 1.0f, 1.0f)) // east
		.add(RotatedTextureOffset.Top.getOffset(),
			new Point(0.0f, 1.0f, 1.0f), new Point(0.0f, 0.001f, 1.0f), new Point(0.0f, 0.0f, 1.0f), new Point(1.0f, 1.0f, 1.0f)) // south
		.add(new Point(1.0f, 1.0f, 1.0f), new Point(0.0f, 0.0f, 1.0f), new Point(0.0f, 0.0f, 0.0f), new Point(1.0f, 1.0f, 0.0f)) // west
	;

	public SlopedCeiling(final JavaPlugin plugin, final Texture texture, final SlopeSubTexture slopeTexture) {
		super(plugin, slopeTexture.getDisplayName("Sloped %s Ceiling"), QUAD_LIST, texture, slopeTexture);
	}

	@Override
	public String[] getRecipe() {
		return new String[] { "AAA", " A ", "  A" };
	}
}
