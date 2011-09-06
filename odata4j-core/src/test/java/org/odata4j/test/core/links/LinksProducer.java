/*
 * Copyright 2011 rozan04.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odata4j.test.core.links;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.core4j.Enumerable;
import org.core4j.Enumerables;
import org.core4j.Func;
import org.core4j.Func1;
import org.core4j.Funcs;
import org.core4j.ThrowingFunc;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.examples.producer.ProducerUtil;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.ODataProducerProvider;

public class LinksProducer extends InMemoryProducer {

  public LinksProducer() {
    super("LinksProducer");
    
    this.register(A.class, String.class, "As", new Func<Iterable<A>>() {

      @Override
      public Iterable<A> apply() {
        return as;
      }
      
    }, "Name");

    this.register(B.class, String.class, "Bs", new Func<Iterable<B>>() {

      @Override
      public Iterable<B> apply() {
        return bs;
      }
      
    }, "Name");
    
     this.register(C.class, String.class, "Cs", new Func<Iterable<C>>() {

      @Override
      public Iterable<C> apply() {
        return cs;
      }
      
    }, "Name");
  }
  
  public static void main(String[] args) {

    String endpointUri = "http://localhost:8887/LinksProducer.svc/";

    final InMemoryProducer producer = new LinksProducer();

    // register the producer as the static instance, then launch the http server
    ODataProducerProvider.setInstance(producer);
    ProducerUtil.hostODataServer(endpointUri);
  }
  
  private static final List<C> cs = new ArrayList<C>(2);
  private static final List<B> bs = new ArrayList<B>(1);
  private static final List<A> as = new ArrayList<A>(2);
  
  static {
    
    cs.add(new C("c0", "c0"));
    cs.add(new C("c1", "c1"));
    
    bs.add(new B("b0", "b0"));
    
    A emptyA = new A("a0", "an A with empty links");
    as.add(emptyA);
    
    A a = new A("a1", "an A with links");
    a.setMyB(bs.get(0));
    a.addC(cs.get(0));
    a.addC(cs.get(1));
    as.add(a);
  }

  // I'm feeling boring today....
  // A --------0..1 -----------B
  // |
  // ----------0..* -----------C
  
  public static class A {
    private String name;
    private String description;
    private B myB = null;
    private List<C> myCs = new ArrayList<C>();
    
    public A(String name, String description) {
      this.name = name; this.description = description;
    }
    
    public String getName() {
      return name;
    }
     
    public String getDescripion() {
      return description;
    }
    
    public B getMyB() { return myB; }
    public void setMyB(B value) { myB = value; }
    
    public List<C> getMyCs() { return myCs; }
    public void setMyCs(List<C> value) { myCs = value; }
    public void addC(C value) { myCs.add(value); }
  }
  
  public static class B {
    private String name;
    private String description;
    
    public B(String name, String description) {
      this.name = name; this.description = description;
    }
    
    public String getName() {
      return name;
    }
     
    public String getDescripion() {
      return description;
    }
  }
  
   public static class C {
    private String name;
    private String description;
    
    public C(String name, String description) {
      this.name = name; this.description = description;
    }
    
    public String getName() {
      return name;
    }
     
    public String getDescripion() {
      return description;
    }
  }
  
}
