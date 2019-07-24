package com.nilsonmassarenti.simplepingapplication.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

import org.json.JSONObject;

import com.nilsonmassarenti.simplepingapplication.util.ApplicationProperties;

public class HttpServerController {

	private Properties properties;
	private final String newLine = "\r\n";
	private int serverPort;
	private PingController pingController;

	public HttpServerController() {
		this.properties = ApplicationProperties.getProperties();
		this.serverPort = Integer.parseInt(this.properties.getProperty("prop.server.http.port"));
		pingController = PingController.getInstance();
	}

	public void start() {
		System.out.println("Starting Server");
		try {
			ServerSocket socket = new ServerSocket(this.serverPort);
			while (true) {
				Socket connection = socket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				OutputStream out = new BufferedOutputStream(connection.getOutputStream());
				PrintStream pout = new PrintStream(out);
				String request = in.readLine();
				if (request == null) {
					continue;
				}

				while (true) {
					String ignore = in.readLine();
					if (ignore == null || ignore.length() == 0) {
						break;
					}
				}

				StringBuilder payload = new StringBuilder();
				while (in.ready()) {
					payload.append((char) in.read());
				}

				if (!request.startsWith("POST ") || !(request.endsWith(" HTTP/1.0") || request.endsWith(" HTTP/1.1"))) {
					pout.print("HTTP/1.0 400 Bad Request" + newLine + newLine);
				} else {
					if (!checkRequest(request)) {
						pout.print("HTTP/1.0 404 Not Found" + newLine + newLine);
					} else {
						String host = loadPayload(payload.toString());
						if (host == null || host.trim().equals("")) {
							pout.print("HTTP/1.0 404 Not Found" + newLine + newLine);
						} else {
							JSONObject json = this.pingController.getLastResult(host);
							pout.print("HTTP/1.0 200 OK" + newLine + "Content-Type: application/json" + newLine + "Date: "
									+ new Date() + newLine + "Content-length: " + json.toString().length() + newLine + newLine
									+ json);
						}
					}
					
				}
				pout.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Boolean checkRequest(String request) {
		String[] params = request.split(" ");
		System.out.println(params[1]);
		switch (params[1]) {
		case "/reports":
			return true;
		default:
			return false;
		}
	}
	
	private String loadPayload(String payload) {
		JSONObject json = new JSONObject(payload);
		try {
			return json.getString("host");
		} catch (Exception e) {
			return null;
		}
	}

}
