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
package org.eclipse.examples.slideshow.jdt;

import java.util.Properties;

import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.url.URLConstants;
import org.osgi.service.url.URLStreamHandlerService;

/**
 * TODO Sort out timing issue
 * 
 * We have a timing issue with the bundle loading after a slide has already been
 * rendered. In this case, the protocol we specify doesn't get registered in
 * time and ends up with a &quot;not found&quot; error. Once we load,
 * everything's good.
 */
public class Activator implements BundleActivator, IStartup {
	private ServiceRegistration javaSourceUrlHandlerService;

	public void start(BundleContext context) throws Exception {
		startJavaSourceUrlHandlerService(context);
	}

	public void stop(BundleContext context) throws Exception {
		javaSourceUrlHandlerService.unregister();
	}

	public void earlyStartup() {
		// TODO Auto-generated method stub
		
	}

	private void startJavaSourceUrlHandlerService(BundleContext context) {
		Properties properties = new Properties();
		properties.put(URLConstants.URL_HANDLER_PROTOCOL, new String[] {JavaSourceStreamHandlerService.PROTOCOL});
		javaSourceUrlHandlerService = context.registerService(URLStreamHandlerService.class.getName(), new JavaSourceStreamHandlerService(), properties);
	}
}
