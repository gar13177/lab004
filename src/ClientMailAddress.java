
public class ClientMailAddress {
	
	public static final int MAX_USER_LENGTH = 64,
			MAX_DOMAIN_LENGTH = 64,
			MAX_ADDRESS_LENGTH = 256;
	
	private String user, domain, address, path;
	
	public ClientMailAddress (String address) {
		if ( address != null ) this.setAddress( address );
	}
	
	public ClientMailAddress () {}
	
	public void setPath( String path ){
		this.path = path;
	}
	
	public boolean setAddress ( String address ){
		if ( ! AddressValid(address) ) return false;
		
		this.address = address;
		String[] values = address.split("@",2);
		
		this.user = values[0];
		this.domain = values[1];
		this.path = String.format("server/%s/%s", domain.toLowerCase(), user.toLowerCase());
		return true;
	}
	
	public String getUser(){
		return this.user;
	}
	
	public String getDomain(){
		return this.domain;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public boolean equals( Object o ){
		ClientMailAddress c = (ClientMailAddress) o;
		return this.user.toUpperCase().equals(c.getUser().toUpperCase()) 
				&& this.domain.toUpperCase().equals(c.getDomain().toUpperCase())
				&& this.address.toUpperCase().equals(c.getAddress().toUpperCase());
	}
	
	
	public static boolean AddressValid( String address ){
		return ( address.length() > 2 && address.contains("@"));
	}

}
