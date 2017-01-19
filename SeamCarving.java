package Modelisation;

import sun.nio.cs.ext.IBM037;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{
    private ArrayList<Integer> chemin = new ArrayList<Integer>();

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
            File file = new File(System.getProperty("user.dir")+ "/" + filename);
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
			/*écrit largeur puis hauteur de l'image sur la même ligne*/
            outputWriter.write("P2\n"+image[0].length + " " + image.length+ "\n255");

            for(int i = 0; i < image.length; i++){
                for(int j = 0; j < image[0].length; j++){
                    if(j % 26 == 0)
                        outputWriter.newLine();
                    if(image[i][j] != -1)
                        outputWriter.write(image[i][j] + " ");
                }
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
        int moyenneVoisins;
        for(int i = 0; i < image.length; i++){
            for(int j = 0; j < image[0].length; j++){
                /*à la fin de la ligne*/
                if(j == (image[0].length)-1 ){
                    tab[i][j] = Math.abs(image[i][j] - image[i][j-1]);
                }else{
                    /*au debut de la ligne*/
                    if(j == 0){
                        tab[i][j] = Math.abs(image[i][j] - image[i][j+1]);
                    }else{
                        moyenneVoisins = (image[i][j-1] + image[i][j+1])/2;
                        tab[i][j] = Math.abs(image[i][j] - moyenneVoisins );
                    }
                }
            }
        }

        return tab;
    }

    public Graph toGraph(int[][] itr){
        int i,j,cmptSommet = 1;
        GraphArrayList g = new GraphArrayList((itr.length * itr[0].length)+2);

        /* créer le premier sommet de n° 0
        *  itr.length = ligne
        *  itr[0].length = colonne */
        for (j = 1; j <= itr[0].length ; j++)
            g.addEdge(new Edge(0, j, 0));

        /* parcours de toutes les lignes sauf la dernière qui sera relié au
         * point final */
        for (i = 0; i < (itr.length)-1; i++) {
            for (j = 0; j < itr[0].length; j++) {
                /*à la fin de la ligne*/
                if(j == (itr[0].length)-1 ){

                    g.addEdge(new Edge((i*4)+itr[0].length, (((1+i)*4)+itr[0].length)-1 , itr[i][j]));
                    g.addEdge(new Edge((i*4)+itr[0].length, ((1+i)*4)+itr[0].length , itr[i][j]));
                }else{
                    /*au debut de la ligne*/
                    if(j == 0){
                        g.addEdge(new Edge((i*4)+1, (((1+i)*4)+1)+1 , itr[i][j]));
                        g.addEdge(new Edge((i*4)+1, ((1+i)*4)+1 , itr[i][j]));
                    }else{
                        /*au milieu de la ligne*/
                        g.addEdge(new Edge((i*4)+1+j, (((1+i)*4)+1)+j-1 , itr[i][j]));
                        g.addEdge(new Edge((i*4)+1+j, (((1+i)*4)+1)+j , itr[i][j]));
                        g.addEdge(new Edge((i*4)+1+j, (((1+i)*4)+1)+j+1 , itr[i][j]));
                    }
                }
            }
        }

        /*rejoint les sommets de la dernière ligne au sommet final*/
        for (j = 1; j <= itr[0].length ; j++)
            g.addEdge(new Edge((i*4)+j, (itr[0].length*itr.length)+1, itr[i][j-1]));

        return g;
    }

    public void dfs(Graph g, int u)
    {
        Test.visite[u] = true;

        //System.out.println("Je visite " + u);
        for (Edge e: g.next(u))
            if (!Test.visite[e.to])
                dfs(g,e.to);
        chemin.add(u);
    }

    public ArrayList<Integer> tritopo(Graph g){
        Test.visite = new boolean[g.vertices()+2];
        dfs(g, 0);
		/*ordre inverse de l'ordre suffixe*/
        Collections.reverse(chemin);

        for(int i:chemin) {
            System.out.print(i+" ");
        }
        System.out.print("\n");
        return chemin;
    }

    public int min(ArrayList<Integer> al){

        int min = 0;
        if(!al.isEmpty())
            min = al.get(0);

        for(Integer i : al){
            if(i < min) min = i;
        }
        return min;
    }

    public int getIndice(ArrayList<Integer> al, int val){
        int i = 0;
        while( i < al.size()){
            if(al.get(i) == val)
                return i;

            i++;
        }

        return -1;
    }

    public ArrayList<Integer> Bellman(Graph g, int s, int t, ArrayList<Integer> order){
        Map<Integer, Integer> T = new HashMap<>(order.size());
        Iterable<Edge> sommetsPrecedent ;
        ArrayList<Integer> cheminMin = new ArrayList<Integer>();
        ArrayList<Integer> passageSommets = new ArrayList<Integer>();
        Map<Integer, Integer> indiceSommets = new HashMap<>(order.size());
        int coutMin = 0, indiceSommet = 0;
        T.put(s, 0);

        for(int i = 0; i < order.size(); i++){
            /*test valeur sommets précédents et prend la valeur minimum*/
            sommetsPrecedent = g.prev(order.get(i));
            for (Edge e:sommetsPrecedent) {
                //System.out.println("sommet: " + order.get(i)+"from: "+ e.from + " to: "+ e.to+ " "+ (T.get(e.from) + e.cost));
                cheminMin.add(T.get(e.from) + e.cost);
                indiceSommets.put(e.from,T.get(e.from) + e.cost );
            }
            coutMin = this.min(cheminMin);

            for(Map.Entry e: indiceSommets.entrySet()){
                if(coutMin == (int) e.getValue()){
                    indiceSommet = (int) e.getKey();
                    break;
                }
            }

            indiceSommets.clear();
            cheminMin.clear();
            /*correspondance entre le sommet dans l'ordre topologique avec coutMin*/
            T.put(order.get(i), coutMin);
            /*correspondance entre le sommet dans l'ordre topologique et le sommet précédement visité*/
            passageSommets.add(indiceSommet);

        }

        for(int i = 0; i < order.size(); i++){
            System.out.println("Sommet: "+order.get(i) + " CCM: "+ T.get(order.get(i)));
        }
        for(int i = 0; i < order.size(); i++){
            System.out.println("Sommet: "+order.get(i) + " passage:" +passageSommets.get(i));
        }

        /*creer la liste des sommets du ccm (mais bug)*/
        ArrayList<Integer> res = new ArrayList<>();
        int j = t;
        while(passageSommets.get(j) != 0){
            j = this.getIndice(order, passageSommets.get(j));
            res.add(order.get(j));
            //j = this.getIndice(order, passageSommets.get(j));
        }

        Collections.reverse(res);


        return res;
    }

    /**
     *
     * @param img tableau de l'image
     * @param al liste des sommets à supprimer
     */
    public int[][] suppression(int[][] img, ArrayList<Integer> al){
        for(int i =0; i < img.length; i ++){
            for(int j = 0; j < img[0].length; j++){
                if(al.get(i) == ((i*4)+j+1))
                    img[i][j] = -1;
            }
        }

        return img;
    }

}
