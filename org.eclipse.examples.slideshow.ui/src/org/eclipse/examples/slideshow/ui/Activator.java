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
package org.eclipse.examples.slideshow.ui;

import org.eclipse.examples.slideshow.ui.model.ISlideshowUIService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.examples.slideshow.ui";

	// The shared instance
	private static Activator plugin;

	private SlideshowUIService slideShowUIService;
	
	private ServiceRegistration slideshowUIServiceRegistration;
	private ServiceRegistration eclipseStandardTemplateProviderService;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		slideshowUIServiceRegistration = startSlideshowUIService();
		startEclipseTemplateService();
	}

	void startEclipseTemplateService() {
		eclipseStandardTemplateProviderService = getContext().registerService(ISlideshowTemplateProvider.class.getName(), new EclipseStandardTemplateProvider(), null);
	}

	ServiceRegistration startSlideshowUIService() {
		slideShowUIService = new SlideshowUIService();
		return getContext().registerService(ISlideshowUIService.class.getName(), slideShowUIService, null);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		eclipseStandardTemplateProviderService.unregister();
		slideShowUIService.dispose();
		slideshowUIServiceRegistration.unregister();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public BundleContext getContext() {
		return getBundle().getBundleContext();
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
