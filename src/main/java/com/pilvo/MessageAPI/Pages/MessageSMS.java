package com.pilvo.MessageAPI.Pages;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import com.pilvo.MessageAPI.component.property;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class MessageSMS {
	static property prop = new property();
	
	public static String sendMessage(String auth_id,String auth_token,String Body) throws IOException
	{
		String msg_url = prop.getData("SendMessage");
		msg_url = msg_url.replaceAll("id", auth_id);
		
		Response responsemessage = 
				given()
				.auth().basic(auth_id,auth_token)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(Body)
				.when()
				.post(msg_url);
		
		
		return responsemessage.asString();
	}
	
	public static String getMessagedetails(String auth_id,String auth_token,String msguuid) throws IOException
	{
		String getmsgdetails_url = prop.getData("GetMessage_details");
		getmsgdetails_url = getmsgdetails_url.replaceAll("id", auth_id);
		
		Response responsemsgdetails = 
				given()
				.auth().basic(auth_id ,auth_token)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get(getmsgdetails_url+msguuid+"/");
		
		return responsemsgdetails.asString();
	}

}
