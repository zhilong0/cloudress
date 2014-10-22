package com.tt.idm.service.contract;

public interface UserAuthorityService {

	public boolean assign(String userCode, String roleName);

	public boolean revoke(String userCode, String roleName);

}
