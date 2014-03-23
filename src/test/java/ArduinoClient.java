import java.io.OutputStream;
import java.net.Socket;

/**
 * @(#) ArduinoClient.java Created on 2014-3-19
 *
 * Copyright © 2013 深圳企业云科技有限公司  版权所有
 */

/**
 * The class <code>ArduinoClient</code>
 * 
 * @author Feng OuYang
 * @version 1.0
 */
public class ArduinoClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		Socket socket = new Socket("localhost", 80);
		socket.setSoTimeout(1000);
		OutputStream out = socket.getOutputStream();
		System.out.println(System.currentTimeMillis()-start);
		while (true) {
			Thread.sleep(2000);
			out.write(new String("" + System.currentTimeMillis() + "\n").getBytes("UTF-8"));
			out.flush();
		}
	}

}
