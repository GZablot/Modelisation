package Modelisation;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{

   public static int[][] readpgm(String fn)
	 {		
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            System.out.println("valeur de f: "+ f);
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
            //TODO modifier le chemin selon PC
            File file = new File("C:\\Users\\Guillaume\\Desktop\\Ecole\\Modélisation\\"+ filename);
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
			/*écrit largeur puis hauteur de l'image sur la même ligne*/
			outputWriter.write("P2\n"+image[0].length + " " + image.length+ "\n255");

			for(int i = 0; i < image.length; i++){
				for(int j = 0; j < image[0].length; j++){
                    if(j % 26 == 0)
                        outputWriter.newLine();
					outputWriter.write(image[i][j] + " ");
				}
				//outputWriter.newLine();
			}
			outputWriter.close();

		}catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}

	}

	public int[][] interest(int[][] image){
        int[][] tab = new int[image.length][image[0].length];
        for(int i = 0; i < image.length; i++){
            for(int j = 0; j < image[0].length; j++){
                if(j == (image[0].length)-1 ){
                    tab[i][j] = Math.abs(image[i][j] - image[i][j-1]);
                }else{
                    if(j == 0){
                        tab[i][j] = Math.abs(image[i][j] - image[i][j+1]);
                    }else{
                        int moyenneVoisins = (image[i][j-1] + image[i][j+1])/2;
                        tab[i][j] = Math.abs(image[i][j] - moyenneVoisins );
                    }
                }
            }
        }

        return tab;
    }

   
}
