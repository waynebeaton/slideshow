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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	 * This method obtains the image found at the location described by the
	 * combination of the baseUrl and file parameters. The baseUrl parameter can
	 * be <code>null</code>; in this case, the file parameter is assumed to
	 * contain an absolute URL. If the file parameter contains an absolute URL,
	 * the baseUrl is ignored.
	 * <p>
	 * Fetches only happen once. When the image has been successfully obtained,
	 * it is cached and the cached version is returned on subsequent requests.
	 * Note that all life-cycle management of the image is the responsibility of
	 * this instance. Callers must <em>not</em> dispose this instance
	 * themselves.
	 * <p>
	 * Note that this method will first combine the baseUrl and file and try to
	 * use the combination to locate the image resource. If the image cannot be
	 * found at the combined URL, it guesses attempts to determine the MediaWiki
	 * location of the resource.
	 * 
	 * @see #getMediaWikiImageUrl(URL, String)
	 * 
	 * @param baseUrl
	 *            the base &quot;directory&quot; where we find most of our
	 *            resources. If this value is <code>null</code>, then the
	 *            &quot;file&quot; field is assumed to contain an absolute
	 *            address.
	 * @param file
	 *            location of the image. Can be relative to the
	 *            &quot;baseUrl&quot; or be an absolute URL. Must not be
	 *            <code>null</code>.
	 * @return an instance of {@link Image}
	 * @throws IOException
	 *             if the {@link URL} is invalid, or other stream-related
	 *             exception occurs.
	 * @throws InvalidImageException
	 *             if the attempt to obtain the image results in an
	 *             {@link SWTException} (most likely as a result of an
	 *             unsupported format, or missing image).
	 */
	public Image getImage(URL baseUrl, String file) throws IOException, InvalidImageException {
		URL url = baseUrl == null ? new URL(file) : new URL(baseUrl, file);
		String key = url.toString();
		Image image = imageRegistry.get(key);
		if (image != null) return image;
		
		try {
			image = obtainImage(url);
		} catch (InvalidImageException e) {
			try {
				image = obtainImage(getMediaWikiImageUrl(baseUrl, file));
			} catch (NoSuchAlgorithmException e1) {
				// TODO This shouldn't happen, but consider options anyway.
			}
		}

		imageRegistry.put(key, image);
		return image;
	}

	/**
	 * This method computes a likely MediaWiki URL for the image.
	 * <p>
	 * In MediaWiki, an image tag of the form &quot;[[Image:STEMUSA.png]]&quot; 
	 * results in a URL of the form &quot;http://wiki.eclipse.org/images/f/f3/STEMUSA.png&quot;. 
	 * The directory &quot;f/f3&quot; is of the form &quot;x/xy&quot; where xy are the two 
	 * first characters of the md5 hash of the final image filename.
	 * <p>
	 * More information: http://www.mediawiki.org/wiki/Manual:Image_Administration
	 * 
	 * @param baseUrl
	 * @param file
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws MalformedURLException 
	 */
	private URL getMediaWikiImageUrl(URL baseUrl, String file) throws NoSuchAlgorithmException, MalformedURLException {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(file.getBytes());
		byte[] digest = algorithm.digest();
		
		String hex = Integer.toHexString(0xFF & digest[0]);
		if (hex.length() == 1) hex = "0" + hex;
		char x = hex.charAt(0);
		char y = hex.charAt(1);
		
		// No, a StringBuilder/StringBuffer won't make the next line more efficient...
		String path = "images/" + x + '/' + x + y + '/' + file;
		
		return new URL(baseUrl, path);
	}

	private Image obtainImage(URL url) throws IOException, InvalidImageException {
		InputStream in = null;
		try {
			URLConnection connection = url.openConnection();
			connection.setReadTimeout(500);
			in = connection.getInputStream();
			return new Image(device, in);
		} catch (SWTException e) {
			throw new InvalidImageException(e);
		} catch (NullPointerException e) {
			/*
			 * Found a case where, when the url is specified as http:/host/path, a
			 * NullPointerException results.
			 */
			throw new MalformedURLException("The image URL was not specified correctly.");
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
			}
		}
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
