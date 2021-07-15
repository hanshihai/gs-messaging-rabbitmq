package com.example.messagingrabbitmq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

	@Autowired(required = false)
	private MongoTemplate mongoTemplate;

	@Value("${spring.mongodb.enable}") boolean enableSave;
	@Value("${spring.mongodb.collection}") String collection;

	private CountDownLatch latch = new CountDownLatch(1);

	public void saveMessageIntoMongo(String message) {
		if(null == message || message.isEmpty()) {
			return;
		}
		try{
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
			HashMap<String,Object> map = mapper.readValue(message, typeRef);
			DBObject dbObject = new BasicDBObject(map);
			mongoTemplate.save(dbObject, collection);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveMessage(Object message) {
		if(null != message) {
			if(message instanceof byte[]){
				String str = new String(((byte[])message), Charset.forName("UTF-8"));
				System.out.println(str);
				if(enableSave){
					this.saveMessageIntoMongo(str);
				}
			}else{
				System.out.println("Received unknown type < " + message.getClass().getName() + ">");
			}
		}else{
			System.out.println("Received < null >");
		}
		//latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}
