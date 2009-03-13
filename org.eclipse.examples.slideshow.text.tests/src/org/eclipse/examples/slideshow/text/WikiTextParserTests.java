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

import static org.junit.Assert.assertEquals;

import org.eclipse.examples.slideshow.core.BlockChunk;
import org.eclipse.examples.slideshow.core.IContent;
import org.eclipse.examples.slideshow.core.ImageChunk;
import org.eclipse.examples.slideshow.core.ListContent;
import org.eclipse.examples.slideshow.core.ListItemContent;
import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.core.SpanChunk;
import org.eclipse.examples.slideshow.core.TextChunk;
import org.eclipse.examples.slideshow.core.TextContent;
import org.junit.Test;


public class WikiTextParserTests {
	@Test
	public void testParseSingleSlide() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("=Title=\n* Bullet One\n*Bullet Two");
		
		Slide slide = deck.getSlide(0);		
		assertEquals("Title", slide.getTitle());
		
		IContent[] listContent = ((ListContent)slide.getContent()[0]).getItems();
		assertEquals("Bullet One", ((ListItemContent)listContent[0]).getText());
		assertEquals("Bullet Two", ((ListItemContent)listContent[1]).getText());
	}
	
	@Test
	public void testParseMultipleSlides() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("=Title One=\n* Bullet One\n*Bullet Two\n=Title Two=\n*Bullet Three");
		
		Slide slide1 = deck.getSlide(0);		
		assertEquals("Title One", slide1.getTitle());
		
		IContent[] listContent = ((ListContent)slide1.getContent()[0]).getItems();
		assertEquals("Bullet One", ((ListItemContent)listContent[0]).getText());
		assertEquals("Bullet Two", ((ListItemContent)listContent[1]).getText());
		
		Slide slide2 = deck.getSlide(1);		
		listContent = ((ListContent)slide2.getContent()[0]).getItems();
		assertEquals("Title Two", slide2.getTitle());
		assertEquals("Bullet Three", ((ListItemContent)listContent[0]).getText());
	}
	
	@Test
	public void testParseNumericBullets() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("=Title=\n# Bullet One\n#Bullet Two");
		
		Slide slide = deck.getSlide(0);		
		assertEquals("Title", slide.getTitle());
		
		IContent[] listContent = ((ListContent)slide.getContent()[0]).getItems();
		assertEquals("Bullet One", ((ListItemContent)listContent[0]).getText());
		assertEquals("Bullet Two", ((ListItemContent)listContent[1]).getText());
	}
	
	
	@Test
	public void testParseWithNestedBullets() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("=Title=\n*Bullet One\n**Bullet Two\n**Bullet Three\n*Bullet Four");
		
		Slide slide = deck.getSlide(0);		
		assertEquals("Title", slide.getTitle());
		
		IContent[] topLevel = ((ListContent)slide.getContent()[0]).getItems();
		//ListItemContent firstItem = topLevel[0]
		ListItemContent firstItem = (ListItemContent)topLevel[0];
		assertEquals("Bullet One", firstItem.getText());
		
		IContent[] nested = ((ListContent)firstItem.getNestedContent()[0]).getItems();
		assertEquals("Bullet Two", ((ListItemContent)nested[0]).getText());
		assertEquals("Bullet Three", ((ListItemContent)nested[1]).getText());
		
		assertEquals("Bullet Four", ((ListItemContent)topLevel[1]).getText());
	}
	
	@Test
	public void testParseWithNestedTags() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("=Title One=\nParagraph ''italics''\n\nParagraph '''Bold'''");
		
		Slide slide1 = deck.getSlide(0);		
		assertEquals("Title One", slide1.getTitle());
		
		assertEquals("Paragraph italics", ((TextContent)slide1.getContent()[0]).getText());
		assertEquals("Paragraph ", ((TextChunk)((TextContent)slide1.getContent()[0]).getChunk(0)).getText());
		SpanChunk italicsSpan = (SpanChunk)((TextContent)slide1.getContent()[0]).getChunk(1);
		assertEquals(SpanChunk.STYLE_ITALICS, italicsSpan.getStyle());
		assertEquals("italics", ((TextChunk)italicsSpan.getChunk(0)).getText());
		
		assertEquals("Paragraph Bold", ((TextContent)slide1.getContent()[1]).getText());
		SpanChunk boldSpan = (SpanChunk)((TextContent)slide1.getContent()[1]).getChunk(1);
		assertEquals(SpanChunk.STYLE_BOLD, boldSpan.getStyle());
		assertEquals("Bold", boldSpan.getText());
	}

	@Test
	public void testParseWithImageTag() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("=Title One=\n[[Image:junk.jpg]]\n\nParagraph ''italics''\n\nParagraph '''Bold'''");
		
		Slide slide1 = deck.getSlide(0);		
		assertEquals("Title One", slide1.getTitle());
		assertEquals("junk.jpg", ((ImageChunk)((TextContent)slide1.getContent()[0]).getChunk(0)).getUrl()); 
		assertEquals("Paragraph italics", ((TextContent)slide1.getContent()[1]).getText());
		assertEquals("Paragraph Bold", ((TextContent)slide1.getContent()[2]).getText());
	}	
	
	@Test
	public void testParseImageTagParts() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("=Title One=\n[[Image:junk.jpg|30px|right]]");
		
		Slide slide1 = deck.getSlide(0);		
		
		ImageChunk imageChunk = (ImageChunk)((TextContent)slide1.getContent()[0]).getChunk(0);
		assertEquals("junk.jpg", imageChunk.getUrl()); 
		assertEquals(BlockChunk.ALIGN_RIGHT, imageChunk.getAlign()); 
		assertEquals(30, imageChunk.getWidth()); 
		assertEquals(30, imageChunk.getHeight()); 
	}	
	
	@Test
	public void testParseWithCopyrightTag() throws Exception {
		SlideDeck deck = new WikiTextParser().parse("{Copyright:Copyright 2008, The Eclipse Foundation}\n=My Talk=\nWayne Beaton, The Eclipse Foundation");
		
		assertEquals("Copyright 2008, The Eclipse Foundation", deck.getCopyright());
		Slide slide1 = deck.getSlide(0);		
		assertEquals("My Talk", slide1.getTitle());
		assertEquals("Wayne Beaton, The Eclipse Foundation", ((TextContent)slide1.getContent()[0]).getText());
	}
}
