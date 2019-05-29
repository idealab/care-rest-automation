package ca.freedommobile.api.framework.testcases;

import ca.freedommobile.api.framework.data.ExcelProvider;

public class Test {


    public void test1(){
        ExcelProvider provider = new ExcelProvider();
        System.out.println(provider.getAPiData("GetAccount"));
    }
}
