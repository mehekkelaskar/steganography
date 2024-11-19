package com.mehek.bot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
    public class SteganographyUtil {

        public static BufferedImage encodeMessage(BufferedImage image , String message){
            int width= image.getWidth();//no of pixel in column
            int height= image.getHeight();//no of pixel in rows
            int messageIndex=0;
            StringBuilder binaryMessage =new StringBuilder();
            for(char c:message.toCharArray()){
                binaryMessage.append(String.format("%8s",Integer.toBinaryString(c)).replace(' ','0'));//each character must be of 1byte ie 8 bit
            }
            binaryMessage.append("00000011");//end of the string ir ETX
            outerLoop:
            for(int y=0;y<height;y++){
                for(int x=0;x<width;x++){
                    int rgb=image.getRGB(x,y);//get the pixel at location x,y
                    int red =(rgb>>16)&0xFF;
                    int green=(rgb>>8)&0xFF;
                    int blue=rgb&0xFF;
                    if(messageIndex<binaryMessage.length()){
                        blue=(blue&0xFE)|(binaryMessage.charAt(messageIndex)-'0');// set the lsb to 0 then add the bit to pixel ie modify the blue component of pixel
                        messageIndex++;
                    }
                    int modifiesRGB=(red<<16)|(green<<8)|blue;
                    image.setRGB(x,y,modifiesRGB);
                    if (messageIndex >= binaryMessage.length()) {
                        break outerLoop;
                    }
                }
            } return image;
        }
        public static void saveImage(BufferedImage image,String outputPath) {
            try {
                ImageIO.write(image, "png", new File(outputPath));
                System.out.println("Image saved with the encoded message");
            } catch (Exception e) {
                System.err.println("Error saving image:"+e.getMessage());

            }


        }
        public static String decodeMessage(BufferedImage image) {
            int width = image.getWidth();
            int height=image.getHeight();
            StringBuilder binaryMessage=new StringBuilder();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int blue = rgb & 0xFF;
                    binaryMessage.append(blue & 1); // Extract the least significant bit
                }
            }
            StringBuilder message=new StringBuilder();
            for (int i = 0; i + 8 <= binaryMessage.length(); i += 8) {
                String byteString = binaryMessage.substring(i, i + 8);
                char character = (char) Integer.parseInt(byteString, 2);
                if (character == 3) break; // End of Text marker
                message.append(character);
            }

            return message.toString();
        }
    }


