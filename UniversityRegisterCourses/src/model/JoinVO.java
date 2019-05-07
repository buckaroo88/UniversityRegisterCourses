package model;

public class JoinVO {

	String id;
	String password;
	String name;
	
	public JoinVO() {
		super();
	}

	public JoinVO(String id) {
		super();
		this.id = id;
	}
	
	public JoinVO(String id, String password) {
		super();
		this.id = id;
		this.password = password;
	}

	public JoinVO(String id, String password, String name) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
