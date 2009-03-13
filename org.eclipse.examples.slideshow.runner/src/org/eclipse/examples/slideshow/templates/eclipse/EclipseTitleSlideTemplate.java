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
package org.eclipse.examples.slideshow.templates.eclipse;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.rendering.ContentAreaRenderer;
import org.eclipse.examples.slideshow.resources.FontDescription;
import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;

public class EclipseTitleSlideTemplate implements ITemplate {
	EclipseTitleSlideBackground background;
	final Device device;

	public EclipseTitleSlideTemplate(Device device) {
		this.device = device;
		background = new EclipseTitleSlideBackground(device);
	}
		
	public void renderOn(ResourceManager resourceManager, IFigure root, Slide slide) {
		background.renderOn(resourceManager, root, slide);
		
		Label titleFigure = new Label(slide.getTitle());
		Color white = device.getSystemColor(SWT.COLOR_WHITE);
		titleFigure.setForegroundColor(white);
		Rectangle titleBounds = new Rectangle(25,25,1024-50, 75);
		titleFigure.setBounds(titleBounds);
		root.add(titleFigure);

		/*
		 * Make the title fit. We do this by dropping the font size
		 * down until the label fits across. There is no wrapping,
		 * the title must fit on one line.
		 * 
		 * TODO Abstract this code out. It's repeated in EcilpseInfoSlideTemplate. Yuck.
		 */
		FontDescription titleFont = getTitleFontDescription();
		int size = titleFont.getHeight();
		while (true) {
			Font font = resourceManager.getFont(titleFont.getName(), size, titleFont.getStyle());
			titleFigure.setFont(font);
			if (titleFigure.getPreferredSize().width < titleBounds.width) break;
			size -= 1;
			if (size < 8) break; // Bail out if we get too small. 
		}
		
		Figure contentFigure = new Figure();
		//contentFigure.setBorder(new LineBorder());
		contentFigure.setBounds(new Rectangle(25, 500, 1024-50, 768-550));
	
		ContentAreaRenderer contentRenderer = new ContentAreaRenderer(contentFigure, resourceManager, getDefaultContentFont(), slide.getContent());
		contentRenderer.setForegroundColor(white);
		contentRenderer.render();		
		
		root.add(contentFigure);
	}

	private FontDescription getDefaultContentFont() {
		return new FontDescription("Helvetica", 30, SWT.NORMAL);
	}

	private FontDescription getTitleFontDescription() {
		return new FontDescription("Helvetica", 48, SWT.ITALIC + SWT.BOLD);
	}

	public void dispose() {
		background.dispose();
	}

}
