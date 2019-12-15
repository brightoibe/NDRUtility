/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.HashMap;
import model.Key;

/**
 *
 * @author The Bright
 */
public class TestClass {
    public static void main(String[] arg){
        Key k1,k2;
        k1=new Key(1,2);
        k2=new Key(1,4);
        System.out.println(k1.equals(k2));
       
    }
    public static int[] toArray(int... i){
        int[] ans=i;
        return ans;
    }
    
}
