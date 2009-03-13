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
 * Instances represent a list containing one or more {@link ListItemContent}
 * instances. A ListContent specifies a type: either {@link ListType#BULLETED},
 * or {@link ListType#NUMERIC}. This information provides a hint to the render
 * so that it can decide how to draw the list on a slide.
 */
public class ListContent implements IContent {
	public enum ListType {BULLETED, NUMERIC};
	
	IContent[] items;
	private final ListType type;

	public ListContent(IContent[] items, ListType type) {
		this.items = items;
		this.type = type;
	}

	public void setItems(IContent[] items) {
		this.items = items;
	}
	
	public IContent[] getItems() {
		return items;
	}

	public BlockChunk getSingleBlock() {
		return null;
	}

	public ListType getType() {
		return type;
	}

}
