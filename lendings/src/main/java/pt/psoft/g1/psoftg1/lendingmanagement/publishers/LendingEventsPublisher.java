package pt.psoft.g1.psoftg1.lendingmanagement.publishers;

import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

/**
 * Interface for publishing Lending events to the message broker.
 */
public interface LendingEventsPublisher {

    LendingViewAMQP sendLendingCreated(Lending lending);

    LendingViewAMQP sendLendingReturned(Lending lending, Long currentVersion);
}

