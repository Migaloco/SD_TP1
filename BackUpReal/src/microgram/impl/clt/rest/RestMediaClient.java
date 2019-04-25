package microgram.impl.clt.rest;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import discovery.Discovery;
import microgram.api.java.Media;
import microgram.api.java.Result;
import microgram.api.rest.RestMediaStorage;
import microgram.impl.srv.rest.MediaStorageRestServer;

public class RestMediaClient extends RestClient implements Media {

	public RestMediaClient() {
		super(Discovery.findUrisOf(MediaStorageRestServer.SERVICE, 1)[0], RestMediaStorage.PATH);
	}
	
	@Override
	public Result<String> upload(byte[] bytes) {
		
		return super.reTry(() -> uploadTry(bytes));
	}
	
	@Override
	public Result<byte[]> download(String id) {

		return super.reTry(() -> downloadTry(id));
	}
	
	@Override
	public Result<Void> delete(String id) {
		return super.reTry(() -> deleteTry(id));
	}
	
	
	private Result<String> uploadTry(byte[] bytes) {
		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(bytes, MediaType.APPLICATION_OCTET_STREAM));

		return responseContents(r, Status.OK, new GenericType<String>() {});
	}
	
	
	private Result<byte[]> downloadTry(String id) {
		Response r = target.path(id)
				.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.get();
		
		return responseContents(r, Status.OK, new GenericType<byte[]>() {});
	}

	
	private Result<Void> deleteTry(String id) {
		Response r = target.path(id)
				.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.delete();

		return responseContents(r, Status.OK, new GenericType<Void>() {
		});
	}
}