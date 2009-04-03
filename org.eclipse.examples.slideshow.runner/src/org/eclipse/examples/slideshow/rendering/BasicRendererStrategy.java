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
package org.eclipse.examples.slideshow.rendering;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.examples.slideshow.core.BlockChunk;
import org.eclipse.examples.slideshow.core.Chunk;
import org.eclipse.examples.slideshow.core.CodeBlockChunk;
import org.eclipse.examples.slideshow.core.IContent;
import org.eclipse.examples.slideshow.core.ImageChunk;
import org.eclipse.examples.slideshow.figures.CodeBlockFigure;
import org.eclipse.examples.slideshow.figures.IResizeableFigure;
import org.eclipse.examples.slideshow.figures.ListFigure;
import org.eclipse.examples.slideshow.figures.MessageFigure;
import org.eclipse.examples.slideshow.figures.ResizeableImageFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

/**
 * This renderer strategy is pretty much a brute-force approach.
 * At some point, this should be made extensible. That is, this
 * renderer should be broken up into multiple renderers that are
 * accessible either as extensions or services with some mechanism
 * for selection the "right" one based on the content of the slide.
 */
public class BasicRendererStrategy extends RendererStrategy {
	private List<BlockChunk> positionableChunks;
	private List<IContent> otherContent;

	public BasicRendererStrategy(ContentAreaRenderer contentAreaRenderer) {
		super(contentAreaRenderer);
	}
	
	@Override
	protected void layoutFigures() {
		positionableChunks = new ArrayList<BlockChunk>();
		otherContent = new ArrayList<IContent>();
		
		for (IContent item : getContent()) {
			BlockChunk block = item.getSingleBlock();
			if (block != null) {
				positionableChunks.add(block);
			} else {
				otherContent.add(item);
			}
		}

		if (!positionableChunks.isEmpty()) layoutWithPositionableContent();
		else layoutWithoutPositionableContent();
	}
	
	protected void layoutWithoutPositionableContent() {
		getRootFigure().setLayoutManager(new StackLayout());
		
		final ListFigure textColumn = new ListFigure(contentAreaRenderer.getResourceManager());
		textColumn.setContent((IContent[]) otherContent.toArray(new IContent[otherContent.size()]));
		textColumn.setFontDescription(contentAreaRenderer.getDefaultFontDescription());
		textColumn.setForegroundColor(contentAreaRenderer.getForegroundColor());
		textColumn.setScale(100);
				
		getRootFigure().add(textColumn);
		getRootFigure().addLayoutListener(new LayoutListener.Stub() {
			public void postLayout(IFigure container) {
				makeTextColumnFit(textColumn, getRootFigure());
			}
		});
	}

	protected void makeTextColumnFit(ListFigure textColumn, IFigure parent) {
		if (textColumn.getScale() <= 25) 
			return;
		Rectangle parentBounds = parent.getBounds();
		int scale = 100;
		while (true) {
			Dimension size = textColumn.getPreferredSize(parentBounds.width, -1);
			if (size.height < parentBounds.height) break;
			scale -= 5;
			if (scale < 25) break; // Can't keep squeezing forever.
			textColumn.setScale(scale);
		}
	}

