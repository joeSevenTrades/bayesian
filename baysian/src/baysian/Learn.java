package baysian;

import java.util.*;

public class Learn {	
	/*
	To Learn a Baysian Network, not Dynamic
	*/
	public Digraph<String> learnStructures(Digraph<String> Nini, data da){
//		Nini is a Digraph that without any edge
		Digraph<String> Nres = Nini;
//		Nres is the result of the Digraph
		Digraph<String> Np = Nres;
		Double Sres=Double.MIN_VALUE;
		boolean flag= false;
		LinkedList<Digraph<String>> TABU = new LinkedList<Digraph<String>>();
//		Need to get the right flag
		while(flag){
//			Have a N-times Loop
//			Fix every Vertex each time
			for(int i =0;i!=da.getN();++i){
				Map<Digraph<String>,Double> temp = new HashMap<Digraph<String>,Double>();
				Digraph<String> graphToFix = new Digraph<String>(Np);
//				Have a N-times Loop
//				Fix every Vertex each time
				for(int j=0;j!=da.getN();j++){
//					if i == j, it must not be a DAG
					if(i==j)	continue;
//					Test if t contains the edge i->j
					else if(graphToFix.contains(da.getVl().get(i),da.getVl().get(j))){
//						if t contains i->j, try to delete or reverse one 
						Digraph<String> graphContainsEdgeRemove = new Digraph<String>(graphToFix);
						Digraph<String> graphContainsEdgeReverse = new Digraph<String>(graphToFix);
						graphContainsEdgeReverse.remove(da.getVl().get(i), da.getVl().get(j));
						graphContainsEdgeRemove.reverse(da.getVl().get(i), da.getVl().get(j));
						if(graphContainsEdgeRemove.isDag()&&!TABU.contains(graphContainsEdgeRemove)){
							temp.put(graphContainsEdgeRemove, null);
							TABU.add(graphContainsEdgeRemove);
						}
						if(graphContainsEdgeReverse.isDag()&&!TABU.contains(graphContainsEdgeReverse)){
							temp.put(graphContainsEdgeReverse, null);
							TABU.add(graphContainsEdgeReverse);
						}
					}
//					if t doesn't contains i->j, try to add one 
					else{
						Digraph<String> graphNotContainsEdgeAdd = new Digraph<String>(graphToFix);
						graphNotContainsEdgeAdd.add(da.getVl().get(i), da.getVl().get(j));
						if(graphNotContainsEdgeAdd.isDag()&&!TABU.contains(graphNotContainsEdgeAdd)){
							temp.put(graphNotContainsEdgeAdd, null);
							TABU.add(graphNotContainsEdgeAdd);
						}			
					}
				}
//				Scoring the temp HERE
				

//				Travel through the temp MAP, get the best neighbour 
				Double max=Double.MIN_VALUE;
				Digraph<String> Npp=new Digraph<String>();
				Iterator<HashMap.Entry<Digraph<String>, Double>> entries = temp.entrySet().iterator();  

				while (entries.hasNext()) {  
				    Map.Entry<Digraph<String>, Double> entry = entries.next();
//				    To score 
				    double score = llscore(da.getCore(),entry.getKey());
				    entry.setValue(score);
				    if(entry.getValue()>max){
				    	Npp=entry.getKey();
				    	max=entry.getValue();
				    }
				}
//				if the best neighbor is better than current result, replace it.
				if(max>Sres){
					Nres=Npp;
					Sres=max;
				}
				Np=Npp;
			}
			
		}
		return Nres; 
	}
}
