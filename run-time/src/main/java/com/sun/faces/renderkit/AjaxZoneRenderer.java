package com.sun.faces.renderkit;

import com.sun.faces.components.ProcessingContextViewRoot;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.el.ValueExpression;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * This class renderers TextField components.
 */
public class AjaxZoneRenderer extends Renderer {
    // Request parameters.
    private static final String IDS_PARAM = "sjwuic_ids";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Renderer methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Render the beginning of the specified UIComponent to the output stream or
     * writer associated with the response we are creating.
     *
     * @param context FacesContext for the current request.
     * @param component UIComponent to be rendered.
     *
     * @exception IOException if an input/output error occurs.
     * @exception NullPointerException if context or component is null.
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        if (context == null || component == null) {

            throw new NullPointerException();
        }
        if (!component.isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        String id = component.getClientId(FacesContext.getCurrentInstance());
        if (isAjaxRequest(context, component)) {
            writer.startElement("processing-context", component);
            writer.writeAttribute("id",component.getClientId(context),
                    "clientId");
            writer.write("<![CDATA[");
        } else {
            writer.write("\n");
            writer.startElement("div", component);
            writer.writeAttribute("id", id, null);
            writeStyle(context, writer, component);
        }
    }
    
    private void writeStyle(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException {
        String style = null;
        ValueExpression styleExp = null;
        
        if (null == (style = (String) comp.getAttributes().get("style"))) {
            if (null != (styleExp = comp.getValueExpression("style"))) {
                style = (String) styleExp.getValue(context.getELContext());
            }
        }
        
        if (null != style) {
            writer.writeAttribute("style", style, null);
        }
    }

    /**
     * Render the children of the specified UIComponent to the output stream or
     * writer associated with the response we are creating.
     *
     * @param context FacesContext for the current request.
     * @param component UIComponent to be rendered.
     *
     * @exception IOException if an input/output error occurs.
     * @exception NullPointerException if context or component is null.
     */
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (!component.isRendered()) {
            return;
        }

        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next(); 
            if (!kid.isRendered()) {
                continue;
            }
            kid.encodeAll(context);
        }
    }

    /**
     * Render the ending of the specified UIComponent to the output stream or
     * writer associated with the response we are creating.
     *
     * @param context FacesContext for the current request.
     * @param component UIComponent to be rendered.
     *
     * @exception IOException if an input/output error occurs.
     * @exception NullPointerException if context or component is null.
     */
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (!component.isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        if (isAjaxRequest(context, component)) {
            writer.write("]]>");
            writer.endElement("processing-context");
        } else {
            writer.endElement("div"); //NOI18N
        }
    }

    /**
     * Return a flag indicating whether this Renderer is responsible
     * for rendering the children the component it is asked to render.
     * The default implementation returns false.
     */
    public boolean getRendersChildren() {
        return true; 
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Helper method to test if this is an Ajax request
    private boolean isAjaxRequest(FacesContext context, UIComponent component) {
        Map<String, Object> requestMap = 
                context.getExternalContext().getRequestMap();
        return requestMap.containsKey(ProcessingContextViewRoot.PROCESSING_CONTEXTS_REQUEST_PARAM_NAME);
    }
}