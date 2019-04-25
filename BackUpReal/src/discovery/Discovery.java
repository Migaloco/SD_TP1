package discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Discovery {

	private static Logger Log = Logger.getLogger(Discovery.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
	}
	
	
	static final InetSocketAddress DISCOVERY_ADDR = new InetSocketAddress("226.226.226.226", 2266);
	static final int DISCOVERY_PERIOD = 1000;
	static final int DISCOVERY_TIMEOUT = 5000;

	private static final String DELIMITER = "\t";

	/**
	 * 
	 * Announces periodically a service in a separate thread .
	 * 
	 * @param serviceName the name of the service being announced.
	 * @param serviceURI the location of the service
	 */
	public static void announce(String serviceName, String serviceURI) {
		Log.info(String.format("Starting Discovery announcements on: %s for: %s -> %s", DISCOVERY_ADDR, serviceName, serviceURI));
		
		byte[] pktBytes = String.format("%s%s%s", serviceName, DELIMITER, serviceURI).getBytes();

		DatagramPacket pkt = new DatagramPacket(pktBytes, pktBytes.length, DISCOVERY_ADDR);
		new Thread(() -> {
			try (DatagramSocket ms = new DatagramSocket()) {
				for (;;) {
					ms.send(pkt);
					Thread.sleep(DISCOVERY_PERIOD);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}


	/**
	 * Performs discovery of instances of the service with the given name.
	 * 
	 * @param  serviceName the name of the service being discovered
	 * @param  minRepliesNeeded the required number of service replicas to find. 
	 * @return an array of URI with the service instances discovered. Returns an empty, 0-length, array if the service is not found within the alloted time.
	 * 
	 */
	public static URI[] findUrisOf(String serviceName, int minRepliesNeeded) {
		Set<URI> replies = new HashSet<URI>();
		long deadLine = System.currentTimeMillis() + DISCOVERY_TIMEOUT;
		final int MAX_DATAGRAM_SIZE = 65536;
		final InetAddress group = DISCOVERY_ADDR.getAddress();
		if( ! group.isMulticastAddress()) {
		    System.out.println( "Not a multicast address (use range : 224.0.0.0 -- 239.255.255.255)");
		    System.exit( 1);
		}

		try( MulticastSocket socket = new MulticastSocket(DISCOVERY_ADDR.getPort())) {
		    socket.joinGroup( group);
		    String receiveS	= null;
		    while(replies.size() < minRepliesNeeded) {
		    	int timeout = (int) ((deadLine) - System.currentTimeMillis());
			    if(timeout < 0) {
			    	break;
			    }
			    socket.setSoTimeout(DISCOVERY_TIMEOUT); 
		    	byte[] buffer = new byte[MAX_DATAGRAM_SIZE] ;
		        DatagramPacket request = new DatagramPacket( buffer, buffer.length ) ;
		        socket.receive( request );
		        System.out.write( request.getData(), 0, request.getLength() ) ;
		        receiveS = new String(request.getData(), 0, request.getLength());
		        String url[] = receiveS.split(DELIMITER);
		        if(url.length <= 1) {
		        	continue;
		        } else if (url[0].equals(serviceName)){
		        	deadLine = System.currentTimeMillis() + DISCOVERY_TIMEOUT;
		        	replies.add(URI.create(url[1]));
		        }
		    }    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return replies.toArray(new URI[replies.size()]);
	}
	}	

