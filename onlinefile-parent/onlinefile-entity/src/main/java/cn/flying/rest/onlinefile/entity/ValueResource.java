package cn.flying.rest.onlinefile.entity;


public class ValueResource extends ValueText{
	private static final long serialVersionUID = 5L;
	public ValueResource(){
		super();
	}
	public ValueResource(String s){
		super(s);
	}	
	public TYPE getType() {
		return Value.TYPE.RESOURCE;
	}

}
