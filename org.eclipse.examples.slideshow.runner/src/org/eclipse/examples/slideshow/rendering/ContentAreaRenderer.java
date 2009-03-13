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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.examples.slideshow.core.IContent;
import org.eclipse.examples.slideshow.resources.FontDescription;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * A ContentAreaRenderer generates the figures for the content area based on the
 * provided {@link IContent}[]. The work is done with the assistance of a
 * {@link RendererStrategy} that is selected based on the nature of the content.
 */
public class ContentAreaRenderer {
	
	/**
	 * The {@link #rootFigure} is the figure into which this instance
	 * will assemble the various parts.
	 */
	final Figure rootFigure;
	
	/**
	 * This is the content that the instance will render.
	 */
	final IContent[] content;
	
	
	final ResourceManager resourceManager;

	/**
	 * The {@link #defaultFontDescription} is a {@link FontDescription} that defines the
	 * the font that is used as a starting point in any {@link IFigure} that requires one.
	 * Since some resizing may occur to make things fit, the actual font used
	 * may be smaller than the one described by this value.
	 */
	private final FontDescription defaultFontDescription;
	
	/**
	 * The {@link #foregroundColor} is the colour used by any IFigure that requires
	 * a colour. Primarily, this used for setting the font colour.
	 */
	Color foregroundColor;

	public ContentAreaRenderer(Figure rootFigure, ResourceManager resourceManager, FontDescription defaultFont, IContent[] content) {
		this.rootFigure = rootFigure;
		this.resourceManager = resourceManager;
		this.defaultFontDescription = defaultFont;
		this.content = content;
		this.foregroundColor = resourceManager.getSystemColor(SWT.COLOR_BLACK);
	}
	
	public void render() {
		RendererStrategy strategy = choseRendererStrategy();
		
		strategy.layoutFigures();
	}

	/**
	 * The idea here is to make it so that we can plug-in different renderer strategies.
	 * This might be overkill.
	 * 
	 * TODO Re-evaluate the utility of having a pluggable renderer strategy.
	 * @return
	 */
	private RendererStrategy choseRendererStrategy() {
//		for (IContent item : content) {
//			if (isSingleRightAlignedImageContent(item)) return new RightAlignedImageRendererStrategy(this);
//		}
//		return new SingleColumnRendererStrategy(this);
		return new BasicRendererStrategy(this);
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;		
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}
	
	public FontDescription getDefaultFontDescription() {
		return defaultFontDescription;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}
}