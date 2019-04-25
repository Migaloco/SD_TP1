package microgram.impl.clt.rest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import discovery.Discovery;
import microgram.api.Profile;
import microgram.api.java.Profiles;
import microgram.api.java.Result;
import microgram.api.rest.RestProfiles;
import microgram.impl.srv.rest.ProfilesRestServer;

public class RestProfilesClient extends RestClient implements Profiles {

	public RestProfilesClient(URI serverUri) {
		super(serverUri, RestProfiles.PATH);
	}
	
	public RestProfilesClient() {
		super(Discovery.findUrisOf(ProfilesRestServer.SERVICE, 1)[0], RestProfiles.PATH);
	}
	
	@Override
	public Result<Profile> getProfile(String userId) {
		Response r = target.path(userId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		
		return super.responseContents(r, Status.OK, new GenericType<Profile>() {});
	}
	
	@Override
	public Result<Void> createProfile(Profile profile) {
		// TODO Auto-generated method stub
		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(profile, MediaType.APPLICATION_JSON));

		return responseContents(r, Status.OK, new GenericType<Void>() {});
	}

	@Override
	public Result<Void> deleteProfile(String userId) {
		// TODO Auto-generated method stub
		Response r = target.path(userId)
				.request()
				.delete();

		return responseContents(r, Status.OK, new GenericType<Void>() {
		});
	}

	@Override
	public Result<List<Profile>> search(String prefix) {
		// TODO Auto-generated method stub
		Response r = target.queryParam("search", prefix)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		return responseContents(r, Status.OK, new GenericType<List<Profile>>() {});
	}

	@Override
	public Result<Void> follow(String userId1, String userId2, boolean isFollowing) {
		// TODO Auto-generated method stub
		Response r = target.path(userId1+"/following/"+userId2)
				.request()
				.put(Entity.entity(isFollowing, MediaType.APPLICATION_JSON));

		return responseContents(r, Status.OK, new GenericType<Void>() {
		});
	}

	@Override
	public Result<Boolean> isFollowing(String userId1, String userId2) {
		// TODO Auto-generated method stub
		Response r = target.path(userId1+"/following/"+userId2)
				.request()
				.get();

		return responseContents(r, Status.OK, new GenericType<Boolean>() {
		});
	}
	
	
}
