package com.sun.faces.extensions.avatar.lifecycle;

import com.sun.faces.extensions.avatar.application.DeferredStateManager;
import com.sun.faces.extensions.common.util.FastWriter;
import com.sun.faces.extensions.common.util.Util;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.ContextCallback;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.convert.Converter;

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class AsyncResponse {
    
    private final static ThreadLocal<AsyncResponse> Instance = new ThreadLocal<AsyncResponse>();
    
    private String viewState;

    private AsyncResponse() {
        super();
    }
    
    public static boolean exists() {
        return Instance.get() != null;
    }
    
    public static AsyncResponse getInstance(boolean create) {
        AsyncResponse ar = Instance.get();
        if (ar == null && create) {
            ar = new AsyncResponse();
            Instance.set(ar);
        }
        return ar;
    }
    
    public static AsyncResponse getInstance() {
        return getInstance(true);
    }
    
    public static void clearInstance() {
        AsyncResponse instance = getInstance(false);
        if (null != instance) {
            instance.clearSubtrees();
            instance.ajaxResponseWriter = null;
            instance.removeNoOpResponse(FacesContext.getCurrentInstance());
        }
        Instance.remove();
    }
    
    public void setViewState(String output) {
        this.viewState = output;
    }
    
    public String getViewState(FacesContext context) {
        if (null == this.viewState) {
            Object stateManagerObj = null;
            if (null != (stateManagerObj = context.getApplication().getStateManager()) &&
                stateManagerObj instanceof DeferredStateManager) {
                DeferredStateManager dsm = (DeferredStateManager) stateManagerObj;
                ResponseWriter rw = null;
                try {
                    rw = this.getResponseWriter();
                    FastWriter fw = new FastWriter(256);
                    StateCapture sc = new StateCapture(rw.cloneWithWriter(fw), fw);
                    context.setResponseWriter(sc);
                    Object stateObj = dsm.getWrapped().saveView(context);
                    dsm.getWrapped().writeState(context, stateObj);
                    this.viewState = sc.getState();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                finally {
                    context.setResponseWriter(rw);
                }                
            }
        }        
        return this.viewState;
    }
    
    private ResponseWriter ajaxResponseWriter = null;
    
    public ResponseWriter getResponseWriter() throws IOException {
        if (null == ajaxResponseWriter) {
            ajaxResponseWriter = 
                    AsyncResponse.getInstance().createAjaxResponseWriter(FacesContext.getCurrentInstance());
        }
        return ajaxResponseWriter;
    }
    
    private List<String> populateListFromHeader(String headerName) {
        String param = null;
        String [] pcs = null;
        Map requestMap = FacesContext.getCurrentInstance().
                getExternalContext().getRequestHeaderMap();
        List<String> result = null;
        // If there is no subtrees request header
        result = new ArrayList<String>();

        if (!requestMap.containsKey(headerName)) {
            return result;
        }
        // If we have a processingContext Request Parameter
        param = 
            requestMap.get(headerName).toString();
        if (null != param && param.equalsIgnoreCase("none")) {
            return result;
        }
        if (null != (pcs = param.split(",[ \t]*"))) {
            for (String cur : pcs) {
                cur = cur.trim();
                result.add(cur);
            }
        }

        return result;
    }

    private transient List<String> executeSubtrees;
    private transient List<String> renderSubtrees;
    
    public void clearSubtrees() {
        if (null != executeSubtrees) {
            executeSubtrees.clear();
        }
        executeSubtrees = null;
        if (null != renderSubtrees) {
            renderSubtrees.clear();
        }
        renderSubtrees = null;
    }

    public List<String> getExecuteSubtrees() {
        
        if (null != executeSubtrees) {
            return executeSubtrees;
        }
        executeSubtrees = populateListFromHeader(EXECUTE_HEADER);
        return this.executeSubtrees;
    }

    public void setExecuteSubtrees(List<String> executeSubtrees) {

        this.executeSubtrees = executeSubtrees;
    }    

    public List<String> getRenderSubtrees() {
        
        if (null != renderSubtrees) {
            return renderSubtrees;
        }
        renderSubtrees = populateListFromHeader(RENDER_HEADER);
        return this.renderSubtrees;
    }
    
    public boolean isRenderNone() {
        boolean result = false;
        String param = null;
        final String RENDER_NONE = FACES_PREFIX + "RenderNone";
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, String> requestHeaderMap = extContext.getRequestHeaderMap();
        Map<String, Object> requestMap = extContext.getRequestMap();

        if (requestMap.containsKey(RENDER_NONE)) {
            return true;
        }
        param = requestHeaderMap.get(RENDER_HEADER);
        result = null != param && param.equalsIgnoreCase("none");
        if (result) {
            requestMap.put(RENDER_NONE, Boolean.TRUE);
        }
        
        return result;
    }
    
    public boolean isRenderAll() {
        boolean result = false;
        final String RENDER_ALL = FACES_PREFIX + "RenderAll";
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> requestMap = extContext.getRequestMap();

        if (requestMap.containsKey(RENDER_ALL)) {
            return true;
        }
        result = isAjaxRequest() && !isRenderNone() && getRenderSubtrees().isEmpty();
        if (result) {
            requestMap.put(RENDER_ALL, Boolean.TRUE);
        }
        
        return result;
    }
    

    public boolean isExecuteNone() {
        boolean result = false;
        String param = null;
        final String EXECUTE_NONE = FACES_PREFIX + "ExecuteNone";
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, String> requestHeaderMap = extContext.getRequestHeaderMap();
        Map<String, Object> requestMap = extContext.getRequestMap();

        if (requestMap.containsKey(EXECUTE_NONE)) {
            return true;
        }
        param = requestHeaderMap.get(EXECUTE_HEADER);
        result = null != param && param.equalsIgnoreCase("none");
        if (result) {
            requestMap.put(EXECUTE_NONE, Boolean.TRUE);
        }
        
        return result;
    }

    public void setRenderSubtrees(List<String> renderSubtrees) {

        this.renderSubtrees = renderSubtrees;
    }
    
    public PhaseId getLastPhaseId(FacesContext context) {
        PhaseId result = PhaseId.RENDER_RESPONSE;
        Map<String,String> headerMap = context.getExternalContext().getRequestHeaderMap();
        if (headerMap.containsKey(EXECUTE_HEADER)) {
            result = PhaseId.INVOKE_APPLICATION;
        }
        
        return result;
    }
    
    public static void addToRenderList(UIComponent component) {
        AsyncResponse async = AsyncResponse.getInstance();
        FacesContext context = FacesContext.getCurrentInstance();
        List<String> list = async.getRenderSubtrees();
        list.add(component.getClientId(context));
    }
    
    private ResponseWriter createAjaxResponseWriter(FacesContext context) {
        // set up the ResponseWriter
        ResponseWriter responseWriter = null;
        RenderKitFactory renderFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit =
                renderFactory.getRenderKit(context,
                context.getViewRoot().getRenderKitId());
        Writer out = null;
        Object response = (null != origResponse) ? origResponse :
            context.getExternalContext().getResponse();
        try {
            Method getWriter =
                    response.getClass().getMethod("getWriter",
                    (Class []) null);
            if (null != getWriter) {
                out = (Writer) getWriter.invoke(response);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        if (null != out) {
            responseWriter =
                    renderKit.createResponseWriter(out,
                    "text/xml",
                    context.getExternalContext().getRequestCharacterEncoding());
        }
        return responseWriter;
    }
    
    /**
     * <p>Return <code>true</code> if and only if the request headers include
     * an entry for {@link #PARTIAL_HEADER}.
     */
    
    public static boolean isAjaxRequest() {
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> requestMap = ext.getRequestMap();
        final String ajaxFlag = FACES_PREFIX + "IsAjax";
        if (requestMap.containsKey(ajaxFlag)) {
            return true;
        }
        
        Map<String, String> p = ext.getRequestHeaderMap();
        boolean result = false;
        result = p.containsKey(PARTIAL_HEADER);
        if (result) {
            requestMap.put(ajaxFlag, Boolean.TRUE);
        }
        return result;
    }
    
    private Object origResponse = null;
    
    /**
     * <p>Called from {@link PartialTraversalLifecycle#render}, this method replaces
     * the <code>ResponseWriter</code> and <code>ResponseStream</code> with 
     * no-op instances.</p>
     */
    
    public void installNoOpResponse(FacesContext context) {
        origResponse = context.getExternalContext().getResponse();
        context.getExternalContext().setResponse(getNoOpResponse(origResponse));
    }
    
    /**
     * <p>Called from {@link PartialTraversalLifecycle#render}, this method replaces
     * the <code>ResponseWriter</code> and <code>ResponseStream</code> with 
     * their original instances.</p>
     */
    
    public void removeNoOpResponse(FacesContext context) {
        if (null != origResponse) {
            context.getExternalContext().setResponse(origResponse);
            origResponse = null;
        }
    }
    
    private Object getNoOpResponse(Object orig) {
        Object result = null;
        if (orig instanceof HttpServletResponse) {
            result = new NoOpResponseWrapper((HttpServletResponse)orig);
        }
        else {
            try {
                Method getPortletOutputStream =
                        orig.getClass().getMethod("getPortletOutputStream",
                        (Class []) null);
                if (null == getPortletOutputStream) {
                    throw new FacesException("Response is not a portlet or servlet");
                }
            } catch (NoSuchMethodException ex) {
                throw new FacesException("Response is not a portlet or servlet",ex);
            } catch (IllegalArgumentException ex) {
                throw new FacesException("Response is not a portlet or servlet", ex);
            } catch (SecurityException ex) {
                throw new FacesException("Response is not a portlet or servlet", ex);
            } 
            // PENDING(edburns): support portlet
            throw new UnsupportedOperationException();
        }
        return result;
    }
    
    public static final String FACES_PREFIX = "com.sun.faces.avatar.";
    public static final String VIEW_ROOT_ID = FACES_PREFIX + "ViewRoot";
    public static final String PARTIAL_HEADER= FACES_PREFIX + "partial";
    public static final String EXECUTE_HEADER = FACES_PREFIX + "execute";
    public static final String RENDER_HEADER= FACES_PREFIX + "render";
    public static final String METHOD_NAME_HEADER= FACES_PREFIX + "methodname";
    public static final String FACES_EVENT_HEADER= FACES_PREFIX + "facesevent";
    public static final String XJSON_HEADER= "X-JSON";
    
    public static final String FACES_EVENT_CONTEXT_PARAM = "com.sun.faces.extensions.avatar.FacesEvents";
    
     private static class StateCapture extends ResponseWriterWrapper {
        
        protected final ResponseWriter orig;
        private Object state;
        private FastWriter fw;
        
        public StateCapture(ResponseWriter orig, FastWriter fw) {
            this.orig = orig;
            this.fw = fw;
        }

        protected ResponseWriter getWrapped() {
            return this.orig;
        }

        public void writeAttribute(String name, Object value, String property) throws IOException {
            if ("value".equals(name)) {
                this.state = value;
            }
        }
        
        public String getState() {
            String buf = null;
            int i,j;
            if (null == this.state && null != (buf = fw.toString())) {
                buf = buf.toLowerCase(Locale.US);
                if (-1 != (i = buf.indexOf(ResponseStateManager.VIEW_STATE_PARAM.toLowerCase()))) {
                    if (-1 != (i = buf.lastIndexOf("<", i))) {
                        if (-1 != (i = buf.indexOf("value", i))) {
                            if (-1 != (i = buf.indexOf("\"",i))) {
                                if (-1 != (j = buf.indexOf("\"", ++i))) {
                                    state = buf.substring(i, j);
                                }
                            }
                        }
                    }
                }
            }
            return this.state != null ? this.state.toString() : "";
        }
        
    }

    private static class NoOpResponseWrapper extends HttpServletResponseWrapper {
        public NoOpResponseWrapper(HttpServletResponse orig) {
            super(orig);
        }

        public int getBufferSize() {
            return 0;
        }

        public void flushBuffer() throws IOException {
        }
        
        private ServletOutputStream noOpServletOutputStream = null;

        public ServletOutputStream getOutputStream() throws IOException {
            if (null == noOpServletOutputStream) {
                noOpServletOutputStream = new ServletOutputStream() {
                    public void write(int c) throws IOException {
                    }
                };
            }
            return noOpServletOutputStream;
        }
        
        private PrintWriter noOpPrintWriter = null;

        public PrintWriter getWriter() throws IOException {
            if (null == noOpPrintWriter) {
                noOpPrintWriter = new PrintWriter(new Writer() {
                    public void write(char[] cbuf, int off, int len) {}
                    public void flush() {}
                    public void close() {}
                });
            }
            return noOpPrintWriter;
        }

        public void reset() {
        }

        public void resetBuffer() {
        }
        
    }
    
    
}
