import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileReader;
import java.util.Iterator;
import org.json.simple.JSONArray;

public class server {

	 public static void main(String[] args) throws Exception {
	    
		InetAddress serverAddress;
		String serverAddressString;
		String login = null;
		String password = null;
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
        
        ServerSocket listener = new ServerSocket();
        listener.setReuseAddress(true);
	listener.bind(new InetSocketAddress(serverAddress, port));
	System.out.format("Le serveur de conversion Sobel tourne sur: %s:%d%n", serverAddressString, port);
        
	try {
        	while (true) {
                new SobelConverter(listener.accept(), "valentin", "bouis").start();
            }
        } finally {
            listener.close();
        }
	 }
}
