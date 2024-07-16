/* Copyright (C) Red Hat 2024 */
package org.animals;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ApplicationScoped
public class VeterinaryHospital {

  public static final String INGRESS_CHANNEL = "ingress";

  @PostConstruct
  public void init() {}

  void onStart(@Observes StartupEvent ev) {
    Log.infof("Hospital starting up");
  }

  @Incoming(INGRESS_CHANNEL)
  public CompletionStage<Void> processMainFlow(Message<String> message) {
    var payload = message.getPayload();
    Log.infof("Processed message: %s", payload);

    return message.ack();
  }
}
