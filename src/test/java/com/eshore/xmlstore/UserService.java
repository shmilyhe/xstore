package com.eshore.xmlstore;

import java.util.List;




import com.eshore.xmlstore.api.Crud;
import com.eshore.xmlstore.vo.User;

public interface UserService  extends Crud<User>{
	User getUser(String name);
	User getUser(String name,String age);
}
