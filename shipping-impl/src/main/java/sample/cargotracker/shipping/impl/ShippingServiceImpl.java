package sample.cargotracker.shipping.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import javax.inject.Inject;

import sample.cargotracker.registration.api.Cargo;
import sample.cargotracker.registration.api.RegistrationService;
import sample.cargotracker.shipping.api.Itinerary;
import sample.cargotracker.shipping.api.Leg;
import sample.cargotracker.shipping.api.ShippingService;

import java.util.concurrent.CompletionStage;

/**
 * Implementation of the ShippingService.
 */
public class ShippingServiceImpl implements ShippingService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final RegistrationService registrationService;

    @Inject
    public ShippingServiceImpl(PersistentEntityRegistry persistentEntityRegistry, RegistrationService registrationService) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.registrationService = registrationService;
        persistentEntityRegistry.register(ItineraryEntity.class);
    }

    @Override
    public ServiceCall<Leg, Done> addLeg() {
        return ( request) -> {
            CompletionStage<Cargo> response = registrationService.getRegistration().invoke(request.getCargoId());
            PersistentEntityRef<ShippingCommand> itinerary = persistentEntityRegistry.refFor(ItineraryEntity.class, request.getId());
            return itinerary.ask(AddLeg.of(request));
        };
    }

    @Override
    public ServiceCall<Itinerary,  Done> createItinerary() {
        return ( request) -> {
            // Look up the itinerary for thegiven ID.
            PersistentEntityRef<ShippingCommand> itinerary =
                persistentEntityRegistry.refFor(ItineraryEntity.class, request.getId());
            // Tell the entity to use the greeting message specified.
            return itinerary.ask(CreateItinerary.of(request));
        };
    }

}
