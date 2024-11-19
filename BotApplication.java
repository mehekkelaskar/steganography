package com.mehek.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
@SpringBootApplication
public class BotApplication {

	public static void main(String[] args) {

		SpringApplication.run(BotApplication.class, args);
	}
	@RestController
	@RequestMapping("/steganography")
	class SteganographyController {

		@PostMapping("/encrypt")
		public ResponseEntity<String> encrypt(
				@RequestParam("message") String message,
				@RequestParam("imagePath") String imagePath) {
			try {
				BufferedImage image = ImageIO.read(new File(imagePath));
				BufferedImage encodedImage = SteganographyUtil.encodeMessage(image, message);
				String outputImagePath = "output.png";
				SteganographyUtil.saveImage(encodedImage, outputImagePath);
				return ResponseEntity.ok("Message encoded and saved as 'output.png'");
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body("Error encoding message: " + e.getMessage());
			}
		}

		@PostMapping("/decrypt")
		public ResponseEntity<String> decrypt(@RequestParam("imagePath") String imagePath) {
			try {
				BufferedImage image = ImageIO.read(new File(imagePath));
				String decodedMessage = SteganographyUtil.decodeMessage(image);
				return ResponseEntity.ok("Decoded message: " + decodedMessage);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(500).body("Error decoding message: " + e.getMessage());
			}
		}
	}

}