	protected void layoutWithPositionableContent() {
		if (otherContent.isEmpty()) {
			layoutWithOnlyPositionableContent();
			return;
		}
		
		getRootFigure().setLayoutManager(new BorderLayout());
		
		IFigure topFigure = new Figure();
		IFigure rightFigure = new Figure();
		
		List<IFigure> otherFigures = new ArrayList<IFigure>();
		List<IResizeableFigure> allFigures = new ArrayList<IResizeableFigure>();
		
		for (BlockChunk chunk : positionableChunks) {
			IResizeableFigure figure = createFigure(chunk);
			allFigures.add(figure);
			if (isTall(figure)) {
				addFigure(rightFigure, figure);
			} else if (isWide(figure)) {
				addFigure(topFigure, figure);
			} else {
				otherFigures.add(figure);
			}
		}	
		
		if (!otherFigures.isEmpty()) {
			if (rightFigure.getChildren().isEmpty()) {
				if (topFigure.getChildren().isEmpty()) {
					addFigures(rightFigure, otherFigures);
				} else {
					addFigures(topFigure, otherFigures);
				}
			} else if (topFigure.getChildren().isEmpty()) addFigures(rightFigure, otherFigures);
			else 
				addFigures(topFigure, otherFigures);
		}
		
		int size = 100;
		
		if (topFigure.getChildren().size() > 0) {
			GridLayout layout = new GridLayout(topFigure.getChildren().size(), false);
			//layout.horizontalSpacing = 25;
			topFigure.setLayoutManager(layout);
			getRootFigure().add(topFigure);
			getRootFigure().setConstraint(topFigure, BorderLayout.TOP);
			
			int width = 0;
			int height = 0;
			for(Object object : topFigure.getChildren()) {
				IResizeableFigure figure = (IResizeableFigure) object;
				layout.setConstraint(figure, new GridData(GridData.CENTER, GridData.CENTER, true, true));
				Dimension dimension = figure.getPreferredSize();
				width += dimension.width;
				height = Math.max(height, dimension.height);
			}
			
			int heightScale = (getRootFigure().getBounds().height * 100 / 2) / height;
			int widthScale = (getRootFigure().getBounds().width * 100) / width;
			
			size = Math.min(size, heightScale);
			size = Math.min(size, widthScale);
		}

		if (rightFigure.getChildren().size() > 0) {
			GridLayout layout = new GridLayout(1, false);
			//layout.verticalSpacing = 25;
			rightFigure.setLayoutManager(layout);
			getRootFigure().add(rightFigure);
			getRootFigure().setConstraint(rightFigure, BorderLayout.RIGHT);

			int width = 0;
			int height = topFigure.getPreferredSize().height; // + 25 * (rightFigure.getChildren().size() - 1);
			for(Object object : rightFigure.getChildren()) {
				IResizeableFigure figure = (IResizeableFigure) object;
				
				layout.setConstraint(figure, new GridData(GridData.CENTER, GridData.BEGINNING, true, true));
				
				Dimension dimension = figure.getPreferredSize();
				width = Math.max(width, dimension.width);
				height += dimension.height;
			}
			
			int widthScale = (getRootFigure().getBounds().width * 100 / 2) / width;
			int heightScale = (getRootFigure().getBounds().height * 100) / height;
			
			size = Math.min(size, heightScale);
			size = Math.min(size, widthScale);
		}
		
		for (IResizeableFigure figure : allFigures) {
			figure.setScale(size);
		}
		
		final IFigure textArea = new Figure();
		getRootFigure().add(textArea);
		getRootFigure().setConstraint(textArea, BorderLayout.CENTER);
		
		textArea.setLayoutManager(new StackLayout());
		
		final ListFigure textColumn = new ListFigure(contentAreaRenderer.getResourceManager());
		textColumn.setContent((IContent[]) otherContent.toArray(new IContent[otherContent.size()]));
		textColumn.setFontDescription(contentAreaRenderer.getDefaultFontDescription());
		textColumn.setForegroundColor(contentAreaRenderer.getForegroundColor());
		textColumn.setScale(100);
				
		textArea.add(textColumn);
		textArea.addLayoutListener(new LayoutListener.Stub() {
			public void postLayout(IFigure container) {
				makeTextColumnFit(textColumn, textArea);
			}
		});
		
//		getRootFigure().setLayoutManager(new BorderLayout());
//		getRootFigure().addLayoutListener(new LayoutListener.Stub() {
//			@Override
//			public void postLayout(IFigure container) {
//				makeItFit();
//			}
//		});
//		
//		int columnCount = (!otherContent.isEmpty() ? 1 : 0) + (!rightContent.isEmpty() ? 1 : 0) + (!leftContent.isEmpty() ? 1 : 0);
//		
//		textColumn = new ListFigure(contentAreaRenderer.getResourceManager());
//		textColumn.setContent((IContent[]) otherContent.toArray(new IContent[otherContent.size()]));
//		textColumn.setFontDescription(contentAreaRenderer.getDefaultFontDescription());
//		textColumn.setForegroundColor(contentAreaRenderer.getForegroundColor());
//		
//		addFigureSizer(new ListFigureSizer(textColumn, 100));
//		
//		getRootFigure().add(textColumn);
//		getRootFigure().setConstraint(textColumn, BorderLayout.CENTER);
//		
//		int size = 100;
//
//		IFigure rightImageColumn = new Figure();
//		if (!rightContent.isEmpty()) {
//			rightImageColumn.setLayoutManager(new GridLayout());
//			
//			for(IContent content : rightContent) {
//				TextContent text = (TextContent)content;
//				for (Chunk chunk : text.getChunks()) {
//					layoutImageChunk(rightImageColumn, chunk);
//				}
//			}
//			getRootFigure().add(rightImageColumn);
//			getRootFigure().setConstraint(rightImageColumn, BorderLayout.RIGHT);
//
//			int rightHeightScale = getRootFigure().getBounds().height * 100 / rightImageColumn.getPreferredSize().height;
//			int rightWidthScale = getRootFigure().getBounds().width * (100/columnCount) / rightImageColumn.getPreferredSize().width;
//			
//			size = Math.min(size, rightHeightScale);
//			size = Math.min(size, rightWidthScale);
//		}
//
//		IFigure leftImageColumn = new Figure();
//		if (!leftContent.isEmpty()) {
//			leftImageColumn.setLayoutManager(new GridLayout());
//			
//			for(IContent content : leftContent) {
//				TextContent text = (TextContent)content;
//				for (Chunk chunk : text.getChunks()) {
//					layoutImageChunk(leftImageColumn, chunk);
//				}
//			}
//			getRootFigure().add(leftImageColumn);
//			getRootFigure().setConstraint(leftImageColumn, BorderLayout.LEFT);
//
//			int leftHeightScale = getRootFigure().getBounds().height * 100 / leftImageColumn.getPreferredSize().height;
//			int leftWidthScale = getRootFigure().getBounds().width * (100/columnCount) / leftImageColumn.getPreferredSize().width;
//
//			size = Math.min(size, leftHeightScale);
//			size = Math.min(size, leftWidthScale);
//		}
//		
//		if (size < 100) {
//			for (FigureSizer sizer : imageSizers) {
//				sizer.resizeTo(size);
//			}
//			getRootFigure().invalidateTree();
//		}

	}

