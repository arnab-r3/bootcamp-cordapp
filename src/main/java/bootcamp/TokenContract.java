package bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";


    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getInputs().size() != 0)
            throw new IllegalArgumentException("Zero inputs expected"); // use [IllegalArgumentException] only
        if (tx.getOutputs().size() != 1)
            throw new IllegalArgumentException("One output expected");
        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException("One Command Expected");
        if (!(tx.getOutput(0) instanceof TokenState))
            throw new IllegalArgumentException("Token State expected");
        //          command type and list of signatures
        if (!(tx.getCommand(0).getValue() instanceof Commands.Issue))
            throw new IllegalArgumentException("Issue Command Expected");

        // need the token state but it gives me the contract state, need to do type casting
        TokenState tokenState = (TokenState) tx.getOutput(0);
        if(tokenState.getAmount() < 1)
            throw new IllegalArgumentException("Positive amt expected");

        if (!(tx.getCommand(0).getSigners().contains(tokenState.getIssuer().getOwningKey())))
            throw new IllegalArgumentException("Issuer signature expected");
    }


    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}
