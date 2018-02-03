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
        
        JSONParser parser = new JSONParser();
        
        try 
        {
        	Object obj = parser.parse(new FileReader("login.json"));
            JSONArray jsonArray = (JSONArray)obj;
            boolean found = false;
            
            for (Object o : jsonArray)
            {
            	JSONObject utilisateur = (JSONObject) o;
            	if (login == utilisateur.get("login"))
            	{
            		if (password == utilisateur.get("password")) {found = true;}
            		else {}
            	}
            }
            
            if (!found)
            {
            	FileWriter file = new FileWriter("login.json");
            	JSONObject newJSON = new JSONObject();
        	    newJSON.put("login", login);
        	    newJSON.put("password", password);
        	    jsonArray.add(newJSON);
        	    file.write(jsonArray.toJSONString());
                file.flush();
        	    
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        
        
    }
	
	
}
