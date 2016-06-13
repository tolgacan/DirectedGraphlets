import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Random;
import java.io.*;

public class RandomizeNetworks
{
	//Command line arguments
	private static String networkListFileName = "";

public static void main(String[] argv) throws FileNotFoundException, IOException
{
    Random rndgen = new Random(System.currentTimeMillis());
		
	// Get command line arguments
	if (argv != null) 
	{
		int len = argv.length;
		if (len == 1)
		{
			networkListFileName = argv[0];
		}
		else printUsage();
	}

    BufferedReader listReader = new BufferedReader(new FileReader(networkListFileName));
    int nnum = 1;
    String listLine = listReader.readLine();
        while (listLine!=null)
        {
            System.out.println("Now randomizing network #"+nnum);
            Hashtable edges = new Hashtable(100);
                BufferedReader networkReader = new BufferedReader(new FileReader(listLine));
                FileOutputStream outFile=new FileOutputStream("random_"+nnum+".txt");
                DataOutputStream outData=new DataOutputStream(outFile);
                String networkLine = networkReader.readLine();
                while (networkLine!=null)
                {
                    String[] strs = split(networkLine,'\t');
                    
                    String p1 = strs[0];
                    String p2 = strs[1];
                    String inttype = strs[2];
                    
                    if (edges.containsKey(inttype))
                    {
                        Vector pairs = (Vector)edges.get(inttype);
                        pairs.addElement(networkLine);
                    }
                    else
                    {
                        Vector pairs = new Vector(100000,50000);
                        pairs.addElement(networkLine);
                        edges.put(inttype,pairs);
                    }
                    
                    networkLine = networkReader.readLine();
                }
                networkReader.close();
            
            for (Enumeration en = edges.keys();en.hasMoreElements();)
            {
                String inttype = (String)en.nextElement();
                Vector pairs = (Vector)edges.get(inttype);
                //System.out.println("There are "+pairs.size()+" edges of type "+inttype+" in network "+listLine);
                
                // now shuffle the pairs
                while (pairs.size()>1)
                {
                    int ind1 = rndgen.nextInt(pairs.size());
                    int ind2 = rndgen.nextInt(pairs.size());
                
                    while (ind2==ind1)
                    {
                        ind2 = rndgen.nextInt(pairs.size());
                    }
                
                    String pair1 = (String)pairs.elementAt(ind1);
                    String pair2 = (String)pairs.elementAt(ind2);
                
                    String[] strs1 = split(pair1,'\t');
                    String[] strs2 = split(pair2,'\t');
                
                    outData.writeBytes(strs1[0]+'\t'+strs2[1]+'\t'+strs1[2]+"\n");
                    outData.writeBytes(strs2[0]+'\t'+strs1[1]+'\t'+strs2[2]+"\n");
                
                    pairs.removeElementAt(ind1);
                    if (ind1>ind2)
                        pairs.removeElementAt(ind2);
                    else
                        pairs.removeElementAt(ind2-1);
                }
            }

            listLine = listReader.readLine();
            outData.close();
            
            nnum++;
        }
}

	private static void printUsage()
	{
		System.out.println("Usage: java RandomizeNetworks <network list file name>");
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

