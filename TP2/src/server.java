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
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
            	Socket socket = listener.accept();
            	
            	InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String pseudomdp = br.readLine();
                
                //pseudomdp de la forme pseudo:mdp
                String pseudo = pseudomdp.split(":")[0];
                String mdp = pseudomdp.split(":")[1];
                System.out.println("Pseudo : " + pseudo);
                System.out.println("Mot de passe : " + mdp);
            	
                Boolean co = verifierCredentials(pseudo, mdp);
                System.out.println(co);
            
                //new SobelConverter(listener.accept(), "valentin", "bouis").start();
            }
        } finally {
            listener.close();
        }
	 }
	 
	 public static boolean verifierCredentials(String login, String mdp) throws FileNotFoundException, IOException, ParseException {
		
		 JSONParser parser = new JSONParser();
	        
    	Object obj = parser.parse(new FileReader("login.json"));
        JSONArray jsonArray = (JSONArray)obj;
        
        for (Object o : jsonArray)
        {
        	JSONObject utilisateur = (JSONObject) o;
        	if (login == utilisateur.get("login"))
        	{
        		if (mdp == utilisateur.get("password")) { return true; }
        		else {
        			return false;
        		}
        	}
        }
        
        	FileWriter file = new FileWriter("login.json");
        	JSONObject newJSON = new JSONObject();
    	    newJSON.put("password", mdp);
    	    newJSON.put("login", login);
    	    jsonArray.add(newJSON);
    	    file.write(jsonArray.toJSONString());
            file.flush();
    	    return true;

	 }
}
