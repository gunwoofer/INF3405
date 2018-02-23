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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		int port;
        Scanner keyboard = new Scanner (System.in);
        
        
        // ADRESSE DU SERVEUR
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

        // PORT DU SERVEUR
        while (true){
        	System.out.println("Veuillez entrer le port d'ecoute");
   
        	try{
	        	port = keyboard.nextInt();
	        	if (port <= 5050 && port >= 5000)
	        	{
	        		System.out.println("port valide");
	        		break;
	        	}
	        	else System.out.println("port hors de l'intervalle 5000-5050, reessayez");
	        }
        	catch (InputMismatchException e)
        	{
        		System.out.println("format du port incorrect, reessayez");
        		keyboard.next();
        	}
        		
        }
        
        // CREDENTIALS
        keyboard.nextLine();
        System.out.println("Veuillez entrer votre nom d utilisateur :");
        login = keyboard.nextLine();
        System.out.println("Veuillez entrer votre mot de passe :");
        password = keyboard.nextLine();
		
        // CONNEXION ET IDENTIFICATION
		try {
		     socket = new Socket(serverAddress, port);	
		     out = new ObjectOutputStream(socket.getOutputStream()); 		    
 			 String pseudomdp = login + ":" + password;
             out.writeObject(pseudomdp);
             String messageRecu = null;
		     in = new ObjectInputStream(socket.getInputStream());

			try {
				messageRecu = in.readObject().toString();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
             if (messageRecu.equals("true")) {
            	 System.out.println("Vous etes connecte au service de traitement d image !");
            	 
            	 
            	 // ENVOI DE L IMAGE
            	 
            	 System.out.println("veuillez entrer le nom de l image a traiter (exemple : lassonde.jpg) : ");
            	 String nomFichier = keyboard.nextLine();
            	 out.writeObject(nomFichier);
            	 BufferedImage image = ImageIO.read(new File(nomFichier));
            	 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            	 ImageIO.write(image, "jpg", byteArrayOutputStream);
            	 byteArrayOutputStream.flush();
            	 int size = byteArrayOutputStream.size();
            	 out.writeObject(size); 	 
            	 
            	 try {
					String recutaille = in.readObject().toString();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
            	 byte tabImage[] = byteArrayOutputStream.toByteArray();
            	 out.writeObject(tabImage);
            	 System.out.println("Image envoyee");
            	 try {
					String recuimage = in.readObject().toString();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
    
            	 // RECEPTION DE L IMAGE
            	 
            	String tailleImage = null;
				try {
					tailleImage = in.readObject().toString();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
            	 out.writeObject("taille bien recue");
            	 int sobelSize = Integer.parseInt(tailleImage);
            	 byte[] tabSobel = new byte[sobelSize];
            	 in.readFully(tabSobel, 0, sobelSize);
 				 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tabSobel);
 				 BufferedImage imageSobel = ImageIO.read(byteArrayInputStream);
 				 System.out.println("Image bien recue");
 				 System.out.println("Entrez le nom sous lequel enregistrer l image Sobel sans l extension : ");
 				 String nomSobel = keyboard.nextLine();
 				 ImageIO.write(imageSobel, "jpg", new File(nomSobel + ".jpg"));
 				 System.out.println("Votre image convertie se trouve dans le meme fichier que votre client sous le nom " + nomSobel + ".jpg");
 				 out.writeObject("TERMINE");
             } else {
            	 System.out.println("Mauvaise combinaison de login/password !");
             }
	         socket.close();

		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
