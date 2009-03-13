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
 * A TextContent contains {@link Chunk}s of text, images, etc.
 */
public class TextContent implements IContent {

	final Chunk[] chunks;

	public TextContent(Chunk[] chunks) {
		this.chunks = chunks;
	}
	
	public String getText() {
		StringBuilder builder = new StringBuilder();
		for(Chunk chunk : chunks) {
			builder.append(chunk.getText());
		}
		return builder.toString();
	}

	public boolean hasText() {
		if (chunks == null) return false;
		for(Chunk chunk : chunks) {
			if (chunk.hasText()) return true;
		}
		return true;
	}

	public Chunk getChunk(int index) {
		return chunks[index];
	}

	public Chunk[] getChunks() {
		return chunks;
	}

	public BlockChunk getSingleBlock() {
		if (chunks.length > 1) return null;
		if (chunks[0] instanceof BlockChunk) return (BlockChunk)chunks[0];
		return null;
	} 
}
