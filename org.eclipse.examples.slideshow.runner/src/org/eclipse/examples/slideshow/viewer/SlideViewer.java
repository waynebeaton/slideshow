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
package org.eclipse.examples.slideshow.viewer;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class SlideViewer {

	FigureCanvas canvas;
	Slide slide;
	ITemplate template;
	ResourceManager resourceManager;
	int inset;
	
	public SlideViewer(Composite parent, int none) {
		canvas = new FigureCanvas(parent, SWT.NO_BACKGROUND);
		canvas.setScrollBarVisibility(FigureCanvas.NEVER);
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
		update();
	}
	
	public void setSlide(Slide slide) {
		this.slide = slide;
		update();
	}

	public void setTemplate(ITemplate template) {
		this.template = template;
		update();
	}
	
	public void setInset(int inset) {
		this.inset = inset;
	}
	
	class ScalingFigure extends RectangleFigure  {
		private final IFigure focalFigure;

		public ScalingFigure(IFigure focalFigure) {
			this.focalFigure = focalFigure;
			add(focalFigure);
		}

		@Override
		protected void paintClientArea(Graphics graphics) {
			ScaledGraphics g = new ScaledGraphics(graphics) {
				/**
				 * The scaling algorithms seem to be sizing fonts a little
				 * too large in some cases. As a result, we're getting some
				 * clipping at some scales. Since we're not too concerned
				 * about clipping, we work around this by expanding the clip
				 * rectangle to the full slide space.
				 */
				@Override
				public void clipRect(Rectangle r) {
					super.clipRect(new Rectangle(0,0,1024,768));
				}
			};

			int canvasWidth = canvas.getBounds().width;
			int canvasHeight = canvas.getBounds().height;
			int focalFigureWidth = focalFigure.getBounds().width;
			int focalFigureHeight = focalFigure.getBounds().height;
			
			double scaleX = ((double)canvasWidth - inset * 2) / focalFigureWidth;
			double scaleY = ((double)canvasHeight - inset * 2) / focalFigureHeight;
	
			double scale = Math.min(scaleX, scaleY);
			
			g.translate((int)(canvasWidth-scale*focalFigureWidth) / 2, (int)(canvasHeight-scale*focalFigureHeight) /2);
			g.scale(scale);
			g.pushState();
			paintChildren(g);
			g.popState();
			g.dispose();
		}
	}
	
	void update() {
		IFigure root = new ScalingFigure(renderSlide());
		canvas.setContents(root);
	}

	public Composite getControl() {
		return canvas;
	}
	
	IFigure renderSlide() {
		final Rectangle slideBounds = new Rectangle(0,0,1024,768);
		Figure slideFigure = new RectangleFigure();
		slideFigure.setBounds(slideBounds);
		slideFigure.setBackgroundColor(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		slideFigure.setBorder(new LineBorder());
		
		if (resourceManager == null) return slideFigure;
		if (slide == null) return slideFigure;
		if (template == null) return slideFigure;
		
		template.renderOn(resourceManager, slideFigure, slide);
		
		return slideFigure;
	}

	private Display getDisplay() {
		return canvas.getDisplay();
	}

	public void refresh() {
		update();
	}
}
