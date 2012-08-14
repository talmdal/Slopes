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

import java.util.Arrays;
import java.util.Iterator;

import org.getspout.spoutapi.block.design.SubTexture;
import org.getspout.spoutapi.block.design.Texture;

class QuadDefinition implements Iterable<Point> {
	private final TextureOffset textureOffset;
	private final Point[] points;
	private int quadIndex;

	public QuadDefinition(final TextureOffset textureOffset, final Point[] points) {
		this.textureOffset = textureOffset;
		this.points = Arrays.copyOf(points, points.length);
	}

	public int getQuadIndex() {
		return quadIndex;
	}

	public QuadDefinition setQuadIndex(final int quadIndex) {
		this.quadIndex = quadIndex;
		return this;
	}

	public TextureOffset getTextureOffset() {
		return textureOffset;
	}

	@Override
	public Iterator<Point> iterator() {
		return new Iterator<Point>() {
			private int iteratorIndex = 0;

			@Override
			public boolean hasNext() {
				return iteratorIndex < points.length;
			}

			@Override
			public Point next() {
				return points[iteratorIndex++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public SubTexture getSubTexture(final SubTexture subTexture) {
		final Texture texture = subTexture.getParent();

		int index = 0;
		for (final SubTexture subtexture : texture.subTextures) {
			if (subTexture.equals(subtexture)) {
				break;
			}
			index++;
		}
		return texture.getSubTexture(index + textureOffset.getOffset());
	}

}