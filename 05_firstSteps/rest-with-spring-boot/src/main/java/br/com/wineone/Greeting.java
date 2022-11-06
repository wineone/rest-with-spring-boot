package br.com.wineone;

public class Greeting {
	private final long id;
	private final String convite;

	public Greeting(long id, String convite) {
		super();
		this.id = id;
		this.convite = convite;
	}
	public long getId() {
		return id;
	}
	public String getConvite() {
		return convite;
	}

}
