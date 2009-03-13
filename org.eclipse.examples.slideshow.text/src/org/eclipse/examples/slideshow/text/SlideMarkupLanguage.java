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

import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;

public class SlideMarkupLanguage extends MediaWikiLanguage {

	@Override
	protected void addTokenExtensions(PatternBasedSyntax syntax) {
		super.addTokenExtensions(syntax);
		syntax.add(new CopyrightToken());
		syntax.add(new InsertCodeToken());
	}
}
