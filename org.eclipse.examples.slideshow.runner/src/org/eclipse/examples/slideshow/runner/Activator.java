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
package org.eclipse.examples.slideshow.runner;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {
	public static final String PLUGIN_ID = "org.eclipse.examples.slideshow.runner";
	private static Activator instance;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		instance = null;
	}
	
	public static Activator getDefault() {
		return instance;
	}
}
