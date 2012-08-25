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
package com.timalmdal.bukkit.slopes.designers;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.block.design.SubTexture;
import org.getspout.spoutapi.block.design.Texture;

import com.timalmdal.bukkit.slopes.util.RotatedTextureOffset;
import com.timalmdal.bukkit.slopes.util.SlopeSubTexture;
import com.timalmdal.bukkit.utilities.blockdesign.Point;
import com.timalmdal.bukkit.utilities.blockdesign.QuadDefinition;
import com.timalmdal.bukkit.utilities.blockdesign.QuadList;

public class SlopeBlockDesign extends GenericBlockDesign {

	private final SubTexture subTexture;

	public SlopeBlockDesign(final Plugin plugin, final Texture texture, final SlopeSubTexture slopeTexture, final QuadList quadList) {
		super();
		setTexture(plugin, texture);
		setBoundingBox(.25f, 0f, .25f, .75f, .5f, .75f);
		subTexture = texture.getSubTexture(slopeTexture.getTextureIndex());

		generate(quadList);

		setRenderPass(1);
	}

	public void generate(final QuadList quadList) {
		setQuadNumber(quadList.size());

		final int defaultOffset = RotatedTextureOffset.Default.getOffset();
		for (final QuadDefinition quad : quadList) {
			int order = 0;
			final SubTexture sideTexture = defaultOffset == quad.getTextureOffset() ? subTexture : getSubTexture(quad.getTextureOffset());

			for (final Point descriptor : quad) {
				this.setVertex(descriptor.setOrder(order++).setQuad(quad.getQuadIndex()).generateVertex(sideTexture));
			}
		}
	}

	public SubTexture getSubTexture(final int textureOffset) {
		final Texture texture = subTexture.getParent();

		int index = textureOffset;
		for (final SubTexture subtexture : texture.subTextures) {
			if (subTexture.equals(subtexture)) {
				break;
			}
			index++;
		}
		return texture.getSubTexture(index);
	}
}
