import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;

public class client {

	public static void main(String[] args) {
		Socket socket;
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
		
		
		try {
		
		     socket = new Socket(serverAddress, port);	
	         socket.close();

		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
