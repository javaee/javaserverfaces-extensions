/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package fireAjaxTrans;


import java.io.IOException;
import javax.el.*;
import javax.faces.*;
import javax.faces.component.*;
import javax.faces.context.*;
import javax.faces.convert.*;
import javax.faces.el.*;
import javax.faces.event.*;
import javax.faces.validator.*;
import javax.faces.webapp.*;
import javax.servlet.jsp.JspException;



public final class ButtonTag extends UIComponentELTag {


    // Setter Methods
    // PROPERTY: action
    private javax.el.MethodExpression action;
    public void setAction(javax.el.MethodExpression action) {
        this.action = action;
    }

    // PROPERTY: actionListener
    private javax.el.MethodExpression actionListener;
    public void setActionListener(javax.el.MethodExpression actionListener) {
        this.actionListener = actionListener;
    }

    // PROPERTY: immediate
    private javax.el.ValueExpression immediate;
    public void setImmediate(javax.el.ValueExpression immediate) {
        this.immediate = immediate;
    }

    // PROPERTY: value
    private javax.el.ValueExpression value;
    public void setValue(javax.el.ValueExpression value) {
        this.value = value;
    }

    // PROPERTY: accesskey
    private javax.el.ValueExpression accesskey;
    public void setAccesskey(javax.el.ValueExpression accesskey) {
        this.accesskey = accesskey;
    }

    // PROPERTY: alt
    private javax.el.ValueExpression alt;
    public void setAlt(javax.el.ValueExpression alt) {
        this.alt = alt;
    }

    // PROPERTY: dir
    private javax.el.ValueExpression dir;
    public void setDir(javax.el.ValueExpression dir) {
        this.dir = dir;
    }

    // PROPERTY: disabled
    private javax.el.ValueExpression disabled;
    public void setDisabled(javax.el.ValueExpression disabled) {
        this.disabled = disabled;
    }

    // PROPERTY: image
    private javax.el.ValueExpression image;
    public void setImage(javax.el.ValueExpression image) {
        this.image = image;
    }

    // PROPERTY: label
    private javax.el.ValueExpression label;
    public void setLabel(javax.el.ValueExpression label) {
        this.label = label;
    }

    // PROPERTY: lang
    private javax.el.ValueExpression lang;
    public void setLang(javax.el.ValueExpression lang) {
        this.lang = lang;
    }

    // PROPERTY: onblur
    private javax.el.ValueExpression onblur;
    public void setOnblur(javax.el.ValueExpression onblur) {
        this.onblur = onblur;
    }

    // PROPERTY: onchange
    private javax.el.ValueExpression onchange;
    public void setOnchange(javax.el.ValueExpression onchange) {
        this.onchange = onchange;
    }

    // PROPERTY: onclick
    private javax.el.ValueExpression onclick;
    public void setOnclick(javax.el.ValueExpression onclick) {
        this.onclick = onclick;
    }

    // PROPERTY: ondblclick
    private javax.el.ValueExpression ondblclick;
    public void setOndblclick(javax.el.ValueExpression ondblclick) {
        this.ondblclick = ondblclick;
    }

    // PROPERTY: onfocus
    private javax.el.ValueExpression onfocus;
    public void setOnfocus(javax.el.ValueExpression onfocus) {
        this.onfocus = onfocus;
    }

    // PROPERTY: onkeydown
    private javax.el.ValueExpression onkeydown;
    public void setOnkeydown(javax.el.ValueExpression onkeydown) {
        this.onkeydown = onkeydown;
    }

    // PROPERTY: onkeypress
    private javax.el.ValueExpression onkeypress;
    public void setOnkeypress(javax.el.ValueExpression onkeypress) {
        this.onkeypress = onkeypress;
    }

    // PROPERTY: onkeyup
    private javax.el.ValueExpression onkeyup;
    public void setOnkeyup(javax.el.ValueExpression onkeyup) {
        this.onkeyup = onkeyup;
    }

    // PROPERTY: onmousedown
    private javax.el.ValueExpression onmousedown;
    public void setOnmousedown(javax.el.ValueExpression onmousedown) {
        this.onmousedown = onmousedown;
    }

    // PROPERTY: onmousemove
    private javax.el.ValueExpression onmousemove;
    public void setOnmousemove(javax.el.ValueExpression onmousemove) {
        this.onmousemove = onmousemove;
    }

    // PROPERTY: onmouseout
    private javax.el.ValueExpression onmouseout;
    public void setOnmouseout(javax.el.ValueExpression onmouseout) {
        this.onmouseout = onmouseout;
    }

    // PROPERTY: onmouseover
    private javax.el.ValueExpression onmouseover;
    public void setOnmouseover(javax.el.ValueExpression onmouseover) {
        this.onmouseover = onmouseover;
    }

    // PROPERTY: onmouseup
    private javax.el.ValueExpression onmouseup;
    public void setOnmouseup(javax.el.ValueExpression onmouseup) {
        this.onmouseup = onmouseup;
    }

    // PROPERTY: onselect
    private javax.el.ValueExpression onselect;
    public void setOnselect(javax.el.ValueExpression onselect) {
        this.onselect = onselect;
    }

