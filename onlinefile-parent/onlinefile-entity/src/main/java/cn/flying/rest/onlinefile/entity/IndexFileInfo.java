package cn.flying.rest.onlinefile.entity;

public class IndexFileInfo {

	private String _id;
	private String companyId;
	private String classId;
	private String filename;
	private String version;
	private String fileId;
	
	public IndexFileInfo(){
		
	}
	
	public IndexFileInfo(String _id, String companyId, String classId,
			String filename, String version, String fileId) {
		this._id = _id;
		this.companyId = companyId;
		this.classId = classId;
		this.filename = filename;
		this.version = version;
		this.fileId = fileId;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
}
