package javax.faces.plugin;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;



@ApplicationScoped
@ManagedBean(eager=true)
public abstract class PluginManager<T extends Plugin>  {

	protected  final List<T> 	plugins;
	protected  PluginLoader<T>  loader;
	
	
	public PluginManager() {
		
		plugins=new ArrayList<T>();
		
	}
	
	public PluginManager(PluginLoader<T> loader) {
	
			this();
			this.loader=loader;							
	}

	@PostConstruct
	public void load()  {
		
		try {
			
			ServletContext context=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String realPath=context.getRealPath("/");
			Manage manage=getClass().getAnnotation(Manage.class);
			Folder pluginFolder=new Folder(realPath+File.separator+manage.folder());
			for(Folder folder : pluginFolder.getSubFolders()) {
				Document metadata=new Document(folder+File.separator+manage.metadata());
				if(metadata.exists()) {
					T plugin=load(metadata.getInputStream());
					if(plugin!=null) {
						plugin.setMetadata(metadata);
						add(plugin);
					}
				}
			} 
		}
		catch(Exception e) {
			
		}
	}
	
	public T load(InputStream stream) throws Exception{
		
			return loader.load(stream);
						
	}
	
	public void add(T plugin) {
		
			plugins.add(plugin);
			
	}
		
	public void remove(T plugin)  {
		
			plugins.remove(plugin);
	}
	
	public List<T> getPlugins() {
		
			return plugins;
	}

	public PluginLoader<T> getLoader() {
		return loader;
	}

	public void setLoader(PluginLoader<T> loader) {
		this.loader = loader;
	}

}