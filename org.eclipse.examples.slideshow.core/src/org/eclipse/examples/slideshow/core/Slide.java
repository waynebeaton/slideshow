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

/**
 * A Slide. A slide has a {@link #title} and {@link #contents}. The
 * {@link #level} of a slide is used as a hint by the visual rendering mechanism
 * to determine how to draw the slide. A level of <code>1</code> hints that the
 * slide is a cover (or title) slide. Renderers can decide how to interpret
 * different levels. When a slide is specified using wiki text, the
 * &quot;h1&quot; style is interpreted as level &quot;1&quot;, &quot;h2&quot;
 * style is interpreted as level &quot;2&quot;, etc.
 * <p>
 * A slide can optionally have a copyright (you can have a different copyright
 * on different slides).
 * 
 * @see IContent
 */
public class Slide {

	String title;
	IContent[] contents;
	private SlideDeck slideDeck;
	private String copyright;
	private int level;

	public void setTitle(String title) {
		this.title = title;		
	}

	public void setContents(IContent[] contents) {
		this.contents = contents;
	}

	public String getTitle() {
		return title;
	}

	public IContent[] getContent() {
		return contents;
	}
	
	public void setCopyright(String copyright) {
		this.copyright = copyright;		
	}

	/**
	 * This method returns the copyright message for this slide. If the slide
	 * does not have its own copyright message, it assumes that of the parent
	 * slide deck. Since the slide deck has a default value that cannot be set
	 * to <code>null</code>, this method should always return a proper value.
	 * 
	 * @return A copyright {@link String}
	 */
	public String getCopyright() {
		if (copyright != null) return copyright;
		return getSlideDeck().getCopyright();
	}

	public void setSlideDeck(SlideDeck slideDeck) {
		this.slideDeck = slideDeck;		
	}
	
	public SlideDeck getSlideDeck() {
		return slideDeck;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}

}
