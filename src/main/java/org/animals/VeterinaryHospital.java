/* Copyright (C) Red Hat 2024 */
package org.animals;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class VeterinaryHospital {

  public static final String FISH_CHANNEL = "fish";
  public static final String FELINE_CHANNEL = "feline";
  public static final String MUSTELID_CHANNEL = "mustelid";

  @Inject
  @Remote("animals")
  RemoteCache<String, String> lastBattledAnimal;

  @PostConstruct
  public void init() {}

  void onStart(@Observes StartupEvent ev) {
    Log.infof("Hospital starting up");
  }

  @Incoming(FISH_CHANNEL)
  @Incoming(FELINE_CHANNEL)
  @Incoming(MUSTELID_CHANNEL)
  public CompletionStage<Void> processMainFlow(Message<String> message) {
    var payload = message.getPayload();
    var topic = message.getMetadata(IncomingKafkaRecordMetadata.class).get().getTopic();
    Log.infof("Processed message: %s on topic %s, benching them for the next round", payload, topic);

    lastBattledAnimal.put(topic, payload);

    return message.ack();
  }
}
