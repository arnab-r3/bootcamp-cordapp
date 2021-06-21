package bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(TokenIssueFlowInitiator.class)
public class TokenIssueFlowResponder extends FlowLogic<Void> {

    private final FlowSession otherSide;

    public TokenIssueFlowResponder(FlowSession otherSide) {
        this.otherSide = otherSide;
    }

    @Override
    @Suspendable
    public Void call() throws FlowException {
        SignedTransaction signedTransaction = subFlow(new SignTransactionFlow(otherSide) { // does the job of responding...
            @Suspendable
            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException { // add additional logic
                // Implement responder flow transaction checks here
                // gives the counter party to define their own logic

            }
        });
        subFlow(new ReceiveFinalityFlow(otherSide, signedTransaction.getId())); // receive the validated transaction after validating the notarised transaction against the signed transaction

        return null;
    }
}