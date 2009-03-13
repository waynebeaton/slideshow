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
package org.eclipse.examples.slideshow.rendering.util;

import org.eclipse.examples.slideshow.figures.ListFigure;

public class ListFigureSizer extends FigureSizer {

	private final ListFigure figure;

	public ListFigureSizer(ListFigure figure, int scale) {
		this.figure = figure;
		resizeTo(scale);
	}

	@Override
	public void resizeTo(int percent) {
		figure.setScale(percent);
	}

}
