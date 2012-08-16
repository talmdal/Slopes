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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spout.Spout;
import org.getspout.spoutapi.SpoutManager;

public enum FileUtilities {
	;

	public static File getTempResourceFile(final JavaPlugin plugin, final String fileName) {
		final File tempDirectory = new File(Spout.getInstance().getDataFolder(), "temp/" + plugin.getName());
		if (!tempDirectory.exists()) {
			tempDirectory.mkdirs();
		}
		return new File(tempDirectory, fileName);
	}

	public static File loadResource(final JavaPlugin plugin, final String resourceName) throws IOException {
		final File resourceFile = getTempResourceFile(plugin, resourceName);

		fastChannelCopy(Channels.newChannel(FileUtilities.class.getClassLoader().getResourceAsStream(resourceName)),
			Channels.newChannel(new FileOutputStream(resourceFile)));

		return resourceFile;
	}

	public static void fastChannelCopy(final ReadableByteChannel resourceSource, final WritableByteChannel resourceTarget) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(32 * 1024 * 1024);
		while (resourceSource.read(buffer) != -1) {
			buffer.flip();
			resourceTarget.write(buffer);
			buffer.compact();
		}

		buffer.flip();
		while (buffer.hasRemaining()) {
			resourceTarget.write(buffer);
		}
		resourceSource.close();
		resourceTarget.close();
	}

	public static String loadAndCacheResource(final JavaPlugin plugin, final String resourceName) throws IOException {
		final File resourceFile = FileUtilities.loadResource(plugin, resourceName);
		SpoutManager.getFileManager().addToCache(plugin, resourceFile);

		return resourceFile.getPath();
	}
}
