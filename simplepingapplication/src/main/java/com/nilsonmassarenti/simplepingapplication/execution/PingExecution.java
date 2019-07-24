package com.nilsonmassarenti.simplepingapplication.execution;

public interface PingExecution {
	
	String icmpPing();
	String tcpPing();
	String trace();

}