	private void addFigures(IFigure parent, List<IFigure> otherFigures) {
		for(IFigure figure : otherFigures) addFigure(parent, figure);
	}

	private void addFigure(IFigure parent, IFigure figure) {
		parent.add(figure);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER, GridData.VERTICAL_ALIGN_CENTER, true, true);
		parent.setConstraint(figure, gridData);
	}

	private boolean isWide(IFigure figure) {
		Dimension size = figure.getPreferredSize();
		return size.width > (size.height * 2);
	}

	private boolean isTall(IFigure figure) {
		Dimension size = figure.getPreferredSize();
		return size.height > (size.width * 2);
	}

	void layoutWithOnlyPositionableContent() {
		if (positionableChunks.size() == 1) {
			layoutWithSinglePositionableFigure();
		}
	}

	void layoutWithSinglePositionableFigure() {
		getRootFigure().setLayoutManager(new GridLayout());
		IResizeableFigure figure = createFigure(positionableChunks.get(0));
		
		int scale = 100;
		scale = Math.min(scale,  getRootFigure().getBounds().width * 100 / figure.getPreferredSize().width);
		scale = Math.min(scale, getRootFigure().getBounds().height * 100 / figure.getPreferredSize().height);
		figure.setScale(scale);
		
		getRootFigure().add(figure);
		getRootFigure().setConstraint(figure, new GridData(SWT.CENTER, SWT.CENTER, true, true));
	}

	IResizeableFigure createFigure(Chunk chunk) {
		if (chunk instanceof ImageChunk) return createFigure((ImageChunk)chunk);
		if (chunk instanceof CodeBlockChunk) return createFigure((CodeBlockChunk)chunk);
		
		// TODO Return a placeholder instead.
		return new MessageFigure(getResourceManager(), contentAreaRenderer.getDefaultFontDescription(), "Unknown content type:" + chunk.getClass());
	}
	
	IResizeableFigure createFigure(CodeBlockChunk chunk) {
		CodeBlockFigure figure = new CodeBlockFigure(getResourceManager(), contentAreaRenderer.getDefaultFontDescription(), chunk.getText());
		figure.setBorder(new LineBorder());
		return figure;
	}

	IResizeableFigure createFigure(ImageChunk chunk) {
		try {
			Image image = getResourceManager().getImage(chunk.getBaseUrl(), chunk.getUrl());
			ResizeableImageFigure figure = new ResizeableImageFigure(image, chunk.getWidth(), chunk.getHeight());
			//figure.setBorder(new LineBorder());
			return figure;
		} catch (Exception e) {
			MessageFigure figure = new MessageFigure(getResourceManager(), contentAreaRenderer.getDefaultFontDescription(), e.toString());
			return figure;
		}
	}

}
