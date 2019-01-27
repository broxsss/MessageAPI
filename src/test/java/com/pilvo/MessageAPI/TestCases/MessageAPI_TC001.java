package com.pilvo.MessageAPI.TestCases;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pilvo.MessageAPI.Pages.Accounts;
import com.pilvo.MessageAPI.Pages.MessageSMS;
import com.pilvo.MessageAPI.Pages.PhoneNumbers;
import com.pilvo.MessageAPI.Pages.Pricingdetails;
import com.pilvo.MessageAPI.component.property;

public class MessageAPI_TC001 {
	
	Accounts account = new Accounts();
	static property prop = new property();
	HashMap<Integer,String> hmap = new HashMap<Integer,String>();
	
	
	@Test
	public void TC001() throws IOException
	{
		String auth_id = prop.getData("auth_id");
		String auth_token = prop.getData("auth_token");
		
		//Accounts details before sending message
		double cash_creditsbeforesms = (new JsonParser()).parse(Accounts.getAccountDetails(auth_id,auth_token)).getAsJsonObject().get("cash_credits").getAsDouble();
		System.out.println("cash_creditsbeforesms : "+cash_creditsbeforesms);
		
		
		//Taking any two Numbers from phone number api
		JsonObject jsphonenum = (new JsonParser()).parse(PhoneNumbers.getPhonenumbers(auth_id,auth_token)).getAsJsonObject();
		int size = jsphonenum.get("meta").getAsJsonObject().get("total_count").getAsInt();
		for(int i=0;i<size;i++)
		{
			String number = jsphonenum.get("objects").getAsJsonArray().get(i).getAsJsonObject().get("number").getAsString();
			hmap.put(i, number);
		}
		System.out.println("Phone number 1:"+hmap.get(0));
		System.out.println("Phone number 2:"+hmap.get(1));
		
		//Post Test specific data as string and get the uuid after sending msg
		String message = "{\"src\": \""+hmap.get(0)+"\",\"dst\": \""+hmap.get(1)+"\", \"text\": \"How U doin?\"}";
		String msguuid = (new JsonParser()).parse(MessageSMS.sendMessage(auth_id,auth_token,message)).getAsJsonObject().get("message_uuid").getAsJsonArray().getAsString();
		System.out.println("message uuid :"+msguuid);
		
		
		//Pricing for above send message
		double perSMS_Rate = (new JsonParser()).parse(MessageSMS.getMessagedetails(auth_id,auth_token,msguuid)).getAsJsonObject().get("total_rate").getAsDouble();
		System.out.println("price deducted after sending message:"+perSMS_Rate);
		
		//Expected or original Price for sending message
		double pricing_rate = (new JsonParser()).parse(Pricingdetails.getPricingdetails(auth_id,auth_token)).getAsJsonObject().get("message").getAsJsonObject().get("outbound").getAsJsonObject().get("rate").getAsDouble();
		System.out.println("Pricing rate for outbound in US: "+pricing_rate);
		
		DecimalFormat df3 = new DecimalFormat("#.####");
		String c = df3.format(perSMS_Rate);
		perSMS_Rate = Double.parseDouble(c);
		
		DecimalFormat df4 = new DecimalFormat("#.####");
		String d = df4.format(perSMS_Rate);
		pricing_rate = Double.parseDouble(d);
		
		//Check the actual price for sending message is equal to expected price for sending msg
		SoftAssert sf = new SoftAssert();
		sf.assertEquals(perSMS_Rate, pricing_rate);
		
		if(Double.compare(perSMS_Rate, pricing_rate) == 0)
		{
			System.out.println("yeah Both rate are equal "+"perSMS_Rate: "+perSMS_Rate+"     ##########    pricing_rate:"+pricing_rate);
		}
		else
		{
			System.out.println("They are not equal "+"perSMS_Rate: "+perSMS_Rate+"     ##########     pricing_rate:"+pricing_rate);
		}
		
		//Accounts details after sending message
		double cash_creditsaftersms = (new JsonParser()).parse(Accounts.getAccountDetails(auth_id,auth_token)).getAsJsonObject().get("cash_credits").getAsDouble();
		System.out.println("cash_creditsaftersms : "+cash_creditsaftersms);
		
		double Actualamtwithsmsprice =  	cash_creditsaftersms+perSMS_Rate;
		DecimalFormat df1 = new DecimalFormat("#.#####");
		String a = df1.format(Actualamtwithsmsprice);
		Actualamtwithsmsprice = Double.parseDouble(a);
		System.out.println("Actualamtwithsmsprice : "+cash_creditsaftersms+"+"+perSMS_Rate+" = "+Actualamtwithsmsprice);
		
		
		double Expectedamtwithsmsprice =  cash_creditsbeforesms-perSMS_Rate;
		DecimalFormat df2 = new DecimalFormat("#.#####");
		String b = df2.format(Expectedamtwithsmsprice);
		Expectedamtwithsmsprice = Double.parseDouble(b);
		
		//Check the sms sending amt is deducted or not from account credit
		Assert.assertEquals(Actualamtwithsmsprice, cash_creditsbeforesms);
		
		if(Double.compare(Actualamtwithsmsprice, cash_creditsbeforesms) == 0)
		{
			System.out.println("Yes the amoumt is deducted correctly:");
			System.out.println("Actual amount present in account after deduction : "+cash_creditsaftersms +"     ##########    Expected amount to be left in account after decuction : "+Expectedamtwithsmsprice);
		}
		else
		{
			System.out.println("The amoumt is not deducted correctly:");
			System.out.println("Actual amount present in account after deduction : "+cash_creditsaftersms +"     ##########    Expected amount to be left in account after decuction : "+Expectedamtwithsmsprice);
		}
		
		
	}
}
