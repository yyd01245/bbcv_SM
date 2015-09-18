package prod.nebula.vrc.test;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InjectionTest extends TestCase {

    
  private BeanFactory factory;  
    
  @Override  
  protected void setUp() throws Exception {  
      factory = new ClassPathXmlApplicationContext("applicationContext.xml");   
  }  
  
  public void testInjection1() {  
      Bean1 bean1 = (Bean1)factory.getBean("bean1");  
      System.out.println("bean1.dateValue=" + bean1.getDateValue());  
  }  
}
