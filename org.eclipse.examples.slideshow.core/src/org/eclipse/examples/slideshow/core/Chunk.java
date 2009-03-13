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
 * Content is divided into &quot;chunks&quot;. A single chunk might be
 * a span of text, and image, or something else. There is a notion of
 * composite chunks (chunks within chunks).
 * 
 * @see TextChunk
 * @see SpanChunk
 * @see ImageChunk
 * 
 * @author wayne
 *
 */
public abstract class Chunk {

	private final Slide slide;

	public Chunk(Slide slide) {
		this.slide = slide;
	}

	public Slide getSlide() {
		return slide;
	}
	
	public SlideDeck getSlideDeck() {
		return slide.getSlideDeck();
	}
	
	public abstract String getText();

	public abstract boolean hasText();

}
