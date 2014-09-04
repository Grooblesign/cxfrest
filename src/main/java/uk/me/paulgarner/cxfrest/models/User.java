package uk.me.paulgarner.cxfrest.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class User {
	private int id;
	private String name;
	private String password;
	private String wibble;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getWibble() {
		return wibble;
	}
	public void setWibble(String wibble) {
		this.wibble = wibble;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
