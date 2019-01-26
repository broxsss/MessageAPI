package com.pilvo.MessageAPI.Pages;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import com.pilvo.MessageAPI.component.property;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PhoneNumbers {
	
	static property prop = new property();
	public static String getPhonenumbers(String auth_id,String auth_token) throws IOException
	{
		String phoneno_url = prop.getData("Numbers");
		phoneno_url = phoneno_url.replaceAll("id", auth_id);
		
		Response responsephonenumber = 
				given()
				.auth().basic(auth_id,auth_token)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get(phoneno_url);
		
		return responsephonenumber.asString();
		
		
	
	}

}
