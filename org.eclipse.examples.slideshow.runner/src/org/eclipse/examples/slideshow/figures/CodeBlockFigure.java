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
package org.eclipse.examples.slideshow.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.examples.slideshow.resources.FontDescription;
import org.eclipse.examples.slideshow.resources.ResourceManager;

/**
 * TODO Sort out a better way to make the FlowPage stop wrapping.
 */
public class CodeBlockFigure extends Figure implements IResizeableFigure {
	private static final int MARGIN = 8;
	private final String source;
	private FlowPage page;
	private FontDescription fontDescription;
	private final ResourceManager resourceManager;
	private int scale;

	public CodeBlockFigure(ResourceManager resourceManager, FontDescription fontDescription, String source) {
		this.resourceManager = resourceManager;
		this.fontDescription = fontDescription;
		this.source = source;
		scale = 100;
		createLayout();
	}

	private void createLayout() {
		this.removeAll();
		
		page = new FlowPage();
		BlockFlow blockFlow = new BlockFlow();
		blockFlow.setHorizontalAligment(PositionConstants.LEFT);
		String text = source.replaceAll("\t", "    ");
		TextFlow textFlow = new TextFlow(text);
		blockFlow.add(textFlow);
		
		page.add(blockFlow);
		
		page.setFont(resourceManager.getFont(fontDescription.sizedTo(fontDescription.getHeight() * scale / 100)));
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = MARGIN * scale / 100;
		layout.marginHeight = MARGIN * scale / 100;
		this.setLayoutManager(layout);
		this.add(page);
		this.setConstraint(page, new GridData(GridData.FILL, GridData.FILL, true, true));
	}
	
	/**
	 * We override this method to prevent the FlowPage from wrapping. When asked
	 * for our preferred size with any hint, we answer the unhinted preferred
	 * size (which should give us the value of the FlowPage without wrapping).
	 * The size is expanded by five pixels to make sure that wrapping doesn't
	 * occur. Yes, this has hack written all over it.
	 */
	public Dimension getPreferredSize(int widthHint, int heightHint) {
		Dimension size = super.getPreferredSize(-1, -1);
		return size.expand(5, 5); //page.getPreferredSize(widthHint, heightHint);
	}
	
	public void setScale(int percent) {
		scale = percent;
		createLayout();
	}
}
