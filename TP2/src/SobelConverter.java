import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import javax.imageio.ImageIO;



import java.awt.image.BufferedImage;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.FileWriter;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;





public class SobelConverter extends Thread {

	private Socket socket;
	private String pseudoClient;

	public SobelConverter(Socket socket) {

		this.socket = socket;

	}

	public void run() {

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			int size = 0;
			BufferedImage image = null;

			// Credentials
			
				String messageRecu = in.readLine();
				pseudoClient = messageRecu.split(":")[0];
                String mdp = messageRecu.split(":")[1];
                if(SobelConverter.verifierCredentials(pseudoClient, mdp)) {
                	System.out.println("Connexion acceptee");
                	out.println("true");
                }
                else {

                	System.out.println("Connexion refusee");
                	out.println("false");
               }

			//Reception nom image et taille du bytearray

                String nomImage = in.readLine();
				String taille = in.readLine();
				size = Integer.parseInt(taille);
				out.println("taille recue");

			//Reception de l image

				byte[] tabImage = readExactly(socket.getInputStream(), size);
				InputStream byteArrayInputStream = new ByteArrayInputStream(tabImage);
				image = ImageIO.read(byteArrayInputStream);
				String ipportclient = socket.getRemoteSocketAddress().toString();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();;
				System.out.println("[" + this.pseudoClient + " - " + ipportclient + " - " + dateFormat.format(date) + "] Image " + nomImage + " bien recue");
				out.println("image recue");

			//Traitement de l image
				
				BufferedImage imageSobel = process(image);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           	 	ImageIO.write(imageSobel, "jpg", byteArrayOutputStream);
	           	int sobelSize = byteArrayOutputStream.size();
	        	out.println(sobelSize);
	        	String confirmation = in.readLine();
	        	byte tabSobel[] = byteArrayOutputStream.toByteArray();
	        	socket.getOutputStream().write(tabSobel);
	        	confirmation = in.readLine();

		} catch (IOException e) {

            System.out.println("Error handling client# " + e);

        } catch (ParseException e) {

			e.printStackTrace();

		} 

		  catch (NumberFormatException e) {

			e.printStackTrace();

		}

		finally {

            try {

                socket.close();

            } catch (IOException e) {

                System.out.println("Couldn't close a socket, what's going on?");

            }

            System.out.println("Connection with client closed");

        }

	}

	public static boolean verifierCredentials(String login, String mdp) throws FileNotFoundException, IOException, ParseException 

	 {
		JSONParser parser = new JSONParser();

   	Object obj = parser.parse(new FileReader("./src/login.json"));

       JSONArray userList = (JSONArray)obj;
      
       for (Object o : userList) {
       	JSONObject utilisateur = (JSONObject) o;

       	if (login.equals (utilisateur.get("login"))) {

       		if (mdp.equals(utilisateur.get("password"))) { return true; }
       		else {
       			return false;
       		}
       	}
       }
   	FileWriter file = new FileWriter("./src/login.json");

   	JSONObject newJSON = new JSONObject();

   	newJSON.put("login", login);

	    newJSON.put("password", mdp);

	    userList.add(newJSON);

	    file.write(userList.toJSONString());

        file.flush();
	    return true;
	 }

	

	public static byte[] readExactly(InputStream input, int size) throws IOException //Fonction Buggee

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

	

	public static BufferedImage process(BufferedImage image) throws IOException 

	{

	  System.out.println("applying filter");

	

	  int x = image.getWidth();

	  int y = image.getHeight();

	  int[][] edgeColors = new int[x][y];

	  int maxGradient = -1;

	

	  for (int i = 1; i < x - 1; i++) {

	      for (int j = 1; j < y - 1; j++) {

	

	          int val00 = getGrayScale(image.getRGB(i - 1, j - 1));

	          int val01 = getGrayScale(image.getRGB(i - 1, j));

	          int val02 = getGrayScale(image.getRGB(i - 1, j + 1));

	

	          int val10 = getGrayScale(image.getRGB(i, j - 1));

	          int val11 = getGrayScale(image.getRGB(i, j));

	          int val12 = getGrayScale(image.getRGB(i, j + 1));

	

	          int val20 = getGrayScale(image.getRGB(i + 1, j - 1));

	          int val21 = getGrayScale(image.getRGB(i + 1, j));

	          int val22 = getGrayScale(image.getRGB(i + 1, j + 1));

	

	          int gx =  ((-1 * val00) + (0 * val01) + (1 * val02)) 

	                  + ((-2 * val10) + (0 * val11) + (2 * val12))

	                  + ((-1 * val20) + (0 * val21) + (1 * val22));

	

	          int gy =  ((-1 * val00) + (-2 * val01) + (-1 * val02))

	                  + ((0 * val10) + (0 * val11) + (0 * val12))

	                  + ((1 * val20) + (2 * val21) + (1 * val22));

	

	          double gval = Math.sqrt((gx * gx) + (gy * gy));

	          int g = (int) gval;

	

	          if(maxGradient < g) 

	          {

	              maxGradient = g;

	          }

	          edgeColors[i][j] = g;

	      }

      }



      double scale = 255.0 / maxGradient;



      for (int i = 1; i < x - 1; i++) {

          for (int j = 1; j < y - 1; j++) {

              int edgeColor = edgeColors[i][j];

              edgeColor = (int)(edgeColor * scale);

              edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;



              image.setRGB(i, j, edgeColor);

          }

      }

      

      fillOutlineWithZeros(image, x, y);

      

      System.out.println("Finished");

      

      return image;

  }

	

	private static BufferedImage fillOutlineWithZeros(BufferedImage image, int x, int y)

	{

		for (int i = 0; i < x; i++)

		{

			image.setRGB(i, 0, 0);

			image.setRGB(i, y-1, 0);

		}

		

		for (int j = 0; j < y; j++)

		{

			image.setRGB(0, j, 0);

			image.setRGB(x-1, j, 0);

		}

		

		return image;

	}



  private static int getGrayScale(int rgb) 

  {

      int r = (rgb >> 16) & 0xff;

      int g = (rgb >> 8) & 0xff;

      int b = (rgb) & 0xff;



      //from https://en.wikipedia.org/wiki/Grayscale, calculating luminance

      int gray = (int)(0.2126 * r + 0.7152 * g + 0.0722 * b);



      return gray;

  }

}
