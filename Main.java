package Modelisation;

import java.util.ArrayList;

/**
 * Created by Guillaume on 19/01/2017.
 */
public class Main {


    public Main(String filename){
        SeamCarving s = new SeamCarving();
        Graph g = s.toGraph(s.interest(s.readpgm("test.pgm")));

        ArrayList<Integer> cout = s.Bellman(g,0,13,s.tritopo(g));
    }



    public static void main(String[] args)
    {
        new Main(args[0]);
    }
}
