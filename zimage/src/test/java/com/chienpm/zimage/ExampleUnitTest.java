package com.chienpm.zimage;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void hashCodeUrl(){
        String mUrl = "http://www.project-disco.org/wp-content/uploads/2018/04/Android-logo-1024x576.jpg";
        String hashed = String.valueOf(mUrl.hashCode()) + ".jpg";
        System.out.println(hashed);
        assertEquals(10, hashed);
    }
}