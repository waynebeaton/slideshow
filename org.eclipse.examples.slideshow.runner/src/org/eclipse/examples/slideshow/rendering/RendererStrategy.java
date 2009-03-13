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
package org.eclipse.examples.slideshow.rendering;

import org.eclipse.draw2d.IFigure;
import org.eclipse.examples.slideshow.core.IContent;
import org.eclipse.examples.slideshow.resources.ResourceManager;

public abstract class RendererStrategy {

	final ContentAreaRenderer contentAreaRenderer;
	
	public RendererStrategy(ContentAreaRenderer contentAreaRenderer) {
		this.contentAreaRenderer = contentAreaRenderer;
	}
	
	protected abstract void layoutFigures();

	protected IFigure getRootFigure() {
		return contentAreaRenderer.rootFigure;
	}

	protected ResourceManager getResourceManager() {
		return contentAreaRenderer.getResourceManager();
	}
	
	protected IContent[] getContent() {
		return contentAreaRenderer.content;
	}
}
