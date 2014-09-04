package edu.isi.madcat.xlmem;

import java.net.MalformedURLException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
// import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.net.URLEncoder;
import edu.isi.madcat.xlmem.Levenshtein;

public class SolrLookup {
	
	private static String readFile(String pathname) throws IOException {

	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
	
  public static void main(String[] args) throws SolrServerException, IOException {
	  String serverUrl = "http://studio.isi.edu:8983/solr/fishoil1";
	  HttpSolrServer server = new HttpSolrServer(serverUrl);
	  server.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
	  server.setConnectionTimeout(5000); // 5 seconds to establish TCP
	  // Setting the XML response parser is only required for cross
	  // version compatibility and only when one side is 1.4.1 or
	  // earlier and the other side is 3.1 or later.
	  server.setParser(new XMLResponseParser()); // binary parser is used by default
	  // The following settings are provided here for completeness.
	  // They will not normally be required, and should only be used 
	  // after consulting javadocs to know whether they are truly required.
	  // server.setSoTimeout(1000);  // socket read timeout
	  // server.setDefaultMaxConnectionsPerHost(100);
	  // server.setMaxTotalConnections(100);
	  // server.setFollowRedirects(false);  // defaults to false
	  // allowCompression defaults to false.
	  // Server side must support gzip or deflate for this to have any effect.
	  // server.setAllowCompression(true);
	  
	  // ModifiableSolrParams params = new ModifiableSolrParams();
      // params.set("q", "1");

      // QueryResponse response = server.query(params);
      // System.out.println("response = " + response);
      
	  String inputPathname = null;
	  if (args.length > 0) {
		  inputPathname = readFile(args[0]);
	  }	
	  else {
		  inputPathname = "/tmp/test.txt";
	  }
	  String searchKey = readFile(inputPathname).trim();
	  System.out.println("Input pathname [" + inputPathname + "] has contents [" + searchKey + "]");
      SolrQuery query = new SolrQuery();
      query.setQuery("en_toktext:" + URLEncoder.encode(searchKey, "utf-8"));
      // query.addFilterQuery("cat:electronics","store:amazon.com");
      query.setFields("id","score", "en_tokenized", "ko_tokenized", "en_content", "ko_content", "en_toktext", "ko_toktext");
      query.setStart(0);
      query.setRows(100);
      query.set("defType", "edismax");
      
      QueryResponse response = server.query(query);
      SolrDocumentList results = response.getResults();
      results.getNumFound();
      System.out.println("Query [" + searchKey + "] yields [" + results.size() + "] results, [" + results.getNumFound() + "] total found");
      for (int i = 0; i < results.size(); ++i) {
    	  	SolrDocument doc = results.get(i);
    	  	String en_toktext = (String) doc.getFieldValue("en_toktext");
    	  	int dist = Levenshtein.distance(searchKey, en_toktext);
    	  	System.out.println("Doc [" + doc.getFieldValue("id") + "] has en_toktext [" + en_toktext + "] at dist [" + dist + "]");
      }
  }
} 
