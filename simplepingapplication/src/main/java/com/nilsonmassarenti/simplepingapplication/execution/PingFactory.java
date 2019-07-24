package com.nilsonmassarenti.simplepingapplication.execution;

import java.util.Properties;

public class PingFactory implements AbstractFactory<PingExecution> {

	@Override
	public PingExecution create(String os, Properties prop, String host) {
		if (os.toLowerCase().contains("mac")) {
			return new OSXPingExecution(prop, host);
		} else if (os.toLowerCase().contains("win")) {
			return new WinPingExecution();
		} else if (os.toLowerCase().contains("linux")) {
			return new LinuxPingExecution();
		} else {
			return null;
		}
	}



}
