import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;

public class ProcessNetworkBatch8Bit
{
	//Command line arguments
    private static String graphListFileName = "";

	private static int t = 5;
    
    private static String graphFileName = "";
	private static String outputFileName = "";

	// The adjacency matrix (similarity network)
	private static Hashtable AMatrix = new Hashtable(100000);
    
    private static Hashtable edgeCounts = new Hashtable(4000);
    private static Hashtable doubleEdgeCounts = new Hashtable(2000000);
	private static Hashtable allEdges = new Hashtable(30000);
	private static Hashtable printedEdges = new Hashtable(30000);

public static int intType(String interaction)
{
    if (interaction.equals("interacts-with"))
        return 0;
    else if (interaction.equals("in-complex-with"))
        return 1;
    else if (interaction.equals("catalysis-precedes"))
        return 2;
    else if (interaction.equals("controls-expression-of"))
        return 3;
    else if (interaction.equals("controls-state-change-of"))
        return 4;
    else System.out.println("!!! Unknown Interaction Type in the Network File: "+interaction);
    return -1;
}

    public static String getSymmetric(String e1,int numUndirected)
    {
        String sym_e1;
        char[] csym_e1 = new char[e1.length()];
        int numDirected;
        
        numDirected = (e1.length()-numUndirected)/2;
        for (int i=0;i<e1.length();i++)
        {
            csym_e1[i] = '0';
        }
        
        for (int i=0;i<numUndirected;i++)
        {
            csym_e1[i] = e1.charAt(i);
        }
        
        for (int i=numUndirected;i<numUndirected+numDirected;i++)
        {
            if (e1.charAt(i)=='1')
                csym_e1[i+numDirected] = '1';
        }
        
        for (int i=numUndirected+numDirected;i<e1.length();i++)
        {
            if (e1.charAt(i)=='1')
                csym_e1[i-numDirected] = '1';
        }
        
        sym_e1 = String.valueOf(csym_e1);
        
        return sym_e1;
        
    }
    
    public static boolean isIsomorphic2(String e1,String e2,int numUndirected)
    {
        String sym_e1;
        String sym_e2;
        char[] csym_e1 = new char[e1.length()];
        char[] csym_e2 = new char[e2.length()];
        int numDirected;
        
        numDirected = (e1.length()-numUndirected)/2;
        for (int i=0;i<e1.length();i++)
        {
            csym_e1[i] = '0';
            csym_e2[i] = '0';
        }
        
        for (int i=0;i<numUndirected;i++)
        {
            csym_e1[i] = e1.charAt(i);
            csym_e2[i] = e2.charAt(i);
        }
        
        for (int i=numUndirected;i<numUndirected+numDirected;i++)
        {
            if (e1.charAt(i)=='1')
                csym_e1[i+numDirected] = '1';
            if (e2.charAt(i)=='1')
                csym_e2[i+numDirected] = '1';
        }
        
        for (int i=numUndirected+numDirected;i<e1.length();i++)
        {
            if (e1.charAt(i)=='1')
                csym_e1[i-numDirected] = '1';
            if (e2.charAt(i)=='1')
                csym_e2[i-numDirected] = '1';
        }
        
        sym_e1 = String.valueOf(csym_e1);
        sym_e2 = String.valueOf(csym_e2);
        
        //System.out.print("e1: "+e1+" sym_e1: "+sym_e1+" e2: "+e2+" sym_e2: "+sym_e2);
        
        if ((e1+sym_e1).equals(e2+sym_e2) || (e1+sym_e1).equals(sym_e2+e2))
        {
            //System.out.println(" returns true.");
            return true;
        }
        else
        {
            //System.out.println(" returns false.");
            return false;
        }
    }
    
