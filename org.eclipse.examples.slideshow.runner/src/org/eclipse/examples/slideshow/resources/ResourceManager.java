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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.printing.Printer;

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
		
		// TODO DPI value answered by GTK not correct. See Printer#getDPI()
		// TODO Shouldn't have to screw around with dpi. Does this work on Windows?
		int dpi = device instanceof Printer ? 300 : device.getDPI().x;
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

	// TODO Consider threading issues
	public Image getImage(URL url) {
		String key = url.toString();
		Image image = imageRegistry.get(key);
		if (image != null) return image;
		
		InputStream in = null;
		try {
			URLConnection connection = url.openConnection();
			in = connection.getInputStream();
			image = new Image(device, in);
			imageRegistry.put(key, image);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
