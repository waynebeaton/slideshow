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

import org.eclipse.mylyn.wikitext.core.parser.ImageAttributes;
import org.eclipse.mylyn.wikitext.core.parser.ImageAttributes.Align;
import org.eclipse.mylyn.wikitext.core.parser.markup.PatternBasedElement;
import org.eclipse.mylyn.wikitext.core.parser.markup.PatternBasedElementProcessor;

public class CopyrightToken extends PatternBasedElement {
	@Override
	protected String getPattern(int groupOffset) {
		return "(?:\\{Copyright:([^\\}]*)\\})";
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
				String message = group(1);
				if (builder instanceof SlideDeckDocumentBuilder) {
					((SlideDeckDocumentBuilder)builder).copyright(message);
				}
			}			
		};
	}

	private static class ImageReplacementTokenProcessor extends PatternBasedElementProcessor {
		@Override
		public void emit() {
			String imageUrl = group(1);
			String optionsString = group(2);

			ImageAttributes attributes = new ImageAttributes();
			if (optionsString != null) {
				String[] options = optionsString.split("\\s*\\|\\s*");
				for (String option : options) {
					if ("center".equals(option)) {
						attributes.setAlign(Align.Middle);
					} else if ("left".equals(option)) {
						attributes.setAlign(Align.Left);
					} else if ("right".equals(option)) {
						attributes.setAlign(Align.Right);
					} else if ("none".equals(option)) {
						attributes.setAlign(null);
					} else if ("thumb".equals(option) || "thumbnail".equals(option)) {
						// ignore
					} else if (option.matches("\\d+px")) {
						try {
							int size = Integer.parseInt(option.substring(0, option.length() - 2));
							attributes.setWidth(size);
							attributes.setHeight(size);
						} catch (NumberFormatException e) {
							// ignore
						}
					} else if ("frameless".equals(option)) {
						attributes.setBorder(0);
					} else if ("frame".equals(option)) {
						attributes.setBorder(1);
					} else {
						attributes.setTitle(option);
					}
				}
			}
			builder.image(attributes, imageUrl);
		}
	}
}
