package Modelisation;

import java.util.ArrayList;
import java.util.Collections;

class Test
{
	static boolean visite[];

	public static void dfs(Graph g, int u)
	{
		visite[u] = true;

		//System.out.println("Je visite " + u);
		for (Edge e: g.next(u))
			if (!visite[e.to])
				dfs(g,e.to);
	}


	public static void testGraph()
	{
		/*int n = 5;
		int i,j;
		GraphArrayList g = new GraphArrayList(n*n+2);
		
		for (i = 0; i < n-1; i++)
		  for (j = 0; j < n ; j++)
			g.addEdge(new Edge(n*i+j, n*(i+1)+j, 1664 - (i+j)));

		for (j = 0; j < n ; j++)		  
		  g.addEdge(new Edge(n*(n-1)+j, n*n, 666));
		
		for (j = 0; j < n ; j++)					
		  g.addEdge(new Edge(n*n+1, j, 0));
		
		g.addEdge(new Edge(13,17,1337));
		g.writeFile("test.dot");
		// dfs à partir du sommet 3
		visite = new boolean[n*n+2];
		dfs(g, 3);*/

		SeamCarving s = new SeamCarving();
		 /*test fonction d'ecriture fichier pgm (creer un fichier)*/
		//s.writepgm(s.readpgm("test.pgm"), "reductionTest.pgm");
		 /*test fonction d'interet (creer un fichier)*/
		//s.writepgm(s.interest(s.readpgm("test.pgm")), "interest.pgm");


		/*Graph g = s.toGraph(s.interest(s.readpgm("ex1.pgm")));
		g.writeFile("test.dot");*/



		// dfs à partir du sommet 0
		//visite = new boolean[g.vertices()+2];
		//dfs(g, 0);

		/*test tritopo*/
		//s.tritopo(g);





		/*ArrayList<Integer> cout = s.Bellman(g,0,13,s.tritopo(g));
		for(int i = 0; i < cout.size(); i++){
			System.out.println("Sommet où passer: "+cout.get(i));
		}

		*//*test supression pixels*//*
		s.writepgm(s.suppression(s.readpgm("ex1.pgm"), cout), "reductionTestEx1.pgm");*/
		int[][] img = s.readpgm("ex1.pgm");

		int[][] interest = s.interest(img);
		Graph g = s.toGraph(interest);
		// g.writeFile("res/interest0098.dot");



			/*interest = s.interest(img);
			g = s.toGraph(interest);*/


		ArrayList<Integer> altopo = s.tritopo(g);


		ArrayList<Integer> al = s.Bellman(g, 0, g.vertices() - 1, altopo);

		//for (Integer i : al) System.out.print(" " + i);

		System.out.println(al.size());
		img = s.suppression(img, al);
		SeamCarving.writepgm(img, "src/jlk967js987k.pgm");

		for(int i = 0; i < 20; i++) {
			img = s.readpgm("jlk967js987k.pgm");

			interest = s.interest(img);
			g = s.toGraph(interest);
			// g.writeFile("res/interest0098.dot");



			/*interest = s.interest(img);
			g = s.toGraph(interest);*/


			altopo = s.tritopo(g);


			al = s.Bellman(g, 0, g.vertices() - 1, altopo);

			//for (Integer i : al) System.out.print(" " + i);

			System.out.println(al.size());
			img = s.suppression(img, al);
			SeamCarving.writepgm(img, "src/jlk967js987k.pgm");
		}
	}

	public static void main(String[] args)
	{
		testGraph();
	}
}
