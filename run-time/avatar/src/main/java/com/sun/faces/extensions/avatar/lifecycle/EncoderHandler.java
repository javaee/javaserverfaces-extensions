/*
 *
 * Created on 11 novembre 2006, 19.40
 */

package com.sun.faces.extensions.avatar.lifecycle;

import javax.faces.component.UIComponent;

/**
 *
 * @author agori
 */
public interface EncoderHandler {
    
    public ComponentEncoder getEncoder(UIComponent component);
   
}