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

import org.eclipse.draw2d.Label;

public class MessageFigure extends Label implements IResizeableFigure {
	public MessageFigure(String message) {
		this.setText(message);
	}

	public void setScale(int percent) {
		// TODO Auto-generated method stub

	}

}
