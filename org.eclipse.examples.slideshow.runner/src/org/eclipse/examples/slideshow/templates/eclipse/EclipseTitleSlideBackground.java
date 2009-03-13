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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.resources.IBackground;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

public class EclipseTitleSlideBackground implements IBackground {

	final Device device;
	Image backgroundImage;

	public EclipseTitleSlideBackground(Device device) {
		this.device = device;
		InputStream in = EclipseTitleSlideBackground.class.getResourceAsStream("eclipse_neg_logo_fc_1024x768.jpg");
		backgroundImage = new Image(device, in);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void renderOn(ResourceManager resourceManager, IFigure root, Slide slide) {
		// TODO Make the banner dynamically size based on the size of the root figure
		Figure backgroundFigure = new ImageFigure(backgroundImage);
		backgroundFigure.setBounds(new Rectangle(0, 0, 1024, 768));
		root.add(backgroundFigure);
		
		Label copyright = new Label(slide.getCopyright());
		copyright.setLabelAlignment(PositionConstants.CENTER);
		copyright.setFont(resourceManager.getFont("Helevetica", 12, SWT.NORMAL));
		copyright.setForegroundColor(device.getSystemColor(SWT.COLOR_WHITE));
		int height = copyright.getPreferredSize().height * 3 / 2;
		copyright.setBounds(new Rectangle(0, 768-height, 1024, height));
		root.add(copyright);
	}

	public void dispose() {
		backgroundImage.dispose();
		backgroundImage = null;
	}

}
