package com.eshore.xmlstore;






import com.eshore.xmlstore.core.CrudImpl;
import com.eshore.xmlstore.vo.User;

public class UserServiceImpl extends CrudImpl<User> implements UserService {
	protected void initCache() {
		super.initCache();
		cache.createIndex("name-age", "name","age");
		cache.createIndex("name", "name");
	}

	public User getUser(String name) {
		read.lock();
		try {
			return cache.get("name", name);
		} finally {
			read.unlock();
		}
	}
	
	public User getUser(String name,String age) {
		read.lock();
		try {
			return cache.get("name-age", name,age);
		} finally {
			read.unlock();
		}
	}


}
