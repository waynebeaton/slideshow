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

import org.eclipse.draw2d.GridLayout;

/**
 * When a {@link GridLayoutSizer} is asked to resize, it adjusts the
 * {@link GridLayout#verticalSpacing} attribute to a fraction of the
 * default value.
 */
public class GridLayoutSizer extends FigureSizer {

	private final GridLayout manager;
	private final int spacing;

	public GridLayoutSizer(GridLayout manager, int spacing, int scale) {
		this.manager = manager;
		this.spacing = spacing;
		resizeTo(scale);
	}

	public GridLayoutSizer(GridLayout manager, int spacing) {
		this(manager, spacing, 100);
	}

	@Override
	public void resizeTo(int percent) {
		manager.verticalSpacing = spacing * percent / 100;
	}
	
}