    public static boolean isIsomorphic3(String e1,String e2,int numUndirected)
    {
        String AB1, BC1, CA1, BA1, CB1, AC1;
        String AB2, BC2, CA2, BA2, CB2, AC2;
        
        int edgeLength = e1.length();
        
        AB1 = e1.substring(0,edgeLength/3);
        BA1 = getSymmetric(AB1,numUndirected);
        
        BC1 = e1.substring(edgeLength/3,2*edgeLength/3);
        CB1 = getSymmetric(BC1,numUndirected);
        
        CA1 = e1.substring(2*edgeLength/3,edgeLength);
        AC1 = getSymmetric(CA1,numUndirected);
        
        AB2 = e2.substring(0,edgeLength/3);
        BA2 = getSymmetric(AB2,numUndirected);
        
        BC2 = e2.substring(edgeLength/3,2*edgeLength/3);
        CB2 = getSymmetric(BC2,numUndirected);
        
        CA2 = e2.substring(2*edgeLength/3,edgeLength);
        AC2 = getSymmetric(CA2,numUndirected);
        
        if ((AB1+BA1+BC1+CB1+AC1+CA1).equals(AB2+BA2+BC2+CB2+AC2+CA2) || (AB1+BA1+BC1+CB1+AC1+CA1).equals(BA2+AB2+AC2+CA2+BC2+CB2) || (AB1+BA1+BC1+CB1+AC1+CA1).equals(CB2+BC2+BA2+AB2+CA2+AC2) || (AB1+BA1+BC1+CB1+AC1+CA1).equals(AC2+CA2+CB2+BC2+AB2+BA2) || (AB1+BA1+BC1+CB1+AC1+CA1).equals(CA2+AC2+AB2+BA2+CB2+BC2) || (AB1+BA1+BC1+CB1+AC1+CA1).equals(BC2+CB2+CA2+AC2+BA2+AB2))
            return true;
        
        return false;
    }
    
