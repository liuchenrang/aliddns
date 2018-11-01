package org.xinghuo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    @Test
    public void testTest(){
        Set<String> allip = new HashSet<>();
        allip.add("127.0.0.1");
        allip.add("127.0.0.2");
        allip.add("127.0.0.3");
        Set<String> unused = new HashSet<>();

        Set<String> used = new HashSet<>();
        used.add("127.0.0.2");
        used.add("127.0.0.1");

        unused.addAll(allip);
        unused.removeAll(used);
        System.out.println(unused);

    }
}
