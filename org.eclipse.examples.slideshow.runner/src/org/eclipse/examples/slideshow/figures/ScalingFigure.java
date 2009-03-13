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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.examples.slideshow.runner.PrinterRunner;
import org.eclipse.examples.slideshow.viewer.SlideViewer;

/**
 * This class is in the process of being extracted from {@link SlideViewer}
 * for reuse by the {@link PrinterRunner}.
 */
public class ScalingFigure extends RectangleFigure  {
		private final IFigure focalFigure;
		private final Rectangle bounds;

		public ScalingFigure(IFigure focalFigure, Rectangle bounds) {
			this.focalFigure = focalFigure;
			this.bounds = bounds;
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

			int canvasWidth = bounds.width;
			int canvasHeight = bounds.height;
			int focalFigureWidth = focalFigure.getBounds().width;
			int focalFigureHeight = focalFigure.getBounds().height;
			
			double scaleX = (double)canvasWidth / focalFigureWidth;
			double scaleY = (double)canvasHeight / focalFigureHeight;
	
			double scale = Math.min(scaleX, scaleY);
			
			g.translate((int)(canvasWidth-scale*focalFigureWidth) / 2, (int)(canvasHeight-scale*focalFigureHeight) /2);
			g.scale(scale);
			g.pushState();
			paintChildren(g);
			g.popState();
			g.dispose();
		}
	}