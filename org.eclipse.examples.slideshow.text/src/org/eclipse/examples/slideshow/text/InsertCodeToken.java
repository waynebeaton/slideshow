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

import org.eclipse.mylyn.wikitext.core.parser.markup.PatternBasedElement;
import org.eclipse.mylyn.wikitext.core.parser.markup.PatternBasedElementProcessor;

public class InsertCodeToken extends PatternBasedElement {

	// TODO Add ability to turn on/off line numbers
	// TODO Add flag to include Javadoc
	// TODO Add flag to suppress all comments
	// TODO Add flag to reformat
	// TODO Add flag to preserve whitespace
	@Override
	protected String getPattern(int groupOffset) {
		return "(?:\\{Code:([^\\}]*)\\})";
	}

	@Override
	protected int getPatternGroupCount() {
		return 1;
	}

	@Override
	protected PatternBasedElementProcessor newProcessor() {
		return new PatternBasedElementProcessor() {
			@Override
			public void emit() {
				String url = group(1);
				if (builder instanceof SlideDeckDocumentBuilder) {
					((SlideDeckDocumentBuilder)builder).codeBlock(url);
				}
			}			
		};
	}

}
