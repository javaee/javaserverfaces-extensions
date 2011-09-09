package javax.faces.plugin;




public abstract class Plugin  {

	protected String name="";
	protected String description="";
	protected String creationDate="";
	protected String version="1.0";
	protected String author="";
	protected String authorEmail="";
	protected String authorUrl="";
	protected String license="GPL";
	protected String copyright="";
	protected int index;
    protected Folder folder;
    
	
    
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String toString() {
		
		return name;		
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public String getAuthorUrl() {
		return authorUrl;
	}

	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}	
	
	public String getId() {
		return folder.getName();
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

}
