package org.odata4j.test.issues;

import junit.framework.Assert;

import org.core4j.Enumerable;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.AbstractRuntimeTest;

public class Issue16 extends AbstractRuntimeTest {

  public Issue16(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void issue16() {

    String endpointUri = "http://localhost:8816/Issue16.svc/";

    final String[] actualNavProp = new String[1];
    InMemoryProducer producer = new InMemoryProducer("Issue16") {
      @Override
      public EntitiesResponse getNavProperty(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {

        actualNavProp[0] = navProp;
        return Responses.entities(Enumerable.<OEntity> create().toList(), EdmEntitySet.newBuilder().setName("messageLog").build(), null, null);
      }
    };

    DefaultODataProducerProvider.setInstance(producer);
    ODataServer server = this.rtFacade.startODataServer(endpointUri);
    ODataConsumer c = this.rtFacade.create(endpointUri, null, null);
    c.getEntities("Message").nav(124L, "messageLog()").execute().count();
    Assert.assertNotNull(actualNavProp[0]);
    Assert.assertEquals("messageLog", actualNavProp[0]);

    server.stop();

  }

}
