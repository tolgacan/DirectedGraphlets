import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;

public class ActiveGenes
{
	//Command line arguments
	private static String listFileName = "";

public static void main(String[] argv) throws FileNotFoundException, IOException
{
		
	// Get command line arguments
	if (argv != null) 
	{
		int len = argv.length;
		if (len == 1) 
		{
			listFileName = argv[0];
		}
		else printUsage();
	}

	// read the dictionary file and create the affyid -> genename dictionary
    BufferedReader idReader = new BufferedReader(new FileReader("affyid_genename.txt"));
	String idLine = idReader.readLine();
	Hashtable probes = new Hashtable(100000);
	while (idLine!=null)
	{
		String strs[] = split(idLine,'\t');
		if (!probes.containsKey(strs[0]))
		{
			probes.put(strs[0],strs[1]);
		}
		else
		{
			System.out.println("Duplicate affy id "+strs[0]);
		}
		idLine = idReader.readLine();
	}

	idReader.close();


    BufferedReader listReader = new BufferedReader(new FileReader(listFileName));

    String listLine = listReader.readLine(); 
	while (listLine!=null)
	{
    		BufferedReader activeReader = new BufferedReader(new FileReader(listLine));
		FileOutputStream outFile=new FileOutputStream("genes_"+listLine);
		DataOutputStream outData=new DataOutputStream(outFile);
		String activeLine = activeReader.readLine();
		while (activeLine!=null)
		{	
			if (((String)probes.get(activeLine))!=null)
				outData.writeBytes(((String)probes.get(activeLine))+"\n");
			activeLine = activeReader.readLine();
		}
		activeReader.close();
    		listLine = listReader.readLine(); 
		outData.close(); 
	}
	
	listReader.close();
}

	private static void printUsage()
	{
		System.out.println("Usage: java ActiveGenes <probeset list file name>");
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

