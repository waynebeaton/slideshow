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
package org.eclipse.examples.slideshow.runner.examples;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.examples.slideshow.runner.ScreenRunner;
import org.eclipse.examples.slideshow.templates.eclipse.EclipseStandardTemplate;
import org.eclipse.examples.slideshow.text.WikiTextParser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class ScreenExample1 {
	public static void main(String[] args) throws Exception {
		InputStream in = ScreenExample1.class.getResourceAsStream("soc2008.txt");
		SlideDeck deck = new WikiTextParser().parse(new InputStreamReader(in), ScreenExample1.class.getResource("."));
		in.close();
		
		Display display = Display.getDefault();
		
		ResourceManager resourceManager = new ResourceManager(display);
		ITemplate template = new EclipseStandardTemplate(display);
	
		ScreenRunner runner = new ScreenRunner(display, SWT.NONE);
		runner.setResourceManager(resourceManager);
		runner.setSlideshow(deck);
		runner.setTemplate(template);
		runner.open();
		
		while (runner.isRunning()) {
			display.readAndDispatch();
		}
		
		display.dispose();
		template.dispose();
		resourceManager.dispose();
	}
}
