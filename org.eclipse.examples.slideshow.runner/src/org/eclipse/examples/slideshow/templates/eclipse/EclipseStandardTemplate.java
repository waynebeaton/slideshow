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

import org.eclipse.draw2d.IFigure;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.swt.graphics.Device;

public class EclipseStandardTemplate implements ITemplate {
	ITemplate titleSlide;
	ITemplate infoSlide;

	public EclipseStandardTemplate(Device device) {
		titleSlide = new EclipseTitleSlideTemplate(device);
		infoSlide = new EclipseInfoSlideTemplate(device);
	}
	
	public void renderOn(ResourceManager resourceManager, IFigure root, Slide slide) {
		if (slide.getLevel() == 1) titleSlide.renderOn(resourceManager, root, slide);
		else infoSlide.renderOn(resourceManager, root, slide);
	}

	public void dispose() {
		titleSlide.dispose();
		infoSlide.dispose();
	}

}
