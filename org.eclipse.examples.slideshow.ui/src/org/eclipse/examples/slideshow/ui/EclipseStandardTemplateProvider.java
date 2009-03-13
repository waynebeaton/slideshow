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

import org.eclipse.examples.slideshow.resources.ITemplate;
import org.eclipse.examples.slideshow.templates.eclipse.EclipseStandardTemplate;
import org.eclipse.swt.graphics.Device;

public class EclipseStandardTemplateProvider implements ISlideshowTemplateProvider {

	public ITemplate createTemplate(Device device) {
		return new EclipseStandardTemplate(device);
	}

	public String getName() {
		return "Eclipse Standard";
	}

	public String getDescription() {
		return "The standard presentation template used by The Eclipse Foundation.";
	}

}
