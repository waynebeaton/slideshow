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
 * Instances of this class represent an item in a {@link ListContent}.
 * When rendered, instances appear as bullets. Instances are containers
 * for {@link Chunk}s that can represent text, images, code, etc. and
 * nested content which can be other types of {@link IContent} including
 * {@link TextContent} and nested {@link ListContent} instances.
 */
public class ListItemContent extends TextContent {

	IContent[] nestedContent;

	public ListItemContent(Chunk[] chunks, IContent[] nestedContent) {
		super(chunks);
		this.nestedContent = nestedContent;
	}
	
	/**
	 * This method returns the nested content. This will be an array containing
	 * zero or more {@link IContent} instances. It is possible that a list might
	 * contain one or more nested lists (perhaps one with bullets, one with
	 * numbers, or some combination).
	 * 
	 * @return An array containing nested {@link IContent} items.
	 */
	public IContent[] getNestedContent() {
		return nestedContent;
	}
}
