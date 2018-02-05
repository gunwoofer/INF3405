import java.io.FileReader;
import java.io.FileWriter;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class SobelConverter extends Thread {

	private Socket socket;
	
	public SobelConverter(Socket socket, String login, String password) {
        this.socket = socket;
        
        System.out.println("Nouvelle connexion de "+login+" at "+socket);
        
        
        	
    }
	
	
}