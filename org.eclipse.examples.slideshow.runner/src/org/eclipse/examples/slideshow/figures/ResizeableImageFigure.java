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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

public class ResizeableImageFigure extends Figure implements IResizeableFigure {
	private final Image image;
	private final int width;
	private final int height;
	private final Rectangle imageBounds;
	private int scale;

	public ResizeableImageFigure(Image image) {
		this(image, -1, -1);
	}

	public ResizeableImageFigure(Image image, int width, int height) {
		this.image = image;
		this.width = width == -1 ? image.getBounds().width : width;
		this.height = height == -1 ? image.getBounds().height : height;
		this.scale = 100;
		this.imageBounds = new Rectangle(0,0,image.getBounds().width, image.getBounds().height);
	}
	
	@Override
	public Dimension getPreferredSize(int widthHint, int heightHint) {
		return new Dimension(width*scale/100, height*scale/100);
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.drawImage(image, imageBounds, new Rectangle(getClientArea().x, getClientArea().y, width*scale/100, height*scale/100));
	}

	public void setScale(int percent) {
		scale = percent;
	}
}