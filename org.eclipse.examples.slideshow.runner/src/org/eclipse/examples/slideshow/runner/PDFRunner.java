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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.swt.widgets.Display;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

public class PDFRunner {		
	private ResourceManager resourceManager;
	private ITemplate template;
	private final Display display;


	public PDFRunner(Display display, ResourceManager resourceManager, ITemplate template) {
		this.display = display;
		this.resourceManager = resourceManager;
		this.template = template;
	}

	public void print(SlideDeck deck, String fileName) throws FileNotFoundException {
		print(deck, new FileOutputStream(fileName));
	}
	
	public void print(SlideDeck deck, File file) throws FileNotFoundException {
		print(deck, new FileOutputStream(file));
	}
	
	public void print(SlideDeck deck, OutputStream out) {
		try {
			// FIXME Again with the hardcoding...
			Document document = new Document(new com.lowagie.text.Rectangle(0, 0, 1024, 768));
			PdfWriter writer = PdfWriter.getInstance(document, out);
			
			document.open();
			
			PDFGraphics graphics = new PDFGraphics(document, writer);
					
			for(Slide slide : deck.getSlides()) {
				IFigure root = new Figure();
				root.setBorder(new LineBorder());
				root.setBounds(new Rectangle(0,0,1024,768));
				template.renderOn(resourceManager, root, slide);
				root.validate();
				ScaledGraphics scaledGraphics = new ScaledGraphics(graphics);
				scaledGraphics.scale(1.0);
				root.paint(scaledGraphics);
				document.newPage();
			}
			document.close();
		} catch (DocumentException e) {
			// FIXME Don't want to expose this exception outside of this class
			e.printStackTrace();
		}
	}
}