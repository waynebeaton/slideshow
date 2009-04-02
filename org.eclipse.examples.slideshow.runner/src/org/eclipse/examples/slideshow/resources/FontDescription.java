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

// TODO Can we use JFace FontDescriptor instead?
public class FontDescription {

	private final String face;
	private final int height;
	private final int style;

	public FontDescription(String face, int height, int style) {
		this.face = face;
		this.height = height;
		this.style = style;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((face == null) ? 0 : face.hashCode());
		result = prime * result + height;
		result = prime * result + style;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FontDescription other = (FontDescription) obj;
		if (face == null) {
			if (other.face != null)
				return false;
		} else if (!face.equals(other.face))
			return false;
		if (height != other.height)
			return false;
		if (style != other.style)
			return false;
		return true;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return face;
	}

	public int getStyle() {
		return style;
	}

	public FontDescription sizedTo(int size) {
		return new FontDescription(face, size, style);
	}

	public FontDescription withStyle(int style) {
		return new FontDescription(face, height, this.style | style);
	}

	public FontDescription sizedBy(int percent) {
		return this.sizedTo(getHeight() * percent / 100);
	}
}
