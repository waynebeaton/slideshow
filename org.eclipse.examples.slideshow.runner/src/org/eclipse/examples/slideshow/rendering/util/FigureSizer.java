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
package org.eclipse.examples.slideshow.rendering.util;

/**
 * Anything that the receiver renders that needs to be resized in an attempt
 * to make things fit is wrapped in a {@link FigureSizer} of one form or
 * another. Instances of subclasses of this abstract class will take care of
 * resizing the objects in their care when asked to do so. The {@link #resizeTo(int)}
 * method does all of the heavy lifting here.
 */
public abstract class FigureSizer {
	/**
	 * This method is used to resize the receiver to a certain percentage
	 * of its original size. 
	 * 
	 * @param percent an <code>int</code> value in the range 0-100
	 */
	public abstract void resizeTo(int percent);
}