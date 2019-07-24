package com.nilsonmassarenti.simplepingapplication.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.nilsonmassarenti.simplepingapplication.execution.PingExecution;
import com.nilsonmassarenti.simplepingapplication.execution.PingFactory;
import com.nilsonmassarenti.simplepingapplication.model.PingResult;
import com.nilsonmassarenti.simplepingapplication.util.ApplicationProperties;

public final class PingController {

	private static PingController INSTANCE;
	private Properties properties;
	private Map<String, PingResult> results;
	private Map<String, Boolean> hosts;
	private Integer waitTime; 
	
	private static String OS = System.getProperty("os.name");

	private PingController() {
		this.properties = ApplicationProperties.getProperties();
		this.results = new ConcurrentHashMap<String, PingResult>();
		this.hosts = new ConcurrentHashMap<String, Boolean>();
		this.waitTime = Integer.parseInt(this.properties.getProperty("prop.ping.sleep_seconds")) * 1000;


	}

	public synchronized static PingController getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PingController();
		}
		return INSTANCE;
	}

	public void processPingVerification() throws InterruptedException, ExecutionException {
		while(true) {
			System.out.println(LocalDateTime.now() + " - Start execution pings");
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			List<Callable<PingResult>> callableTasks = new ArrayList<>();
			
			for (Map.Entry<String, Boolean> entry : hosts.entrySet()) {
				Callable<PingResult> task = () -> {
					PingExecution pe = new PingFactory().create(OS, properties, entry.getKey());
					String icmp = pe.icmpPing();
					String trace = pe.trace();
					return new PingResult(entry.getKey(), icmp, null, trace);

				};
				callableTasks.add(task);
			}
			executorService.invokeAll(callableTasks)
		    .stream()
		    .forEach(future -> {
		    	try {
					this.results.put(future.get().getHost(), future.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
		    });
			executorService.shutdown();
			while (!executorService.isShutdown()) {
			}
			
			System.out.println(LocalDateTime.now() + " - Waiting new execution pings");
			Thread.sleep(this.waitTime);
		}
	}
	
	public JSONObject getLastResult(String host) {
		if (results.get(host) == null) {
			firstResult(host);
			addHost(host);
		}
		return new JSONObject(this.results.get(host));
		
	}
	
	private void firstResult(String host) {
		PingExecution pe = new PingFactory().create(OS, properties, host);
		String icmp = pe.icmpPing();
		String trace = pe.trace();
		this.results.put(host, new PingResult(host, icmp, null, trace));
	}
	
	private void addHost(String host) {
		this.hosts.put(host, true);
	}
}
