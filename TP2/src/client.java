import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.imageio.ImageIO;


import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class client {

	public static void main(String[] args) {
		Socket socket;
		InetAddress serverAddress;
		String serverAddressString;
		String login = null;
		String password = null;
		PrintWriter out = null;
		BufferedReader in = null;
		int port;
        Scanner keyboard = new Scanner (System.in);
        
        while(true){
	        System.out.println("Veuillez entrer l'adresse du serveur");
	        serverAddressString = keyboard.nextLine();
	        
			try{
				 serverAddress = InetAddress.getByName(serverAddressString); //test de format et assignation de l adresse
			     System.out.println("IP valide");
			     break;
			}
			catch(UnknownHostException ex){
			       System.out.println("IP non valide, reessayez");
			}
		}
        
        while (true){
        	System.out.println("Veuillez entrer le port d'ecoute");
   
        	try{
	        	port = keyboard.nextInt();
	        	if (port <= 5050 && port >= 5000)
	        	{
	        		System.out.println("port valide");
	        		break;
	        	}
	        	else System.out.println("port hors de l'intervalle 5000-5050, rï¿½essayez");
	        }
        	catch (InputMismatchException e)
        	{
        		System.out.println("format du port incorrect, rï¿½essayez");
        		keyboard.next();
        	}
        		
        }
        
        keyboard.nextLine();
        System.out.println("Veuillez entrer votre nom d utilisateur :");
        login = keyboard.nextLine();
   
        System.out.println("Veuillez entrer votre mot de passe :");
        password = keyboard.nextLine();
		
		
		try {
		
		     socket = new Socket(serverAddress, port);	
		     
		     in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 			 out = new PrintWriter(socket.getOutputStream(), true);
 			 
 			 String pseudomdp = login + ":" + password;
             out.println(pseudomdp);
             
             
             String messageRecu = in.readLine();
             if (messageRecu.equals("true")) {
            	 System.out.println("Vous etes connecte au service de traitement d image !");
            	 
            	 
            	 //Envoi de l image
            	 String nomFichier = "lassonde.jpg";
            	 BufferedImage image = ImageIO.read(new File("./src/" + nomFichier));
            	 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            	 ImageIO.write(image, "jpg", byteArrayOutputStream);
            	 byteArrayOutputStream.flush();
            	 int size = byteArrayOutputStream.size();
            	 out.println(size);
            	 byte tabImage[] = byteArrayOutputStream.toByteArray();
            	 socket.getOutputStream().write(tabImage);
            	 System.out.println("Image envoyee");
            	 
            	 //Reception de l image
            	 int sobelSize = Integer.parseInt(in.readLine());
 				 System.out.println("nombre de bits du fichier " + sobelSize);
            	 byte[] tabSobel = readExactly(socket.getInputStream(), sobelSize);
 				 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tabSobel);
 				 BufferedImage imageSobel = ImageIO.read(byteArrayInputStream);
 				 System.out.println("Image bien reçue");
 				 System.out.println("Entrez le nom sous lequel enregistrer l image Sobel");
 				 String nomSobel = keyboard.nextLine();
 				 ImageIO.write(imageSobel, "jpg", new File("./src/" + nomSobel + ".jpg"));
 				 System.out.println("Votre image convertie se trouve dans ./src/" + nomSobel + ".jpg");
           
            	 
             } else {
            	 System.out.println("Mauvaise combinaison de login/password, veuillez reessayer");
             }
            
	         socket.close();

		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] readExactly(InputStream input, int size) throws IOException
	{
	    byte[] data = new byte[size];
	    int index = 0;
	    while (index < size)
	    {
	        int bytesRead = input.read(data, index, size - index);
	        if (bytesRead < 0)
	        {
	            throw new IOException("Insufficient data in stream");
	        }
	        index += size;
	    }
	    return data;
	}

}
