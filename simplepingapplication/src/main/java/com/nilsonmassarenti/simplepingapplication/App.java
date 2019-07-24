package com.nilsonmassarenti.simplepingapplication;

import java.util.concurrent.ExecutionException;

import com.nilsonmassarenti.simplepingapplication.controller.HttpServerController;
import com.nilsonmassarenti.simplepingapplication.controller.PingController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException, ExecutionException
    {
    	Runnable runnablePingController = () -> {
    	    try {
    	    	PingController pingController = PingController.getInstance();
    	        pingController.processPingVerification();
    	    }
    	    catch (InterruptedException e) {
    	        e.printStackTrace();
    	    } catch (ExecutionException e) {
				e.printStackTrace();
			}
    	};

    	Thread threadPingController = new Thread(runnablePingController);
    	threadPingController.start();
    	
    	Runnable runnableHttpServerController = () -> {
    	    HttpServerController httpServerController = new HttpServerController();
			httpServerController.start();
    	};

    	Thread threadHttpServerController = new Thread(runnableHttpServerController);
    	threadHttpServerController.start();
    	
    	
        
    }
}
