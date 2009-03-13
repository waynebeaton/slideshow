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
 * A SpanChunk represents a span of chunks with style (italics, bold). May
 * contain {@link TextChunk}s and {@link ImageChunk}s for example.
 * 
 * @see TextContent
 * @see TextChunk
 * @see BlockChunk
 */
public class SpanChunk extends Chunk {

	public static final int STYLE_NONE = 0;
	public static final int STYLE_ITALICS = 1;
	public static final int STYLE_BOLD = 2;
	
	final Chunk[] chunks;
	final int style;
	

	public SpanChunk(Slide slide, Chunk[] chunks, int style) {
		super(slide);
		this.chunks = chunks;
		this.style = style;
	}

	@Override
	public String getText() {
		StringBuilder builder = new StringBuilder();
		for (Chunk chunk : chunks) {
			builder.append(chunk.getText());
		}
		return builder.toString();
	}

	@Override
	public boolean hasText() {
		for (Chunk chunk : chunks) {
			if (chunk.hasText()) return true;
		}
		return false;
	}

	public Chunk getChunk(int index) {
		return chunks[index];
	}

	public int getStyle() {
		return style;
	}

	public Chunk[] getChunks() {
		return chunks;
	}

}
