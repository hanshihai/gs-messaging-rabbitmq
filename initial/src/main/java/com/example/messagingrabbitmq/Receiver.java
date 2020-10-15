package com.example.messagingrabbitmq;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(Object message) {
		if(null != message) {
			if(message instanceof byte[]){
				String str = new String(((byte[])message), Charset.forName("UTF-8"));
				System.out.println(str);
			}else{
				System.out.println("Received unknown type < " + message.getClass().getName() + ">");
			}
		}else{
			System.out.println("Received < null >");
		}
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}
