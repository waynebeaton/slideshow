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
package org.eclipse.examples.slideshow.rendering.util;

import org.eclipse.draw2d.IFigure;
import org.eclipse.examples.slideshow.resources.FontDescription;
import org.eclipse.examples.slideshow.resources.ResourceManager;

/**
 * Instances of {@link TextFigureSizer} are responsible for changing the
 * size of the font for any figure that supports fonts. The constructor takes
 * an {@link IFigure} and a {@link FontDescription} as parameters. The font
 * description describes the the base-line font and&mdash;when the instance is
 * asked to resize&mdash;it sets the font for the figure to a font derived
 * by resizing the the base-line font to a percentage of it's original value.
 */
public class TextFigureSizer extends FigureSizer {

	private final IFigure figure;
	private final FontDescription fontDescription;
	private final ResourceManager resourceManager;

	public TextFigureSizer(ResourceManager resourceManager, IFigure figure, FontDescription fontDescription, int scale) {
		this.resourceManager = resourceManager;
		this.figure = figure;
		this.fontDescription = fontDescription;
		resizeTo(scale);
	}

	@Override
	public void resizeTo(int percent) {
		figure.setFont(resourceManager.getFont(fontDescription.sizedTo(fontDescription.getHeight() * percent / 100)));
	}
	
}