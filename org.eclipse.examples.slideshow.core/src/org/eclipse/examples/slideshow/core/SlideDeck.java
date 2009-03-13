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
package org.eclipse.examples.slideshow.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Instances represent a deck of slides. 
 */
public class SlideDeck {

	private List<Slide> slides = new ArrayList<Slide>();
	private String copyright = "Copyright © 2008 by the Author";
	
	/**
	 * The {@link #baseUrl} is, effectively, the location from which
	 * we obtain the resources for this slide. Instances of {@link ImageChunk},
	 * for example, specify a URL that may be relative to this location.
	 */
	final URL baseUrl;

	public SlideDeck(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void add(Slide slide) {
		slides.add(slide);
		slide.setSlideDeck(this);
	}

	public int getSlideCount() {
		return slides.size();
	}

	public Slide getSlide(int index) {
		return slides.get(index);
	}

	public void setCopyright(String copyright) {
		if (copyright == null) throw new IllegalArgumentException();
		this.copyright = copyright;
	}
	
	public String getCopyright() {
		return copyright;
	}

	public URL getBaseUrl() {
		return baseUrl;
	}

	public Slide[] getSlides() {
		return (Slide[]) slides.toArray(new Slide[slides.size()]);
	}

}
