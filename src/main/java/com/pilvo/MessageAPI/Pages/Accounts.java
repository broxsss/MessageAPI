package com.pilvo.MessageAPI.Pages;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Properties;
import com.google.gson.JsonParser;
import com.pilvo.MessageAPI.component.property;

import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class Accounts {
	
	static property prop = new property();
	
	public static String getAccountDetails(String auth_id,String auth_token) throws IOException
	{
		Response responseAccountsbeforesms = null;
		try {
		String acc_url = prop.getData("account_details");
		acc_url = acc_url.replaceAll("id", auth_id);
		
		
		 responseAccountsbeforesms = 
				given()
				.auth().basic(auth_id,auth_token)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.get(acc_url);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Did Not got AccountDetails response");
			
		}
		return responseAccountsbeforesms.asString();

	}
	

}
