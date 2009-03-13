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
package org.eclipse.examples.slideshow.text;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;

public class WikiTextParser {

	ListenerList listeners = new ListenerList();

	public SlideDeck parse(String string) {
		return parse(string, null);
	}
	
	public SlideDeck parse(String string, URL baseUrl) {
		try {
			return parse(new StringReader(string), baseUrl);
		} catch (IOException e) {
			return null; // Should never get an IOException off a StringReader, right?
		}
	}

	public SlideDeck parse(Reader reader) throws IOException {
		return parse(reader, null);
	}
	
	public SlideDeck parse(Reader reader, URL baseUrl) throws IOException {
		MarkupParser parser = new MarkupParser(new SlideMarkupLanguage());
		SlideDeckDocumentBuilder builder = new SlideDeckDocumentBuilder(baseUrl);
		builder.addSlideDeckDocumentListener(new ISlideDeckDocumentListener() {
			public void slideAdded(SlideAddedEvent event) {
				fireSlideAddedEvent(event);
			}
		});
		parser.setBuilder(builder);
		parser.parse(reader);
		return builder.getSlideDeck();
	}
	
	public void addSlideParserListener(ISlideParserListener listener) {
		listeners.add(listener);
	}

	void fireSlideAddedEvent(SlideAddedEvent event) {
		if (listeners == null) return;
		if (listeners.isEmpty()) return;
		
		for (Object listener : listeners.getListeners()) {
			((ISlideParserListener)listener).slideAdded(event);
		}
	}

}
