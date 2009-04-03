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
package org.eclipse.examples.slideshow.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.examples.slideshow.resources.FontDescription;
import org.eclipse.examples.slideshow.resources.ResourceManager;

public class MessageFigure extends Label implements IResizeableFigure {
	private final ResourceManager resourceManager;
	private final FontDescription fontDescription;

	public MessageFigure(ResourceManager resourceManager, FontDescription fontDescription, String message) {
		this(resourceManager, fontDescription, message, 100);
	}
	
	public MessageFigure(ResourceManager resourceManager, FontDescription fontDescription, String message, int scale) {
		this.resourceManager = resourceManager;
		this.fontDescription = fontDescription;
		this.setText(message);
		setScale(scale);
	}

	public void setScale(int percent) {
		setFont(resourceManager.getFont(fontDescription.sizedBy(percent)));
	}

}
