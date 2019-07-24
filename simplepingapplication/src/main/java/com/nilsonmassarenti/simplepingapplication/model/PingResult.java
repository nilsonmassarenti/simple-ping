package com.nilsonmassarenti.simplepingapplication.model;

import org.json.JSONPropertyName;

public class PingResult {
	private String host;
	private String pingICMP;
	private String pingTCP;
	private String trace;
	
	public PingResult(String host, String pingICMP, String pingTCP, String trace) {
		super();
		this.host = host;
		this.pingICMP = pingICMP;
		this.pingTCP = pingTCP;
		this.trace = trace;
	}

	public String getHost() {
		return host;
	}
	@JSONPropertyName("icmp_ping")
	public String getPingICMP() {
		return pingICMP;
	}
	
	@JSONPropertyName("tcp_ping")
	public String getPingTCP() {
		return pingTCP;
	}

	@JSONPropertyName("trace")
	public String getTrace() {
		return trace;
	}
	
	
	
}
