import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class server {

	 public static void main(String[] args) throws Exception {
	    
		InetAddress serverAddress;
		String serverAddressString;
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
        
        ServerSocket listener = new ServerSocket();
        listener.setReuseAddress(true);
		listener.bind(new InetSocketAddress(serverAddress, port));
		System.out.format("Le serveur de conversion Sobel tourne sur: %s:%d%n", serverAddressString, port);
        
		// ATTENTE DES CLIENTS ET CREATION DES THREAD DE TRAITEMENT D IMAGE
		try {
			 while (true) {
	                new SobelConverter(listener.accept()).start();
	            }
        } finally {
            listener.close();
        }
	 }
}