    // PROPERTY: readonly
    private javax.el.ValueExpression readonly;
    public void setReadonly(javax.el.ValueExpression readonly) {
        this.readonly = readonly;
    }

    // PROPERTY: style
    private javax.el.ValueExpression style;
    public void setStyle(javax.el.ValueExpression style) {
        this.style = style;
    }

    // PROPERTY: styleClass
    private javax.el.ValueExpression styleClass;
    public void setStyleClass(javax.el.ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    // PROPERTY: tabindex
    private javax.el.ValueExpression tabindex;
    public void setTabindex(javax.el.ValueExpression tabindex) {
        this.tabindex = tabindex;
    }

    // PROPERTY: title
    private javax.el.ValueExpression title;
    public void setTitle(javax.el.ValueExpression title) {
        this.title = title;
    }

    // PROPERTY: type
    private javax.el.ValueExpression type;
    public void setType(javax.el.ValueExpression type) {
        this.type = type;
    }


    // General Methods
    public String getRendererType() {
        return "javax.faces.Button";
    }

    public String getComponentType() {
        return "Button";
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        javax.faces.component.UICommand command = null;
        try {
            command = (javax.faces.component.UICommand) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + 
					    component.toString() + 
					    " not expected type.  Expected:" +
					  " javax.faces.component.UICommand." +
					    " Perhaps you're missing a tag?");
        }

        if (action != null) {
            command.setActionExpression(action);
        }
        if (actionListener != null) {
            command.addActionListener(new 
               MethodExpressionActionListener(actionListener));
        }
        if (immediate != null) {
            command.setValueExpression("immediate", immediate);
        }

        if (value != null) {
            command.setValueExpression("value", value);
        }

        if (accesskey != null) {
            command.setValueExpression("accesskey", accesskey);
        }
        if (alt != null) {
            command.setValueExpression("alt", alt);
        }
        if (dir != null) {
            command.setValueExpression("dir", dir);
        }
        if (disabled != null) {
            command.setValueExpression("disabled", disabled);
        }
        if (image != null) {
            command.setValueExpression("image", image);
        }
        if (label != null) {
            command.setValueExpression("label", label);
        }
        if (lang != null) {
            command.setValueExpression("lang", lang);
        }
        if (onblur != null) {
            command.setValueExpression("onblur", onblur);
        }
        if (onchange != null) {
            command.setValueExpression("onchange", onchange);
        }
        if (onclick != null) {
            command.setValueExpression("onclick", onclick);
        }
        if (ondblclick != null) {
            command.setValueExpression("ondblclick", ondblclick);
        }
        if (onfocus != null) {
            command.setValueExpression("onfocus", onfocus);
        }
        if (onkeydown != null) {
            command.setValueExpression("onkeydown", onkeydown);
        }
        if (onkeypress != null) {
            command.setValueExpression("onkeypress", onkeypress);
        }
        if (onkeyup != null) {
            command.setValueExpression("onkeyup", onkeyup);
        }
        if (onmousedown != null) {
            command.setValueExpression("onmousedown", onmousedown);
        }
        if (onmousemove != null) {
            command.setValueExpression("onmousemove", onmousemove);
        }
        if (onmouseout != null) {
            command.setValueExpression("onmouseout", onmouseout);
        }
        if (onmouseover != null) {
            command.setValueExpression("onmouseover", onmouseover);
        }
        if (onmouseup != null) {
            command.setValueExpression("onmouseup", onmouseup);
        }
        if (onselect != null) {
            command.setValueExpression("onselect", onselect);
        }
        if (readonly != null) {
            command.setValueExpression("readonly", readonly);
        }
        if (style != null) {
            command.setValueExpression("style", style);
        }
        if (styleClass != null) {
            command.setValueExpression("styleClass", styleClass);
        }
        if (tabindex != null) {
            command.setValueExpression("tabindex", tabindex);
        }
        if (title != null) {
            command.setValueExpression("title", title);
        }
        if (type != null) {
            command.setValueExpression("type", type);
        }
    }
    // Methods From TagSupport
    public int doStartTag() throws JspException {
        try {
            return super.doStartTag();
        } catch (Exception e) {
            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            throw new JspException(root);
        }
    }

    public int doEndTag() throws JspException {
        try {
            return super.doEndTag();
        } catch (Exception e) {
            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            throw new JspException(root);
        }
    }

    // RELEASE
    public void release() {
        super.release();

        // component properties
        this.action = null;
        this.actionListener = null;
        this.immediate = null;
        this.value = null;

        // rendered attributes
        this.accesskey = null;
        this.alt = null;
        this.dir = null;
        this.disabled = null;
        this.image = null;
        this.label = null;
        this.lang = null;
        this.onblur = null;
        this.onchange = null;
        this.onclick = null;
        this.ondblclick = null;
        this.onfocus = null;
        this.onkeydown = null;
        this.onkeypress = null;
        this.onkeyup = null;
        this.onmousedown = null;
        this.onmousemove = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.onmouseup = null;
        this.onselect = null;
        this.readonly = null;
        this.style = null;
        this.styleClass = null;
        this.tabindex = null;
        this.title = null;
        this.type = null;
    }

    public String getDebugString() {
        return "id: " + this.getId() + " class: " + this.getClass().getName();
    }

}
