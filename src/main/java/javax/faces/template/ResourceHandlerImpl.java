package javax.faces.template;

import java.io.IOException;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;


public class ResourceHandlerImpl  extends ResourceHandler{

	
	private ResourceHandler wrapper;
	
	public ResourceHandlerImpl(ResourceHandler wrapper) {
		
		this.wrapper=wrapper;
	}
	
	@Override
	public Resource createResource(String arg0) {
		return wrapper.createResource(arg0);
	}

	@Override
	public Resource createResource(String resourceName, String library) {
		
		FacesContext context=FacesContext.getCurrentInstance();
		Map<String,Object> map=context.getExternalContext().getApplicationMap();
		Template template=(Template) map.get(TemplateManager.SELECTED_TEMPLATE);
		Resource resource=template.getResource(resourceName, library);
		if(resource!=null) return resource;
		return wrapper.createResource(resourceName, library);
	}

	@Override
	public Resource createResource(String arg0, String arg1, String arg2) {
		return wrapper.createResource(arg0, arg1, arg2);
	}

	@Override
	public String getRendererTypeForResourceName(String arg0) {
		return wrapper.getRendererTypeForResourceName(arg0);
	}

	@Override
	public void handleResourceRequest(FacesContext arg0) throws IOException {
		wrapper.handleResourceRequest(arg0);
		
	}

	@Override
	public boolean isResourceRequest(FacesContext arg0) {
		return wrapper.isResourceRequest(arg0);
	}

	@Override
	public boolean libraryExists(String arg0) {
		return wrapper.libraryExists(arg0);
	}

}