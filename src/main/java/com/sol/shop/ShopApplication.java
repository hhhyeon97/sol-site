package com.sol.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {
String hello() {
	return "hi!";
}
	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);

		String hi = "hello!";
		int age = 20;
		final int count = 1;
//		count = 2;
//		System.out.println(hi);
	/*	if(age==20){
			System.out.println(count);
		}
		for(int i=0; i<3; i++){
			System.out.println(hi);
		}*/
		Test test = new Test();
		System.out.println(test.name);
		test.hello();
		Friend friend = new Friend("Park",15);
		System.out.println(friend.name);
		System.out.println(friend.age);
	}

}

class Test {
	String name = "kim";
	void hello(){
		System.out.println("안녕");
	}
}
class Friend {
	String name = "Lee";
	int age = 20;
	Friend(String name, int age){
		this.name = name;
		this.age = age;
	}
}