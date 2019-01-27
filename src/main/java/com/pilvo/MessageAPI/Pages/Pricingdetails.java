package com.pilvo.MessageAPI.Pages;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import com.pilvo.MessageAPI.component.property;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Pricingdetails {

	static property prop = new property();
	public static String getPricingdetails(String auth_id,String auth_token) throws IOException
	{
		Response responsephonenumber = null;
		try {
			String OriginalPricing_url = prop.getData("OriginalPricing");
			String country = prop.getData("USA");
			OriginalPricing_url = OriginalPricing_url.replaceAll("id", auth_id);

			responsephonenumber = 
					given()
					.auth().basic(auth_id,auth_token)
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.when()
					.get(OriginalPricing_url+country);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Did Not got pricing details response");
		}

		return responsephonenumber.asString();



	}

}
