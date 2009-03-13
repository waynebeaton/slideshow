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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.examples.slideshow.core.BlockChunk;
import org.eclipse.examples.slideshow.core.Chunk;
import org.eclipse.examples.slideshow.core.CodeBlockChunk;
import org.eclipse.examples.slideshow.core.IContent;
import org.eclipse.examples.slideshow.core.ImageChunk;
import org.eclipse.examples.slideshow.core.ListContent;
import org.eclipse.examples.slideshow.core.ListItemContent;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.core.SpanChunk;
import org.eclipse.examples.slideshow.core.TextChunk;
import org.eclipse.examples.slideshow.core.TextContent;
import org.eclipse.examples.slideshow.core.ListContent.ListType;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.ImageAttributes;

public class SlideDeckDocumentBuilder extends DocumentBuilder {

	SlideDeck slideDeck;
	Stack<PartBuilder> stack = new Stack<PartBuilder>();
	ListenerList listeners = new ListenerList();
	private final URL baseUrl;
	
	public SlideDeckDocumentBuilder(URL baseUrl) {
		this.baseUrl = baseUrl;	
	}
	
	@Override
	public void acronym(String text, String definition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginBlock(BlockType type, Attributes attributes) {
		if (type == BlockType.BULLETED_LIST) stack.push(new ListBuilder(getCurrentPartBuilder(), ListContent.ListType.BULLETED, attributes));
		else if (type == BlockType.NUMERIC_LIST) stack.push(new ListBuilder(getCurrentPartBuilder(), ListContent.ListType.NUMERIC, attributes));
		else if (type == BlockType.LIST_ITEM) stack.push(new ListItemBuilder(getCurrentPartBuilder()));
		else if (type == BlockType.PARAGRAPH) stack.push(new ParagraphBuilder(getCurrentPartBuilder()));
		else stack.push(new NoopBuilder(getCurrentPartBuilder()));
	}

	private PartBuilder getCurrentPartBuilder() {
		if (stack.isEmpty()) return null;
		return stack.peek();
	}

	@Override
	public void endBlock() {
		getCurrentPartBuilder().end();
		stack.pop();
	}
	
	@Override
	public void beginDocument() {
		slideDeck = new SlideDeck(baseUrl);
	}

	@Override
	public void endDocument() {
		clearBuilderStack();
	}
	
	@Override
	public void beginHeading(int level, Attributes attributes) {
		clearBuilderStack();
		stack.push(new SlideBuilder(level));
	}

	private void clearBuilderStack() {		
		while (!stack.isEmpty()) stack.pop().end();
	}

	@Override
	public void endHeading() {
		
	}

	@Override
	public void beginSpan(SpanType type, Attributes attributes) {
		stack.push(new SpanBuilder(getCurrentPartBuilder(), type, attributes));
	}

	@Override
	public void characters(String text) {
		getCurrentPartBuilder().characters(text);
	}

	@Override
	public void charactersUnescaped(String literal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endSpan() {
		stack.pop().end();
	}

	@Override
	public void entityReference(String entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void image(Attributes attributes, String url) {
		getCurrentPartBuilder().image(url, (ImageAttributes)attributes);
	}

	@Override
	public void imageLink(Attributes linkAttributes,
			Attributes imageAttributes, String href, String imageUrl) {
		// TODO Auto-generated method stub
	}

	@Override
	public void lineBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void link(Attributes attributes, String hrefOrHashName, String text) {
		beginSpan(SpanType.MONOSPACE, new Attributes());
		characters(text);
		endSpan();
	}

	public void copyright(String message) {
		getCurrentPartBuilder().setCopyright(message);
	}

	public void codeBlock(String url) {
		getCurrentPartBuilder().codeBlock(url);
	}
	
	public SlideDeck getSlideDeck() {
		return slideDeck;
	}
	
	class PartBuilder {

		final PartBuilder parent;

		public PartBuilder(PartBuilder parent) {
			this.parent = parent;
		}

		public void codeBlock(String url) {}

		public void end() {}

		public void image(String url, ImageAttributes attributes) {}

		public void setCopyright(String message) {
			getSlideDeck().setCopyright(message);
		}

		public void characters(String text) {
			PartBuilder parent = getParent();
			if (parent == null) return;
			parent.characters(text);
		}

		PartBuilder getParent() {
			return parent;
		}

		SlideBuilder getSlideBuilder() {
			if (parent == null) return null;
			if (parent instanceof SlideBuilder) return (SlideBuilder) parent;
			return parent.getSlideBuilder();
		}
		
		Slide getSlide() {
			if (getSlideBuilder() == null) return null;
			return getSlideBuilder().getSlide();
		}
		
		ListBuilder getListBuilder() {
			if (parent instanceof ListBuilder) return (ListBuilder) parent;
			return parent.getListBuilder();
		}

		/**
		 * This method adds the provided instance of {@link IContent} to the
		 * receiver. In the default case, we assume that our object doesn't know
		 * how to hold content, so we pass the request onto the parent. If no
		 * parent exists, we bail out (thereby ignoring the content).
		 * 
		 * @param content
		 */
		void addContent(IContent content) {
			if (parent == null) return;
			parent.addContent(content);
		}
	}
	
	class NoopBuilder extends PartBuilder {

		public NoopBuilder(PartBuilder parent) {
			super(parent);
		}
		
	}

	class SlideBuilder extends PartBuilder {

		Slide slide;
		List<IContent> contents = new ArrayList<IContent>();

		public SlideBuilder(int level) {
			super(null);
			slide = new Slide();
			slide.setLevel(level);
			fireSlideAddedEvent(slide);
		}
		
		public Slide getSlide() {
			return slide;
		}
		
		@Override
		public void characters(String text) {
			slide.setTitle(text);
		}

		@Override
		void addContent(IContent item) {
			contents.add(item);
		}		
		
		@Override
		public void setCopyright(String copyright) {
			slide.setCopyright(copyright);
		}
		
		@Override
		public void end() {
			slide.setContents((IContent[]) contents.toArray(new IContent[contents.size()]));
			slideDeck.add(slide);
		}
	}
	
	class ListBuilder extends PartBuilder {
		List<IContent> items = new ArrayList<IContent>();
		ListType type;
		Attributes attributes;
		
		public ListBuilder(PartBuilder parent, ListType type, Attributes attributes) {
			super(parent);
			this.type = type;
			this.attributes = attributes;
		}
				
		public void addListItem(ListItemContent bulletContent) {
			items.add(bulletContent);
		}
		
		@Override
		public void end() {
			parent.addContent(new ListContent((IContent[]) items.toArray(new IContent[items.size()]), type));
		}

		@Override
		void addContent(IContent item) {
			items.add(item);
		}
	}
		
	/**
	 * The TextBuilder class is a generic sort of text handler that gathers
	 * {@link Chunk}s of text, images, etc. This type is abstract; subclasses are
	 * expected to do something with the gathere chunks.
	 */
	abstract class TextBuilder extends PartBuilder {
		List<Chunk> chunks = new ArrayList<Chunk>();

		public TextBuilder(PartBuilder parent) {
			super(parent);
		}

		@Override
		public void characters(String characters) {
			chunks.add(new TextChunk(getSlide(), characters));
		}
				
		Chunk[] getChunks() {
			return (Chunk[]) chunks.toArray(new Chunk[chunks.size()]);
		}
		
		@Override
		public abstract void end();

		@Override
		public void image(String url, ImageAttributes attributes) {
			int align = BlockChunk.ALIGN_NONE;
			if (attributes.getAlign() == ImageAttributes.Align.Left) align = BlockChunk.ALIGN_LEFT; 
			else if (attributes.getAlign() == ImageAttributes.Align.Right) align = BlockChunk.ALIGN_RIGHT; 
			
			BlockChunk chunk = new ImageChunk(getSlide(), url, align, attributes.getWidth(), attributes.getHeight());
			chunks.add(chunk);
		}
		
		@Override
		public void codeBlock(String url) {
			CodeBlockChunk chunk = new CodeBlockChunk(getSlide(), url, BlockChunk.ALIGN_NONE);
			chunks.add(chunk);
		}
	}

	class SpanBuilder extends TextBuilder {

		private final SpanType type;

		public SpanBuilder(PartBuilder parent, SpanType type, Attributes attributes) {
			super(parent);
			this.type = type;
		}

		@Override
		public void end() {
			int style = SpanChunk.STYLE_NONE;
			if (type == SpanType.ITALIC) style = SpanChunk.STYLE_ITALICS;
			else if (type == SpanType.BOLD) style = SpanChunk.STYLE_BOLD;
			
			((TextBuilder)getParent()).chunks.add(new SpanChunk(getSlide(), getChunks(), style));
		}
		
	}
	
	/**
	 * The ParagraphBuilder class adds an instance of {@link TextContent} to the
	 * current slide. The instance assumes that a {@link SlideBuilder} has been
	 * created lower on the stack.
	 */
	class ParagraphBuilder extends TextBuilder {
		public ParagraphBuilder(PartBuilder parent) {
			super(parent);
		}

		@Override
		public void end() {
			SlideBuilder slideBuilder = getSlideBuilder();
			if (slideBuilder == null) return;
			slideBuilder.addContent(new TextContent(getChunks()));
		}
	}
	
	/**
	 * The ListItemBuilder class adds a list item (bullet or numbered entry) to
	 * the parent list. The instance assumes that a type of {@link ListBuilder}
	 * has been created lower on the stack.
	 */
	class ListItemBuilder extends TextBuilder {
		List<IContent> nested = new ArrayList<IContent>();

		public ListItemBuilder(PartBuilder parent) {
			super(parent);
		}

		@Override
		public void end() {
			ListItemContent item = new ListItemContent(getChunks(), (IContent[]) nested.toArray(new IContent[nested.size()]));
			parent.addContent(item);
		}
		
		@Override
		void addContent(IContent content) {
			nested.add(content);
		}
	}

	public void addSlideDeckDocumentListener(ISlideDeckDocumentListener slideDeckDocumentListener) {
		listeners.add(slideDeckDocumentListener);
	}

	void fireSlideAddedEvent(Slide slide) {
		if (listeners == null) return;
		if (listeners.isEmpty()) return;
		
		SlideAddedEvent event = new SlideAddedEvent(slide, getLocator().getLineNumber()-1);

		for(Object listener : listeners.getListeners()) {
			((ISlideDeckDocumentListener)listener).slideAdded(event);
		}
	}
}