package microgram.impl.srv.rest;

import microgram.api.java.Media;
import microgram.api.rest.RestMediaStorage;
import microgram.impl.srv.java.JavaMedia;

public class RestMediaResources extends RestResource implements RestMediaStorage {

	final Media impl;
	
	public RestMediaResources(String baseUri ) {
		this.impl = new JavaMedia( baseUri + RestMediaStorage.PATH );
	}
	
	@Override
	public synchronized String upload(byte[] bytes) {
		return super.resultOrThrow( impl.upload(bytes));
	}

	@Override
	public synchronized byte[] download(String id) {
		return super.resultOrThrow( impl.download(id));
 	}
	
	@Override
	public synchronized void delete(String id) {
		super.resultOrThrow( impl.delete(id));
 	}
}
