import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
			       System.out.println("IP non valide, r�essayez");
			}
		}
        
        while (true){
        	System.out.println("Veuillez entrer le port d'�coute");
   
        	try{
	        	port = keyboard.nextInt();
	        	if (port <= 5050 && port >= 5000)
	        	{
	        		System.out.println("port valide");
	        		break;
	        	}
	        	else System.out.println("port hors de l'intervalle 5000-5050, r�essayez");
	        }
        	catch (InputMismatchException e)
        	{
        		System.out.println("format du port incorrect, r�essayez");
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
 			 out = new PrintWriter(socket.getOutputStream());
 			 
 			 String pseudomdp = login + ":" + password;
             out.write(pseudomdp);
             out.flush();
      
             
           
             
       
             
            
             
	         socket.close();

		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
