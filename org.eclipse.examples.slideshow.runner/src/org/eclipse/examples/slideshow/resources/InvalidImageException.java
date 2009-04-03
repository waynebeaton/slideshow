package org.eclipse.examples.slideshow.resources;

import org.eclipse.swt.SWTException;

/**
 * The InvalidImageException has been created to make explicit the
 * handling of exceptions due to problems in obtaining an image.
 * 
 * @see ResourceManager#getImage(java.net.URL)
 */
public class InvalidImageException extends Exception {
	private static final long serialVersionUID = 517781745972212707L;

	public InvalidImageException(SWTException cause) {
		super(cause.getMessage(), cause);
	}

}
