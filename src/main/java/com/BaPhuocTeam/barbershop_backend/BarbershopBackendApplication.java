package com.BaPhuocTeam.barbershop_backend;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BarbershopBackendApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

		//------ Run Local ---------------//
		// Dotenv dotenv = Dotenv.load();

		// System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
		// System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		// System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
		// System.setProperty("SERVER.PORT", dotenv.get("SERVER.PORT"));

		// System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
		// System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
		// System.setProperty("CLOUDINARY_NAME", dotenv.get("CLOUDINARY_NAME"));

		// System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
		// System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		// System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		// System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));

		// System.setProperty("GOOGLE_AUTHORIZATION_URI", dotenv.get("GOOGLE_AUTHORIZATION_URI"));
		// System.setProperty("GOOGLE_TOKEN_URI", dotenv.get("GOOGLE_TOKEN_URI"));
		// System.setProperty("GOOGLE_USER_INFO_URI", dotenv.get("GOOGLE_USER_INFO_URI"));
		// System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		// System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		// System.setProperty("GOOGLE_REDIRECT_URI", dotenv.get("GOOGLE_REDIRECT_URI"));
		// System.setProperty("GOOGLE_SCOPE", dotenv.get("GOOGLE_SCOPE"));

		// System.setProperty("PAYPAL_CLIENT_ID", dotenv.get("PAYPAL_CLIENT_ID"));
		// System.setProperty("PAYPAL_CLIENT_SECRET", dotenv.get("PAYPAL_CLIENT_SECRET"));
		// System.setProperty("PAYPAL_MODE", dotenv.get("PAYPAL_MODE"));

		// System.setProperty("VNP_TMNCODE", dotenv.get("VNP_TMNCODE"));
		// System.setProperty("VNP_HASHSECRET", dotenv.get("VNP_HASHSECRET"));
		// System.setProperty("VNP_URL", dotenv.get("VNP_URL"));
		// System.setProperty("VNP_RETURN_URL", dotenv.get("VNP_RETURN_URL"));
		//--------------------------------------------------------------------------//
		

		//------------ Run Render Deploy -------------------//
		System.setProperty("DATABASE_URL", System.getenv("DATABASE_URL"));
		System.setProperty("DATABASE_USERNAME", System.getenv("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", System.getenv("DATABASE_PASSWORD"));
		System.setProperty("SERVER.PORT", System.getenv("SERVER.PORT"));

		System.setProperty("CLOUDINARY_API_KEY", System.getenv("CLOUDINARY_API_KEY"));
		System.setProperty("CLOUDINARY_API_SECRET", System.getenv("CLOUDINARY_API_SECRET"));
		System.setProperty("CLOUDINARY_NAME", System.getenv("CLOUDINARY_NAME"));

		System.setProperty("MAIL_HOST", System.getenv("MAIL_HOST"));
		System.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD"));
		System.setProperty("MAIL_PORT", System.getenv("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", System.getenv("MAIL_USERNAME"));

		System.setProperty("GOOGLE_AUTHORIZATION_URI", System.getenv("GOOGLE_AUTHORIZATION_URI"));
		System.setProperty("GOOGLE_TOKEN_URI", System.getenv("GOOGLE_TOKEN_URI"));
		System.setProperty("GOOGLE_USER_INFO_URI", System.getenv("GOOGLE_USER_INFO_URI"));
		System.setProperty("GOOGLE_CLIENT_ID", System.getenv("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", System.getenv("GOOGLE_CLIENT_SECRET"));
		System.setProperty("GOOGLE_REDIRECT_URI", System.getenv("GOOGLE_REDIRECT_URI"));
		System.setProperty("GOOGLE_SCOPE", System.getenv("GOOGLE_SCOPE"));

		System.setProperty("PAYPAL_CLIENT_ID", System.getenv("PAYPAL_CLIENT_ID"));
		System.setProperty("PAYPAL_CLIENT_SECRET", System.getenv("PAYPAL_CLIENT_SECRET"));
		System.setProperty("PAYPAL_MODE", System.getenv("PAYPAL_MODE"));

		System.setProperty("VNP_TMNCODE", System.getenv("VNP_TMNCODE"));
		System.setProperty("VNP_HASHSECRET", System.getenv("VNP_HASHSECRET"));
		System.setProperty("VNP_URL", System.getenv("VNP_URL"));
		System.setProperty("VNP_RETURN_URL", System.getenv("VNP_RETURN_URL"));

		SpringApplication.run(BarbershopBackendApplication.class, args);
	}

}
