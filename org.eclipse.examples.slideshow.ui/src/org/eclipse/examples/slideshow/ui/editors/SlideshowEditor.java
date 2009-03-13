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
package org.eclipse.examples.slideshow.ui.editors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.examples.slideshow.core.Slide;
import org.eclipse.examples.slideshow.core.SlideDeck;
import org.eclipse.examples.slideshow.ui.Activator;
import org.eclipse.examples.slideshow.ui.model.ISlideshowUIService;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class SlideshowEditor extends TextEditor {
	SlideDeck slideDeck;
	List<Slide> lineNumberToSlideMap = new ArrayList<Slide>();
	
	private ServiceTracker slideshowUIServiceTracker;
	private IPartListener listener = new IPartListener() {
		public void partActivated(IWorkbenchPart part) {}

		public void partBroughtToTop(IWorkbenchPart part) {
			if (part == SlideshowEditor.this) becomeProvider();
		}

		public void partClosed(IWorkbenchPart part) {}

		public void partDeactivated(IWorkbenchPart part) {}

		public void partOpened(IWorkbenchPart part) {}
	};
		
	public SlideshowEditor() {
		super();
		setSourceViewerConfiguration(new SourceViewerConfiguration());
		setDocumentProvider(new FileDocumentProvider());
	}
	
	ISlideshowUIService getSlideshowUIService() {
		return (ISlideshowUIService) slideshowUIServiceTracker.getService();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		slideshowUIServiceTracker = new ServiceTracker(getBundleContext(), ISlideshowUIService.class.getName(), null);
		slideshowUIServiceTracker.open();
		installPartListener();
	}
		
	private BundleContext getBundleContext() {
		return Activator.getDefault().getContext();
	}

	public void dispose() {
		getSlideshowUIService().setProvider(null);
		removePartListener();
		slideshowUIServiceTracker.close();
		super.dispose();
	}
	
	void installPartListener() {
		getSite().getPage().addPartListener(listener);
	}
	
	void removePartListener() {
		getSite().getPage().removePartListener(listener);
	}
	
	void becomeProvider() {
		getSlideshowUIService().setProvider(new ISlideshowSourceProvider() {
			public String getSource() {
				return getSourceViewer().getDocument().get();
			}
			
			public ITextSelection getSelection() {
				return (ITextSelection) getSourceViewer().getSelectionProvider().getSelection();
			};
			
			public void addTextListener(ITextListener listener) {
				getSourceViewer().addTextListener(listener);
			}
			
			public void removeTextListener(ITextListener listener) {
				getSourceViewer().removeTextListener(listener);
			}
			
			public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
				((TextViewer)getSourceViewer()).addPostSelectionChangedListener(listener);
			}
			
			public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
				((TextViewer)getSourceViewer()).removePostSelectionChangedListener(listener);
			}

			public URL getBaseUrl() {
				try {
					// TODO Too tightly coupled, and too weird. Is there a better way?
					return ((FileEditorInput)getEditorInput()).getPath().toFile().getParentFile().toURL();
				} catch (MalformedURLException e) {
					return null;
				}
			}
		});
	}
}
