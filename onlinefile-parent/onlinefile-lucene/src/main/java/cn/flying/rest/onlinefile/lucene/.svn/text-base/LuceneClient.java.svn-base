package cn.flying.rest.onlinefile.lucene;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.stereotype.Component;

@Component
public class LuceneClient {
	
	private static LuceneClient lc = new LuceneClient();

	private Client client;
	
	private LuceneClient(){
		
	}
	
	public static LuceneClient getLuceneClient(){
		return lc;
	}
	
	@SuppressWarnings("resource")
	public Client getClient(String ip,int port,String clusterName){
		if(client==null){
			Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).put("client.transport.sniff",true).build();
			client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(ip, port));
		}
		return client;
	}
	
}
