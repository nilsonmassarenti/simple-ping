package com.nilsonmassarenti.simplepingapplication.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

public class OSXPingExecution implements PingExecution  {

	private Properties prop;
	private String host;

	public OSXPingExecution(Properties prop, String host) {
		this.prop = prop;
		this.host = host;
	}

	@Override
	public String icmpPing() {
		String result = "";
		String command = prop.getProperty("prop.ping.icmp.os") + " " + prop.getProperty("prop.ping.icmp.amount") + " "
				+ this.host;
		try {
			Process p = Runtime.getRuntime().exec(command);
			try (BufferedReader buffer = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				result = buffer.lines().collect(Collectors.joining("\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String tcpPing() {
		return null;
	}

	@Override
	public String trace() {
		StringBuilder sb = new StringBuilder();
		String command = prop.getProperty("prop.ping.tracert.os") + " " + this.host;
		Integer timeout = 3;
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String result = "";
			while (((result = inputStream.readLine()) != null) && (timeout > 0)) {
				sb.append(result + "\n");
				if (result.contains("* * *")) {
					--timeout;
				} else if (result.contains("ret=-1")) {
					timeout = 0;
				} else {
					timeout = 3;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
