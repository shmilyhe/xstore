package com.eshore.xmlstore;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.eshore.xmlstore.core.GenericDaoXmlImpl;
import com.eshore.xmlstore.vo.Book;
import com.eshore.xmlstore.vo.User;

public class TestApp {
	
	public UserService getService(){
		UserServiceImpl  imp = new UserServiceImpl();
		GenericDaoXmlImpl dao = new GenericDaoXmlImpl();
		dao.setClasses(new Class[]{User.class,Book.class});
		imp.setGenericDao(dao);
		//dao.setFile("teset.xml");
		dao.init();
		imp.init();
		return imp;
	}

	@Test
	public void testsave(){
		UserService servive =getService();
		List<User> list = new ArrayList<User>();
		{
			User u = new User();
			u.setName("eric"+System.currentTimeMillis());
			u.setTitle("CTO");
			u.setAge("21");
			
			List<Book> bl = new ArrayList<Book>();
			long ct =System.currentTimeMillis();
			for(int i =0;i<10;i++){
				Book b = new Book();
				b.setName("book"+ct+i);
				b.setPrice(ct);
				bl.add(b);
			}
			u.setBookList(bl);
			servive.saveOrUpdate(u);
		}
		//servive.saveOrUpdate(t);
		//u.setId(System.currentTimeMillis());
		/*for(int i=0;i<100000;i++){
		User u = new User();
		u.setName("eric"+i);
		u.setTitle("CTO");
		u.setAge("21");
		list.add(u);
		}
		servive.saveOrUpdateAll(list);*/
		User user= servive.getUser("eric1467709725908");
		System.out.println(user.getBookList().size());
		long bt =System.currentTimeMillis();
		/*for(int i=0;i<1000000;i++){
			servive.getUser("eric1466144181775");
			servive.getUser("eric1466144181775","21");
			servive.getUser("eric0","21");
		//System.out.println(servive.getUser("eric1466144181775"));
		//System.out.println(servive.getUser("eric1466144181775","21"));
		}*/
		System.out.println(System.currentTimeMillis()-bt);
	}
}
