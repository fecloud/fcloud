/**
 * @(#) ArduinoServer.java Created on 2014-3-19
 *
 * Copyright © 2013 深圳企业云科技有限公司  版权所有
 */
package com.fcloud.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * The class <code>ArduinoServer</code>
 * 
 * @author Feng OuYang
 * @version 1.0
 */
public class ArduinoServer extends Thread implements ArduinoSocket {

	Logger logger = Logger.getLogger(ArduinoServer.class);

	private int port;

	private List<ArduinoSocketWorker> workers;

	/**
	 * @param port
	 */
	public ArduinoServer(int port) {
		super();
		this.port = port;
		workers = new LinkedList<ArduinoServer.ArduinoSocketWorker>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		setName("ArduinoServer");
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			logger.debug("server port:" + port);
			Socket client = null;
			ArduinoSocketWorker socketWorker;
			while (true) {
				logger.debug("workers.isEmpty():" + workers.isEmpty());
				if (workers.isEmpty()) {
					logger.debug("server accept ");
					client = socket.accept();
					socketWorker = new ArduinoSocketWorker(client);
					workers.add(socketWorker);
					//new Thread(socketWorker).start();
					socketWorker.run();
				}
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fcloud.socket.ArduinoSocket#onOpen()
	 */
	@Override
	public void onOpen() {
		logger.debug("onOpen");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fcloud.socket.ArduinoSocket#onClose()
	 */
	@Override
	public void onClose() {
		logger.debug("onClose");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fcloud.socket.ArduinoSocket#onMessage()
	 */
	@Override
	public void onMessage(String message) {
		logger.debug("onMessage message:" + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fcloud.socket.ArduinoSocket#onError(java.lang.Exception)
	 */
	@Override
	public void onError(Exception exception) {
		logger.error("onError client disconned");
	}

	public boolean send(String messsage) {
		if (workers.isEmpty()) {
			logger.debug("not client conned");
		} else {
			return workers.get(0).send(messsage);
		}
		return false;
	}

	public boolean sendln(String line) {
		return send(line + "\n");
	}

	public boolean flush() {
		if (workers.isEmpty()) {
			logger.debug("not client conned");
		} else {
			return workers.get(0).flush();
		}
		return false;
	}

	private class ArduinoSocketWorker implements Runnable {

		private Socket socket;

		/**
		 * @param socket
		 */
		public ArduinoSocketWorker(Socket socket) {
			super();
			this.socket = socket;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			if (null != socket) {
				onOpen();
			}

			try {
				final BufferedReader reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream(), "UTF-8"));
				String line = null;
				while (null != (line = reader.readLine())) {
					onMessage(line);
				}

			} catch (IOException e) {
				onError(e);
			} finally {
				onClose();
			}
			workers.remove(this);
			socket = null;
		}

		boolean send(String messsage) {
			if (socket != null) {
				OutputStream out;
				try {
					out = socket.getOutputStream();
					out.write(messsage.getBytes("UTF-8"));
				} catch (IOException e) {
					onError(e);
					workers.remove(this);
					socket = null;
					onClose();
				}
				workers.remove(this);
				socket = null;
			}
			return false;
		}

		boolean flush() {
			if (socket != null) {
				try {
					socket.getOutputStream().flush();
				} catch (IOException e) {
					onError(e);
					workers.remove(this);
					socket = null;
					onClose();
					return false;
				}
				return true;

			}
			return false;
		}

	}

	public static void main(String[] args) {
		new ArduinoServer(80).start();
	}

}
