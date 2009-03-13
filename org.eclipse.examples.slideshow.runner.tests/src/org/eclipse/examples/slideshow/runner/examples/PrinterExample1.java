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
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.resources.ResourceManager;
import org.eclipse.examples.slideshow.runner.PrinterRunner;
import org.eclipse.examples.slideshow.runner.ScreenRunner;
import org.eclipse.examples.slideshow.templates.eclipse.EclipseStandardTemplate;
import org.eclipse.examples.slideshow.text.WikiTextParser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PrinterExample1 {
	public static void main(String[] args) throws Exception {
		InputStream in = PrinterExample1.class.getResourceAsStream("one-page.show");
		SlideDeck deck = new WikiTextParser().parse(new InputStreamReader(in), ScreenExample1.class.getResource("."));
		in.close();
		
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.open();
		
		PrintDialog dialog = new PrintDialog(shell);
		PrinterData printerData = dialog.open();
				
		if (printerData != null) printTo(printerData, deck);
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		
		display.dispose();
	}

	private static void printTo(PrinterData printerData, SlideDeck deck) {
		Printer printer = new Printer(printerData);
		
		ResourceManager resourceManager = new ResourceManager(printer);
		ITemplate template = new EclipseStandardTemplate(printer);
		
		PrinterRunner runner = new PrinterRunner(printer, resourceManager, template);
		runner.print(deck);
		
		template.dispose();
		resourceManager.dispose();
		
		printer.dispose();
	}
}
