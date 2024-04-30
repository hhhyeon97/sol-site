package com.sol.shop;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Test {
    private Integer age;
    private String name;
    public void add1age(){
        this.age = this.age +1;
    }
    public void ageSetting(Integer a){
        if(a >0 && a<100) {
            this.age = a;
        }
    }

}



