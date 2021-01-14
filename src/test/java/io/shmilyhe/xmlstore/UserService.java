package io.shmilyhe.xmlstore;

import java.util.List;

import io.shmilyhe.xmlstore.api.Crud;
import io.shmilyhe.xmlstore.vo.User;

public interface UserService  extends Crud<User>{
	User getUser(String name);
	User getUser(String name,String age);
}
