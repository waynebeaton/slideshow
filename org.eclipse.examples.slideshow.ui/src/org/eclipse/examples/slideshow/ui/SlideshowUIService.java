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
package org.eclipse.examples.slideshow.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.text.ISlideParserListener;
import org.eclipse.examples.slideshow.text.SlideAddedEvent;
import org.eclipse.examples.slideshow.text.WikiTextParser;
import org.eclipse.examples.slideshow.ui.editors.ISlideshowSourceProvider;
import org.eclipse.examples.slideshow.ui.model.ISlideshowUIService;
import org.eclipse.examples.slideshow.ui.views.ISlideshowUIHandler;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class SlideshowUIService implements ISlideshowUIService {

	SlideDeck slideDeck;
	Slide slide;

	ServiceTracker handlerTracker;
	
	ITextListener textListener = new ITextListener() {
		public void textChanged(TextEvent event) {
			buildSlideDeckModel();
		}		
	};
	
	ISelectionChangedListener textSelectionListener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent event) {
			findSlideUnderCursor();
		}		
	};
	
	ISlideshowSourceProvider provider;
	List<LineNumberAndSlidePair> lineNumberToSlideMap = new ArrayList<LineNumberAndSlidePair>();
	
	public SlideshowUIService() {
		handlerTracker = new ServiceTracker(getBundleContext(), ISlideshowUIHandler.class.getName(), null) {
			@Override
			public Object addingService(ServiceReference reference) {
				ISlideshowUIHandler handler = (ISlideshowUIHandler) super.addingService(reference);
				handler.slideDeckChanged(slideDeck);
				return handler;
			}
		};
		handlerTracker.open();
	}
	

	public void setProvider(ISlideshowSourceProvider provider) {
		unhookListeners(provider);
		this.provider = provider;
		hookListeners(provider);
		buildSlideDeckModel();
		findSlideUnderCursor();
	}
	
	void hookListeners(ISlideshowSourceProvider provider) {
		if (provider == null) return;
		provider.addTextListener(textListener);
		provider.addPostSelectionChangedListener(textSelectionListener);
	}

	void unhookListeners(ISlideshowSourceProvider provider) {
		if (provider == null) return;
		provider.removeTextListener(textListener);
		provider.removePostSelectionChangedListener(textSelectionListener);
	}

	public void dispose() {
		unhookListeners(provider);
		handlerTracker.close();
	}
	
	BundleContext getBundleContext() {
		return Activator.getDefault().getContext();
	}

	/**
	 * This method sets the current {@link SlideDeck}. This notifies
	 * any &quot;handler&quot; services (instances of {@link ISlideshowUIHandler})
	 * that the slide deck is now different.
	 * <p>
	 * This method has default visibility and is not part of the public API.
	 * 
	 * @see ISlideshowUIHandler#slideDeckChanged(SlideDeck)
	 * @param deck
	 */
	void setSlideDeck(SlideDeck deck) {
		this.slideDeck = deck;
		
		Object[] services = handlerTracker.getServices();
		if (services == null) return;
		
		for(Object handler : services) {
			((ISlideshowUIHandler)handler).slideDeckChanged(deck);
		}
	}

	/**
	 * This method sets the current {@link Slide}. Assuming that the
	 * slide has changed, all &quot;handler&quot; services (instances of
	 * {@link ISlideshowUIHandler}) are notified that the slide is
	 * now different
	 * <p>
	 * This method has default visibility and is not part of the public API.
	 * @see ISlideshowUIHandler#slideChanged(Slide)
	 * @param slide
	 */
	void setSlide(Slide slide) {
		if (this.slide == slide) return;
		
		this.slide = slide;

		Object[] services = handlerTracker.getServices();
		if (services == null) return;
		
		for(Object handler : services) {
			((ISlideshowUIHandler)handler).slideChanged(slide);
		}
	}

	class LineNumberAndSlidePair {
		final int lineNumber;
		final Slide slide;

		public LineNumberAndSlidePair(int lineNumber, Slide slide) {
			this.lineNumber = lineNumber;
			this.slide = slide;
		}
	}
		
	void buildSlideDeckModel() {
		lineNumberToSlideMap.clear();
		SlideDeck deck = parseSlideDeck(new ISlideParserListener() {
			public void slideAdded(SlideAddedEvent event) {
				lineNumberToSlideMap.add(new LineNumberAndSlidePair(event.lineNumber, event.slide));
			}				
		});
		setSlideDeck(deck);
	}


	private SlideDeck parseSlideDeck(ISlideParserListener listener) {
		if (provider == null) return null;
		
		WikiTextParser parser = new WikiTextParser();
		parser.addSlideParserListener(listener);
		SlideDeck deck = parser.parse(provider.getSource(), provider.getBaseUrl());
		return deck;
	}
	
	void findSlideUnderCursor() {
		if (provider == null) {
			setSlide(null);
			return;
		}
		int line = provider.getSelection().getStartLine();
		Slide slide = null;
		for (LineNumberAndSlidePair pair : lineNumberToSlideMap) {
			if (line < pair.lineNumber) {
				setSlide(slide);
				return;
			}
			slide = pair.slide;
		}
		setSlide(slide);
	}
}
