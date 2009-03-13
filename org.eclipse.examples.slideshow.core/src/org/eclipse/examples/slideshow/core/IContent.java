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
package org.eclipse.examples.slideshow.core;

/**
 * Slide content is composed of an array of {@link IContent}s. Implementors
 * provide such things as lists ({@link ListContent}), and paragraphs (
 * {@link TextContent}).
 */
public interface IContent {

	/**
	 * This method returns the single {@link BlockChunk} represented by the
	 * receiver if the receiver does indeed contain exactly one block chunk and
	 * nothing else. This method is typically used by renderers to determine if
	 * the content can be treated special. An instance that represents a single
	 * image, for example, might result in that image being centered on the
	 * slide image.
	 * 
	 * @see TextContent#getSingleBlock()
	 * 
	 * @return an instance of {@link BlockChunk} or <code>null</code>.
	 */
	BlockChunk getSingleBlock();

}
