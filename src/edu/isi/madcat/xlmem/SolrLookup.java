package edu.isi.madcat.xlmem;

/* adapted from http://www.vogella.com/tutorials/MySQLJava/article.html */

import java.net.MalformedURLException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class SolrLookup {
  public static void main(String[] args) throws MalformedURLException, SolrServerException {
	  String serverUrl = "http://studio.isi.edu:8983/solr";
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
	  server.setSoTimeout(1000);  // socket read timeout
	  server.setDefaultMaxConnectionsPerHost(100);
	  server.setMaxTotalConnections(100);
	  server.setFollowRedirects(false);  // defaults to false
	  // allowCompression defaults to false.
	  // Server side must support gzip or deflate for this to have any effect.
	  server.setAllowCompression(true);
	  
	  ModifiableSolrParams params = new ModifiableSolrParams();
      params.set("q", "1");

      QueryResponse response = server.query(params);

      System.out.println("response = " + response);

  }
} 
