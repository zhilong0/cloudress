package com.df.idm.registration;

public interface TokenStore {

	Token getToken(String token);

	boolean save(Token token);

	boolean expire(String token);
}
