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
package org.eclipse.examples.slideshow.runner;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PrinterGraphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.figures.ScalingFigure;
import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.Printer;

// TODO Implements IRunner?
public class PrinterRunner {		
	private ResourceManager resourceManager;
	private ITemplate template;
	private final Printer printer;


	public PrinterRunner(Printer printer, ResourceManager resourceManager, ITemplate template) {
		this.printer = printer;
		this.resourceManager = resourceManager;
		this.template = template;
	}

	public void print(SlideDeck deck) {
		if (!printer.startJob("blah")) return; // TODO May return false
		GC gc = new GC(printer);
		PrinterGraphics graphics = new PrinterGraphics(new SWTGraphics(gc), printer);
		//graphics.scale();
		for(Slide slide : deck.getSlides()) {
			printer.startPage();
			//gc.setForeground(printer.getSystemColor(SWT.COLOR_BLACK));
			//gc.drawRectangle(new org.eclipse.swt.graphics.Rectangle(400, 400, 100, 100));
			IFigure root = new Figure();
			root.setBorder(new LineBorder());
			root.setBounds(new Rectangle(0,0,1024,768));
			template.renderOn(resourceManager, root, slide);
			root.paint(graphics);
			//new ScalingFigure(root, new Rectangle(printer.getClientArea())).paint(graphics);
			printer.endPage();
		}
		gc.dispose();
		printer.endJob();
	}
}
