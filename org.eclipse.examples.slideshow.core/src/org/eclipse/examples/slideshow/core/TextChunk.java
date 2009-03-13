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
 * A {@link Chunk} of text in a {@link TextContent}.
 * 
 * @see SpanChunk
 * @see BlockChunk
 */
public class TextChunk extends Chunk {

	private final String text;

	public TextChunk(Slide slide, String text) {
		super(slide);
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public boolean hasText() {
		return text.trim().length() > 0;
	}

}
