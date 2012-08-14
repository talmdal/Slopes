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
package com.timalmdal.bukkit.slopes.util;

import java.util.ArrayList;
import java.util.List;

import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.block.design.SubTexture;

public class QuadList {
	private final List<QuadDefinition> quads = new ArrayList<QuadDefinition>();

	public static QuadList quadBuilder() {
		return new QuadList();
	}

	public QuadList add(final Point... vertices) {
		return add(TextureOffset.Default, vertices);
	}

	public QuadList add(final TextureOffset textureOffset, final Point... vertices) {
		if (vertices.length < 3 || vertices.length > 4) {
			throw new IllegalArgumentException("Invalid vertex index: " + vertices.length);
		}

		final QuadDefinition quadDefn = new QuadDefinition(textureOffset, vertices).setQuadIndex(quads.size());
		quads.add(quadDefn);

		return this;
	}

	public int size() {
		return quads.size();
	}

	public void generate(final GenericBlockDesign designer, final SubTexture subTexture) {
		for (final QuadDefinition quad : quads) {
			int order = 0;
			final SubTexture sideTexture = TextureOffset.Default.equals(quad.getTextureOffset()) ? subTexture : quad.getSubTexture(subTexture);

			for (final Point descriptor : quad) {
				designer.setVertex(descriptor.setOrder(order++).setQuad(quad.getQuadIndex()).generateVertex(sideTexture));
			}
		}
	}
}
