import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;

public class CreateNetworks
{
	//Command line arguments
	private static String graphFileName = "";
	private static String activeListFileName = "";

public static void main(String[] argv) throws FileNotFoundException, IOException
{
		
	// Get command line arguments
	if (argv != null) 
	{
		int len = argv.length;
		if (len == 2) 
		{
			graphFileName = argv[0];
			activeListFileName = argv[1];
		}
		else printUsage();
	}

    BufferedReader listReader = new BufferedReader(new FileReader(activeListFileName));

    String listLine = listReader.readLine();
        while (listLine!=null)
        {
		Hashtable genes = new Hashtable(50000);
                BufferedReader activeReader = new BufferedReader(new FileReader(listLine));
                FileOutputStream outFile=new FileOutputStream("network_"+listLine);
                DataOutputStream outData=new DataOutputStream(outFile);
                String activeLine = activeReader.readLine();
                while (activeLine!=null)
                {
			while (activeLine.indexOf(" /// ")!=-1)
			{
				int j = activeLine.indexOf(" /// ");
				String gene = activeLine.substring(0,j);
				activeLine = activeLine.substring(j+5,activeLine.length());
                        	if (!genes.containsKey(gene))
					genes.put(gene,gene);
			}
			if (activeLine.length()>0)
				if (!genes.containsKey(activeLine))
					genes.put(activeLine,activeLine);
                       	activeLine = activeReader.readLine();
                }
                activeReader.close();

    		BufferedReader graphReader = new BufferedReader(new FileReader(graphFileName));
    		String graphLine = graphReader.readLine();
	
		while (graphLine!=null)
		{
			String[] strs = split(graphLine,'\t');
		
			String p1 = strs[0];
			String p2 = strs[1];
		
			if (genes.containsKey(p1) && genes.containsKey(p2))
				outData.writeBytes(graphLine+"\n");

			graphLine = graphReader.readLine();
		}

                listLine = listReader.readLine();
                outData.close();
        }
}

	private static void printUsage()
	{
		System.out.println("Usage: java ProcessSIF <SIF file name> <output file>");
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

