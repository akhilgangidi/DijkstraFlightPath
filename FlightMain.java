import java.io.*;
import java.util.*;
public class FlightMain 
{
	private List<String> listNode;
	private Set<String> list;
	private List<Root> visitedNodeList;
	private PriorityQueue<City> priorityQueue;
	
	private String timeCost[][];
	private int t1;
	private int t2;
	private String vertexS;
	private String vertexD;
	
	private HashMap<String, Integer> minDist;
	private HashMap<String, Integer> vertexDist;
	
	public FlightMain(List<String> listNode)
	{
		this.listNode = listNode;
		list = new HashSet<String>();
		visitedNodeList = new ArrayList<Root>();
		priorityQueue = new PriorityQueue<City>(new City());
		minDist = new HashMap<String, Integer>();
		vertexDist = new HashMap<String, Integer>();
	}
	private static List<String> rootPath(List<Root> cityVisit, String target)
	{
		List<String> pathComplete = new ArrayList<String>();
		for(Root p : cityVisit)
		{
			if(!p.exists(target))
			{
				continue;
			}
			pathComplete = rootPath(cityVisit, p.getNode());
			pathComplete.add(target);
			return pathComplete;
		}
		pathComplete.add(target);
		return pathComplete;
	}
	private String getVertex()
	{
		String node = priorityQueue.remove().city;
		return node;
	}
	private boolean vertexExists(List<Root> visitedVerteces, String node)
	{
		for(Root p : visitedNodeList)
		{
			if(p.getNode().equals(node))
				return true;
		}
		return false;
	}
	
	public void dijkstraAlg(String timeCost[][], String path[])
	{
		String vertex;
		vertexS = path[0];
		vertexD = path[1];
		if(path[2].equalsIgnoreCase("C"))
		{
			t2 = 3;
			t1 = 2;
		}
		else
		{
			t2 = 2;
			t1 = 3;
		}
		this.timeCost = timeCost;
		for(String vert : listNode)
		{
			minDist.put(vert, Integer.MAX_VALUE);
			vertexDist.put(vert, Integer.MAX_VALUE);
		}
		priorityQueue.add(new City(vertexS, 0));
		minDist.replace(vertexS, 0);
		vertexDist.replace(vertexS, 0);
		while(!priorityQueue.isEmpty())
		{
			vertex = getVertex();
			Root evalNode = new Root();
			evalNode.setNode(vertex);
			list.add(vertex);
			findAdjacent(vertex, evalNode);
			if(!vertexExists(visitedNodeList, vertex))
			{
				visitedNodeList.add(evalNode);
			}
		}
	}
	private void findAdjacent(String nodeEval, Root nodeEvalList)
	{
		int dist = -1;
		int newDist = -1;
		
		for(int i = 0; i < timeCost.length; i++)
		{
			if(!timeCost[i][0].equals(nodeEval))
			{
				continue;
			}
			String target;
			for(int j = 0; j < listNode.size(); j++)
			{
				target = listNode.get(j);
				if(!timeCost[i][1].equals(target))
					continue;
				if(!list.contains(target))
				{
					dist = Integer.parseInt(timeCost[i][t1]);
					newDist = minDist.get(nodeEval) + dist;
					if(newDist < minDist.get(target))
					{
						minDist.replace(target, newDist);
						vertexDist.replace(target, vertexDist.get(nodeEval) + Integer.parseInt(timeCost[i][t2]));
						for(Root p : visitedNodeList)
						{
							if(p.exists(target))
								p.delete(target);
							break;
						}
						nodeEvalList.add(target);
					}
					priorityQueue.add(new City(target, minDist.get(target)));
				}
			}
		}
	}
	
	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		String timeCost[][];
		String pathList[][];
		String str;
	
		String cityString;
		PrintWriter outputFile = new PrintWriter("output1.txt");
		
		BufferedReader flightData = new BufferedReader(new FileReader("FlightData1.txt"));
		BufferedReader dataRequest = new BufferedReader(new FileReader("Requested.txt"));
		
		List<String> nodeList = new ArrayList<String>();
		timeCost = new String[Integer.parseInt(flightData.readLine())][4];
		pathList = new String[Integer.parseInt(dataRequest.readLine())][3];
		
		int i = 0;
		int j;
		String node;
		
		while((str = flightData.readLine()) != null)
		{
			int k = 1;
			j = 0;
		
			StringTokenizer data = new StringTokenizer(str, "|");
			while(k <= 2)
			{
				if(!nodeList.contains(node = data.nextToken()))
				{
					timeCost[i][j++] = node;
					nodeList.add(node);
				}
				else
				{
					timeCost[i][j++] = node;
				}
				k++;
			}
			while(data.hasMoreTokens())
			{
				timeCost[i][j++] = data.nextToken();
			}
			i++;
		}
		i = 0;
		
		while((str = dataRequest.readLine()) != null)
		{
			j = 0;
			StringTokenizer data= new StringTokenizer(str, "|");
			while(data.hasMoreTokens())
			{
				pathList[i][j++] = data.nextToken();
			}
			i++;
		}
		i = 1;
		String t;
		String ot;
		
		for(String pathRequest[] : pathList)
		{
			if(!(nodeList.contains(pathRequest[0]) && nodeList.contains(pathRequest[1])))
			{
				outputFile.println("Error: The Path Could Not be Found");
				continue;
			}
			
			FlightMain priorityQueue = new FlightMain(nodeList);
			priorityQueue.dijkstraAlg(timeCost, pathRequest);
			
			if(pathRequest[2].equals("C"))
			{
				t = "Cost";
				ot = "Time";
				outputFile.println("Flight " + i + ": " + priorityQueue.vertexS + ", " + priorityQueue.vertexD + " (Cost)");
			}
			else
			{
				t = "Time";
				ot = "Cost";
				outputFile.println("Flight " + i + ": " + priorityQueue.vertexS + ", " + priorityQueue.vertexD + " (Time)");
			}
			for(String vertex : nodeList)
			{
				if(!vertex.equals(priorityQueue.vertexD))
				{
					continue;
				}
				List<String> list = rootPath(priorityQueue.visitedNodeList, priorityQueue.vertexD);
				for(int k = 0; k < list.size(); k++)
				{
					if(k == list.size() - 1)
						outputFile.print(list.get(k) + ". ");
					else
						outputFile.print(list.get(k) + " --> ");
				}
				outputFile.println(t + ": " + priorityQueue.minDist.get(vertex) + " " + ot + ": " + priorityQueue.vertexDist.get(vertex));
				break;
			}
			i++;
		}
		outputFile.close();
	}

}
