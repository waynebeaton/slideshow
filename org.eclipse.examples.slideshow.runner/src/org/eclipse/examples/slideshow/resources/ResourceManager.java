/*******************************************************************************
 * Copyright (c) 2009 The Eclipse Foundation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package org.eclipse.examples.slideshow.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Instances of the {@link ResourceManager} class handling the allocation (and
 * disposal) of system resources such as {@link Font}s, {@link Color}s and
 * {@link Image}s.
 * 
 * <p>
 * Warning: The current implementation is not threadsafe.
 * 
 * <p>
 * Instances of this class should be sent the {@link #dispose()} message when
 * they are no longer required.
 */
public class ResourceManager {

	private final Device device;
	private Map<FontDescription, Font> fontRegistry = new HashMap<FontDescription, Font>();
	private Map<String, Image> imageRegistry = new HashMap<String, Image>();

	public ResourceManager(Device device) {
		this.device = device;
	}

	public Font getFont(String name, int size, int style) {
		return getFont(new FontDescription(name, size, style));
	}

	// TODO Consider threading implications.
	public Font getFont(FontDescription description) {
		Font font = fontRegistry.get(description);
		if (font != null) return font;
		
		int height = description.getHeight();
		font = new Font(device, description.getName(), height, description.getStyle());
		fontRegistry.put(description, font);
		
		return font;
	}

	// TODO Consider threading issues.
	public void dispose() {
		for (Font font : fontRegistry.values()) {
			font.dispose();
		}
		fontRegistry.clear();
		
		for (Image image : imageRegistry.values()) {
			image.dispose();
		}
		imageRegistry.clear();
	}

	/**
	 * This method obtains the image found at the location described by
	 * {@link URL}.
	 * 
	 * @param url
	 *            location of the image.
	 * @return an instance of {@link Image}
	 * @throws IOException
	 *             if the {@link URL} is invalid, or other stream-related
	 *             exception occurs.
	 * @throws InvalidImageException
	 *             if the attempt to obtain the image results in an
	 *             {@link SWTException} (most likely as a result of an
	 *             unsupported format, or missing image).
	 */
	public Image getImage(URL url) throws IOException, InvalidImageException {
		// TODO Consider threading issues
		String key = url.toString();
		Image image = imageRegistry.get(key);
		if (image != null) return image;
		
		InputStream in = null;
		try {
			URLConnection connection = url.openConnection();
			in = connection.getInputStream();
			image = new Image(device, in);
			imageRegistry.put(key, image);
		} catch (SWTException e) {
			throw new InvalidImageException(e);
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				/*
				 * Only throw the exception if we were unsuccessful in obtaining an image.
				 * I'm not sure under which conditions this might happen, but handle it
				 * anyway.
				 */
				if (image == null) throw e;
			}
		}
		
		return image;
	}

	public Image getPlaceholderImage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This is a convenience method that exists to make it slightly easier to
	 * obtain a system colour.
	 * 
	 * @param id
	 *            an {@link SWT} constant, e.g. {@link SWT#COLOR_BLACK}
	 * @return the instance of {@link Color} that corresponds to the provided
	 *         id.
	 */
	public Color getSystemColor(int id) {
		return device.getSystemColor(id);
	}
}
