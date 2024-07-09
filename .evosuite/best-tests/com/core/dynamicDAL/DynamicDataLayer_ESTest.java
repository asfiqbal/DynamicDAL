/*
 * This file was automatically generated by EvoSuite
 * Fri Jun 28 11:18:38 GMT 2024
 */

package com.dilizity.dynamicDAL;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.dilizity.dynamicDAL.DynamicDataLayer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class DynamicDataLayer_ESTest extends DynamicDataLayer_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      DynamicDataLayer dynamicDataLayer0 = null;
      try {
        dynamicDataLayer0 = new DynamicDataLayer((String) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.evosuite.runtime.System", e);
      }
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      DynamicDataLayer dynamicDataLayer0 = null;
      try {
        dynamicDataLayer0 = new DynamicDataLayer("ybs&4eC-");
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("com.core.dynamicDAL.RootNode", e);
      }
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      DynamicDataLayer dynamicDataLayer0 = null;
      try {
        dynamicDataLayer0 = new DynamicDataLayer("");
        fail("Expecting exception: AssertionError");
      
      } catch(AssertionError e) {
         //
         // schemaName can't be empty
         //
      }
  }
}
