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
package org.eclipse.examples.slideshow.text;

import org.eclipse.examples.slideshow.core.Slide;

public class SlideAddedEvent {

	public final Slide slide;
	public final int lineNumber;

	public SlideAddedEvent(Slide slide, int lineNumber) {
		this.slide = slide;
		this.lineNumber = lineNumber;
	}

}
