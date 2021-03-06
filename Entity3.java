public class Entity3 extends Entity
{    
	//Array to hold which nodes to send updates to
	private static int[] directNeighbors = {0,2};
	
	//Array to hold the current minimum costs to all nodes
	private static int minimumCosts[] = new int[NetworkSimulator.NUMENTITIES];
	
    public Entity3()
    {
    	//Initialize distance table making everything "infinity"
    	for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
    	{
    		for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
    		{
    			distanceTable[i][j] = 999;
    		}
    	}
    	
    	//Update distance table values with immediate neighbor values
    	distanceTable[0][3] = 7;
    	distanceTable[2][3] = 2;
    	distanceTable[3][3] = 0;
    	
    	//Minimum cost to each of the other nodes
    	for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
    	{
    		int min = 999;
    		for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
    		{
    			if (distanceTable[i][j] < min)
    			{
    				min = distanceTable[i][j];
    			}
    		}
    		minimumCosts[i] = min;
    	}
    	
    	//Send all direct neighbors the minimum costs to all nodes
    	for (int i : directNeighbors)
    	{
    		Packet p = new Packet (3, i, minimumCosts);
    		NetworkSimulator.toLayer2(p);
    	}    	
    }
    
    //When node receives information from other nodes, update the distance table
    public void update(Packet p)
    {
    	//Says whether the minimum costs updated given the new information
    	boolean didUpdate = false;
    	
    	for(int i = 0; i < minimumCosts.length; i++)
    	{
    		int newCostToNode = p.getMincost(i) + minimumCosts[p.getSource()];
    		int currentCostToNode = distanceTable[i][p.getSource()];
    		
    		//If the new cost to a node is less than the current, update the distance table
    		if (newCostToNode < currentCostToNode)
    		{
    			distanceTable[i][p.getSource()] = newCostToNode;
    			
    			//Check to see if this new cost is also a minimum cost
    			if (newCostToNode < minimumCosts[i])
    			{
    				minimumCosts[i] = newCostToNode;
    				didUpdate = true;
    			}
    		}
    	}
    	
    	//If update occurred then send update to direct neighbors
    	if (didUpdate)
    	{
    		for(int i : directNeighbors)
    		{
    			Packet updatePacket = new Packet (3, i, minimumCosts);
    			NetworkSimulator.toLayer2(updatePacket);
    		}
    	}
    }
    
    //Called when link fails or has greater traffic
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    	//Reset all values in the node
    	for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
    	{
    		for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
    		{
    			distanceTable[i][j] = 999;
    		}
    	}
    	
    	distanceTable[0][3] = 7;
    	distanceTable[2][3] = 2;
    	distanceTable[3][3] = 0;
    	
    	//Update the link to match its new value
    	distanceTable[whichLink][3] = newCost;
    	
    	//Minimum cost to each of the other nodes
    	for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
    	{
    		int min = 999;
    		for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
    		{
    			if (distanceTable[i][j] <= min)
    			{
    				min = distanceTable[i][j];
    			}
    		}
    		minimumCosts[i] = min;
    	}
    	
    	//Send out updated minimum costs
    	for (int i : directNeighbors)
    	{
    		Packet p =  new Packet (3, i, minimumCosts);
    		NetworkSimulator.toLayer2(p);
    	}
    }
    
    public void printDT()
    {
        System.out.println("         via");
        System.out.println(" D3 |   0   2");
        System.out.println("----+--------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 3)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j += 2)
            {
               
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
