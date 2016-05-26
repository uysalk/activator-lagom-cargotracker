package sample.cargotracker.registration.api;

import static com.lightbend.lagom.javadsl.api.Service.*;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Source;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import org.pcollections.PSequence;

/**
 * The registration service interface.
 * <p/>
 * This describes everything that Lagom needs to know about how to serve and consume the RegistrationService.
 */
public interface RegistrationService extends Service {

    /**
     * Example: curl -H "Content-Type: application/json" -X POST -d
     * '{
     * "cargo": {
     * "id": 1,
     * "name": "laptop",
     * "description": "macbook",
     * "owner": "Clark Kent",
     * "destination": "Metropolis"
     * }
     * }' http://localhost:9000/api/registration
     */
    ServiceCall<Cargo, Done> register();


    ServiceCall<NotUsed, PSequence<Cargo>> getAllRegistrations();

    ServiceCall<String,  Cargo> getRegistration();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("registrationService").with(
                restCall(Method.POST, "/api/registration", this::register),
                restCall(Method.GET, "/api/registration/all", this::getAllRegistrations),
                restCall(Method.GET, "/api/registration/:id", this::getRegistration)
        ).withAutoAcl(true);
        // @formatter:on
    }
}
