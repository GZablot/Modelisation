package app;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{

   public static int[][] readpgm(String fn)
	 {		
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();
            String line = d.readLine();
		   while (line.startsWith("#")) {
			  line = d.readLine();
		   }
		   Scanner s = new Scanner(line);
		   int width = s.nextInt();
		   int height = s.nextInt();		   
		   line = d.readLine();
		   s = new Scanner(line);
		   int maxVal = s.nextInt();
		   int[][] im = new int[height][width];
		   s = new Scanner(d);
		   int count = 0;
		   while (count < height*width) {
			  im[count / width][count % width] = s.nextInt();
			  count++;
		   }
		   return im;
        }
		
        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            return null;
        }
    }

    public static void writepgm(int[][] image, String filename){
		try {
			//FileOutputStream fos = new FileOutputStream(new File(filename));
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename));
			/*écrit largeur puis hauteur de l'image sur la même ligne*/
			outputWriter.write(image.length + " " + image[0].length);
			outputWriter.newLine();

			for(int i = 0; i < image.length; i++){
				for(int j = 0; j < image[0].length; j++){
					outputWriter.write(image[i][j] + "");
				}
				outputWriter.newLine();
			}
			outputWriter.close();

		}catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}

	}

   
}