    public static boolean isSymmetric(String e1, String e2)
    {
        int k = 0;
        int i;
        k = e1.length();
        if (e2.length()!=k) return false;
        
        if (e1.equals(e2)) return true;
        if (k==8) {
            return isIsomorphic2(e1,e2,2);
        } else // three node graph
        {
            return isIsomorphic3(e1,e2,2);
        }
    }
public static void main(String[] argv) throws FileNotFoundException, IOException
{
		
	// Get command line arguments
	if (argv != null) 
	{
		int len = argv.length;
		if (len == 2)
		{
			graphListFileName = argv[0];
			t = (new Integer(argv[1])).intValue();
		}
		else printUsage();
	}

    BufferedReader listReader = new BufferedReader(new FileReader(graphListFileName));
    String listLine = listReader.readLine();
    int ccc = 1;
    while (listLine!=null)
    {
    
        graphFileName = listLine;
        System.out.println("Processing file #"+ccc+" : "+listLine);
        ccc++;
        outputFileName = "counts_"+listLine;
	//Output file
	FileOutputStream outFile=new FileOutputStream(outputFileName);
	DataOutputStream outData=new DataOutputStream(outFile);

	// read the graph file and create the adjacency matrix
    BufferedReader graphReader = new BufferedReader(new FileReader(graphFileName));

    String graphLine = graphReader.readLine();
	AMatrix = new Hashtable(100000);
    
    edgeCounts = new Hashtable(1000);
    doubleEdgeCounts = new Hashtable(200000);
	allEdges = new Hashtable(200000);
	Hashtable printedEdges = new Hashtable(100000);

	while (graphLine!=null)
	{
		
		String[] strs = split(graphLine,'\t');
		
		if (strs.length<3)
		{
			System.out.println("Unexpected line in the edge file: "+graphLine);
			break;
		}
		
		String p1 = strs[0];
		String p2 = strs[1];
        String interaction = strs[2];
		
		if (interaction.equals("controls-phosphorylation-of") || interaction.equals("controls-transport-of"))
		{
			graphLine = graphReader.readLine();
			continue;
		}
		
		if (AMatrix.containsKey(p1))
		{
			Hashtable n = (Hashtable)(AMatrix.get(p1));
            if (n.containsKey(p2))
            {
                String edges =(String)(n.get(p2));
                char[] edgesChars = edges.toCharArray();
                edgesChars[intType(interaction)]='1';
                edges = String.valueOf(edgesChars);
                n.remove(p2);
                n.put(p2,edges);
            }
            else
            {
                String edges = "00000000";
                char[] edgesChars = edges.toCharArray();
                edgesChars[intType(interaction)]='1';
                edges = String.valueOf(edgesChars);
                n.put(p2,edges);
            }
		}
		else
		{
			Hashtable n = new Hashtable(20);
            String edges = "00000000";
            char[] edgesChars = edges.toCharArray();
            edgesChars[intType(interaction)]='1';
            edges = String.valueOf(edgesChars);
            n.put(p2,edges);
			AMatrix.put(p1,n);
		}

		// insert the other edge for undirected interactions (and directed interactions in reverse: edges 7-11)
		//if (interaction.equals("in-complex-with") || interaction.equals("interacts-with"))
		//{
			if (AMatrix.containsKey(p2))
			{
				Hashtable n = (Hashtable)(AMatrix.get(p2));
                if (n.containsKey(p1))
                {
                    String edges =(String)(n.get(p1));
                    char[] edgesChars = edges.toCharArray();
					if (intType(interaction)<2)
						edgesChars[intType(interaction)]='1';
					else
						edgesChars[intType(interaction)+3]='1'; // reverse edges are at i+3
                    edges = String.valueOf(edgesChars);
                    n.remove(p1);
                    n.put(p1,edges);
                }
                else
                {
                    String edges = "00000000";
                    char[] edgesChars = edges.toCharArray();
					if (intType(interaction)<2)
						edgesChars[intType(interaction)]='1';
					else
						edgesChars[intType(interaction)+3]='1'; // reverse edges are at i+3
                    edges = String.valueOf(edgesChars);
                    n.put(p1,edges);
                }                
			}
			else
			{
				Hashtable n = new Hashtable(20);
                String edges = "00000000";
                char[] edgesChars = edges.toCharArray();
					if (intType(interaction)<2)
						edgesChars[intType(interaction)]='1';
					else
						edgesChars[intType(interaction)+3]='1'; // reverse edges are at i+3
                edges = String.valueOf(edgesChars);
                n.put(p1,edges);
                AMatrix.put(p2,n);
			}
		//}

		graphLine = graphReader.readLine();
	}

	
	System.out.println("There are "+AMatrix.size()+" nodes in the network.");

	int i = 1;
    for (Enumeration en = AMatrix.keys();en.hasMoreElements();)
    {
        String p = (String)en.nextElement();
        Hashtable n = (Hashtable)AMatrix.get(p);
		//System.out.println("Processing node #"+i+"\tsingle = "+edgeCounts.size()+"\tdouble = "+doubleEdgeCounts.size());
		i++;
        for (Enumeration en2 = n.keys();en2.hasMoreElements();)
        {
            String p2 = (String)en2.nextElement();
			
			//if (!(p.compareToIgnoreCase(p2)<0)) continue; // handles symmetry as well
            String edge = (String)n.get(p2);
			
			if (!allEdges.containsKey(edge))
				allEdges.put(edge,edge);
			
			if (edgeCounts.containsKey(edge))
			{
				int count = ((Integer)(edgeCounts.get(edge))).intValue();
				count++;
				edgeCounts.remove(edge);
				edgeCounts.put(edge,new Integer(count));
			}
			else
			{
				edgeCounts.put(edge,new Integer(1));
			}
			
            Hashtable n2 = (Hashtable)AMatrix.get(p2);
            for (Enumeration en3 = n2.keys();en3.hasMoreElements();)
            {
                String p3 = (String)en3.nextElement();
				if (p3.equals(p)) continue;
				//if (p.compareToIgnoreCase(p2)<0 && p.compareToIgnoreCase(p3)<0)
				//{
					String edge2 = (String)n2.get(p3);
					Hashtable n3 = (Hashtable)AMatrix.get(p3);
					String edge3 = "00000000";
					if (n3.containsKey(p))
					{
						edge3 = (String)(n3.get(p));
					}
					if (!allEdges.containsKey(edge+edge2+edge3))
						allEdges.put(edge+edge2+edge3,edge+edge2+edge3);
						
					if (doubleEdgeCounts.containsKey(edge+edge2+edge3))
					{
						int count = ((Integer)(doubleEdgeCounts.get(edge+edge2+edge3))).intValue();
						count++;
						doubleEdgeCounts.remove(edge+edge2+edge3);
						doubleEdgeCounts.put(edge+edge2+edge3,new Integer(count));
					}
					else
					{
						doubleEdgeCounts.put(edge+edge2+edge3,new Integer(1));
					}
				//}
			}
        }
    }
    
	for (Enumeration en = edgeCounts.keys();en.hasMoreElements();)
	{
		String edge = (String)en.nextElement();
		int count = ((Integer)(edgeCounts.get(edge))).intValue();
		boolean isPrinted = false;
		for (Enumeration en2 = printedEdges.keys();en2.hasMoreElements();)
		{
			String pEdge = (String)en2.nextElement();
			if (isSymmetric(edge,pEdge))
			{
                int count2 = ((Integer)(printedEdges.get(pEdge))).intValue();
                if (count!=count2)
                {
                    System.out.println("Symmetric edges "+edge+" and "+pEdge+" have different counts:"+count+" and "+count2);
                    System.exit(1);
                }
				isPrinted = true;
				break;
			}
		}
		if (!isPrinted && count>=t)
		{
			printedEdges.put(edge,new Integer(count));
			outData.writeBytes(edge+"\t"+count+"\n");
		}

	}
	
	for (Enumeration en = doubleEdgeCounts.keys();en.hasMoreElements();)
	{
		String edge = (String)en.nextElement();
		int count = ((Integer)(doubleEdgeCounts.get(edge))).intValue(); 
		boolean isPrinted = false;
		for (Enumeration en2 = printedEdges.keys();en2.hasMoreElements();)
		{
			String pEdge = (String)en2.nextElement();
			if (isSymmetric(edge,pEdge))
			{
                int count2 = ((Integer)(printedEdges.get(pEdge))).intValue();
                if (count!=count2)
                {
                    System.out.println("Symmetric edges "+edge+" and "+pEdge+" have different counts:"+count+" and "+count2);
                    System.exit(1);
                }

				isPrinted = true;
				break;
			}
		}
		if (!isPrinted && count>=t)
		{
			printedEdges.put(edge,new Integer(count));
			outData.writeBytes(edge+"\t"+count+"\n");
		}
	}
	
	outData.close();
        
    listLine = listReader.readLine();
  }
  listReader.close();
}

	private static void printUsage()
	{
		System.out.println("Usage: java ProcessNetworkBatch8Bit <file listing the network file names> <count threshold>");
		System.exit(1);
	}

	public static String[] split(String str, char delim)
    {
                // begin split
                Vector strsVec = new Vector(0,1);
                String tmp = str;
                while (tmp.indexOf(delim)!=-1)
                {
                		if (tmp.substring(0,tmp.indexOf(delim)).length()>0)
                        	strsVec.addElement(new String(tmp.substring(0,tmp.indexOf(delim))));
                        tmp = tmp.substring(tmp.indexOf(delim)+1,tmp.length());
                }
                strsVec.addElement(new String(tmp));
                String[] strs = new String[strsVec.capacity()];
                for (int s = 0; s < strsVec.capacity(); s++)
                        strs[s] = (String)strsVec.elementAt(s);
                // end of split
                return strs;
    }
}

