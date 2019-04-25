package microgram.impl.clt.java;

import java.util.List;

import microgram.api.Profile;
import microgram.api.java.Profiles;
import microgram.api.java.Result;

public class RetryProfilesClient extends RetryClient implements Profiles {

	final Profiles impl;

	public RetryProfilesClient( Profiles impl ) {
		this.impl = impl;	
	}
	
	@Override
	public Result<Profile> getProfile(String userId) {
		return reTry( () -> impl.getProfile(userId));
	}

	@Override
	public Result<Void> createProfile(Profile profile) {
		// TODO Auto-generated method stub
		return reTry( () -> impl.createProfile(profile));
	}

	@Override
	public Result<Void> deleteProfile(String userId) {
		// TODO Auto-generated method stub
		return reTry( () -> impl.deleteProfile(userId));
	}

	@Override
	public Result<List<Profile>> search(String prefix) {
		// TODO Auto-generated method stub
		return reTry( () -> impl.search(prefix));
	}

	@Override
	public Result<Void> follow(String userId1, String userId2, boolean isFollowing) {
		// TODO Auto-generated method stub
		return reTry( () -> impl.follow(userId1, userId2, isFollowing));
	}

	@Override
	public Result<Boolean> isFollowing(String userId1, String userId2) {
		// TODO Auto-generated method stub
		return reTry( () -> impl.isFollowing(userId1, userId2));
	}
}
