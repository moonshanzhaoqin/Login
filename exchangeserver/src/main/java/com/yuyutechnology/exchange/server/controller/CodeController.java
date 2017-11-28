package com.yuyutechnology.exchange.server.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.server.controller.request.DecryptRequest;
import com.yuyutechnology.exchange.server.controller.response.EncryptResponse;
import com.yuyutechnology.exchange.util.AESCipher;

//@ApiIgnore
@Controller
@RequestMapping(value = "/code", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
		MediaType.APPLICATION_JSON_VALUE })
public class CodeController {

	@RequestMapping(value = "/key/{key}/deCode", method = { RequestMethod.POST })
	@ResponseBody
	public String deCode(@RequestBody DecryptRequest message, @PathVariable String key) {
		try {
			return AESCipher.decryptAES(message.getContent(), key);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/key/{key}/enCode", method = { RequestMethod.POST })
	@ResponseBody
	public EncryptResponse enCode(@RequestBody String message, @PathVariable String key) {
		EncryptResponse encryptResponse = new EncryptResponse();
		try {
			encryptResponse.setContent(AESCipher.encryptAES(message, key));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			encryptResponse.setContent(e.getMessage());
		}
		return encryptResponse;
	}

//	@RequestMapping(value = "/testCode", method = { RequestMethod.POST })
//	@ResponseBody
//	public Message testCode(@RequestBody Message message) {
//		return message;
//	}

	public static class Message {
		private String name;
		private int age;
		private double double1;
		private Double double2;

		public Message() {
			super();
		}

		public Message(String name, int age, double double1, Double double2) {
			super();
			this.name = name;
			this.age = age;
			this.double1 = double1;
			this.double2 = double2;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public double getDouble1() {
			return double1;
		}

		public void setDouble1(double double1) {
			this.double1 = double1;
		}

		public Double getDouble2() {
			return double2;
		}

		public void setDouble2(Double double2) {
			this.double2 = double2;
		}

		@Override
		public String toString() {
			return "Message [name=" + name + ", age=" + age + "]";
		}
	}
}
