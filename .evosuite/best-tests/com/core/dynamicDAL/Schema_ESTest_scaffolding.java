/**
 * Scaffolding file used to store all the setups needed to run 
 * tests automatically generated by EvoSuite
 * Fri Jun 28 11:25:40 GMT 2024
 */

package com.dilizity.dynamicDAL;

import org.evosuite.runtime.annotation.EvoSuiteClassExclude;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;
import org.evosuite.runtime.sandbox.Sandbox;
import org.evosuite.runtime.sandbox.Sandbox.SandboxMode;

@EvoSuiteClassExclude
public class Schema_ESTest_scaffolding {

  @org.junit.Rule 
  public org.evosuite.runtime.vnet.NonFunctionalRequirementRule nfr = new org.evosuite.runtime.vnet.NonFunctionalRequirementRule();

  private static final java.util.Properties defaultProperties = (java.util.Properties) java.lang.System.getProperties().clone(); 

  private org.evosuite.runtime.thread.ThreadStopper threadStopper =  new org.evosuite.runtime.thread.ThreadStopper (org.evosuite.runtime.thread.KillSwitchHandler.getInstance(), 3000);


  @BeforeClass 
  public static void initEvoSuiteFramework() { 
    org.evosuite.runtime.RuntimeSettings.className = "com.core.dynamicDAL.Schema"; 
    org.evosuite.runtime.GuiSupport.initialize(); 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfThreads = 100; 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfIterationsPerLoop = 10000; 
    org.evosuite.runtime.RuntimeSettings.mockSystemIn = true; 
    org.evosuite.runtime.RuntimeSettings.sandboxMode = org.evosuite.runtime.sandbox.Sandbox.SandboxMode.RECOMMENDED; 
    org.evosuite.runtime.sandbox.Sandbox.initializeSecurityManagerForSUT(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.init();
    setSystemProperties();
    initializeClasses();
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
  } 

  @AfterClass 
  public static void clearEvoSuiteFramework(){ 
    Sandbox.resetDefaultSecurityManager(); 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
  } 

  @Before 
  public void initTestCase(){ 
    threadStopper.storeCurrentThreads();
    threadStopper.startRecordingTime();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().initHandler(); 
    org.evosuite.runtime.sandbox.Sandbox.goingToExecuteSUTCode(); 
    setSystemProperties(); 
    org.evosuite.runtime.GuiSupport.setHeadless(); 
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
    org.evosuite.runtime.agent.InstrumentingAgent.activate(); 
  } 

  @After 
  public void doneWithTestCase(){ 
    threadStopper.killAndJoinClientThreads();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().safeExecuteAddedHooks(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.reset(); 
    resetClasses(); 
    org.evosuite.runtime.sandbox.Sandbox.doneWithExecutingSUTCode(); 
    org.evosuite.runtime.agent.InstrumentingAgent.deactivate(); 
    org.evosuite.runtime.GuiSupport.restoreHeadlessMode(); 
  } 

  public static void setSystemProperties() {
 
    java.lang.System.setProperties((java.util.Properties) defaultProperties.clone()); 
    java.lang.System.setProperty("file.encoding", "Cp1252"); 
    java.lang.System.setProperty("java.awt.headless", "true"); 
    java.lang.System.setProperty("java.io.tmpdir", "C:\\Users\\ASIF~1.IQB\\AppData\\Local\\Temp\\"); 
    java.lang.System.setProperty("user.country", "US"); 
    java.lang.System.setProperty("user.dir", "E:\\Personal\\RND\\DynamicDataAccessLayer\\DynamicDAL"); 
    java.lang.System.setProperty("user.home", "C:\\Users\\asif.iqbal"); 
    java.lang.System.setProperty("user.language", "en"); 
    java.lang.System.setProperty("user.name", "asif.iqbal"); 
    java.lang.System.setProperty("user.timezone", "Asia/Karachi"); 
  }

  private static void initializeClasses() {
    org.evosuite.runtime.classhandling.ClassStateSupport.initializeClasses(Schema_ESTest_scaffolding.class.getClassLoader() ,
      "org.apache.commons.collections4.MapIterator",
      "org.apache.commons.collections4.keyvalue.AbstractMapEntry",
      "org.apache.commons.collections4.keyvalue.AbstractKeyValue",
      "org.apache.commons.collections4.map.ListOrderedMap$ListOrderedMapEntry",
      "org.apache.commons.collections4.KeyValue",
      "org.apache.commons.collections4.map.ListOrderedMap",
      "org.apache.commons.collections4.map.AbstractMapDecorator",
      "org.apache.commons.collections4.Get",
      "org.apache.commons.collections4.OrderedMapIterator",
      "com.core.dynamicDAL.Schema",
      "org.apache.commons.collections4.Put",
      "com.core.dynamicDAL.Query",
      "org.apache.commons.collections4.map.ListOrderedMap$ListOrderedIterator",
      "org.apache.commons.collections4.map.ListOrderedMap$EntrySetView",
      "org.apache.commons.collections4.IterableMap",
      "org.apache.commons.collections4.OrderedMap",
      "org.apache.commons.collections4.OrderedIterator",
      "org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator",
      "org.apache.commons.collections4.map.AbstractIterableMap",
      "org.apache.commons.collections4.IterableGet"
    );
  } 

  private static void resetClasses() {
    org.evosuite.runtime.classhandling.ClassResetter.getInstance().setClassLoader(Schema_ESTest_scaffolding.class.getClassLoader()); 

    org.evosuite.runtime.classhandling.ClassStateSupport.resetClasses(
      "com.core.dynamicDAL.Schema",
      "org.apache.commons.collections4.map.AbstractIterableMap",
      "org.apache.commons.collections4.map.AbstractMapDecorator",
      "org.apache.commons.collections4.map.ListOrderedMap",
      "org.apache.commons.collections4.map.ListOrderedMap$KeySetView",
      "org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator",
      "org.apache.commons.collections4.map.ListOrderedMap$KeySetView$1",
      "org.apache.commons.collections4.map.ListOrderedMap$EntrySetView",
      "org.apache.commons.collections4.map.ListOrderedMap$ListOrderedIterator",
      "com.core.dynamicDAL.Query",
      "org.apache.commons.collections4.map.ListOrderedMap$ValuesView",
      "org.apache.commons.collections4.keyvalue.AbstractKeyValue",
      "org.apache.commons.collections4.keyvalue.AbstractMapEntry",
      "org.apache.commons.collections4.map.ListOrderedMap$ListOrderedMapEntry",
      "org.apache.commons.collections4.collection.AbstractCollectionDecorator",
      "org.apache.commons.collections4.list.AbstractListDecorator",
      "org.apache.commons.collections4.list.AbstractSerializableListDecorator",
      "org.apache.commons.collections4.list.UnmodifiableList",
      "com.core.dynamicDAL.Param",
      "com.google.common.base.Converter",
      "com.google.common.base.Converter$FunctionBasedConverter",
      "com.google.common.base.Preconditions",
      "com.google.common.base.Converter$IdentityConverter",
      "com.google.common.base.Converter$ConverterComposition",
      "org.apache.commons.collections4.map.ListOrderedMap$ListOrderedMapIterator",
      "com.google.common.base.Converter$1",
      "org.apache.commons.collections4.map.ListOrderedMap$ValuesView$1"
    );
  }
}
