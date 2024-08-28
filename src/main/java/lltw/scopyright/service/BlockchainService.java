package lltw.scopyright.service;

import lltw.scopyright.fisco.CopyrightRegistryAndQuery;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple10;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;

/**
 * @author Sakura
 */
@Service
@Slf4j
public class BlockchainService {

    private final Client client;
    private final CryptoKeyPair keyPair;
    private final CopyrightRegistryAndQuery copyrightRegistryAndQuery;

    // Replace with your deployed contract address
    private static final String CONTRACT_ADDRESS = "0x47947ab1b549eed03c9b80d8be90e13f0474de11";

    public BlockchainService() {
        String configFile = new File("src/main/resources/config-example.toml").getAbsolutePath();
        BcosSDK sdk = BcosSDK.build(configFile);
        this.client = sdk.getClient(1);
        this.keyPair = client.getCryptoSuite().createKeyPair();

        // Initialize the contract instance with the deployed address
        this.copyrightRegistryAndQuery = CopyrightRegistryAndQuery.load(CONTRACT_ADDRESS, client, keyPair);
    }

    public TransactionReceipt registerWork(String title, String description, String username) {
        return callContractFunction(() -> copyrightRegistryAndQuery.registerWork(title, description, username));
    }

    public TransactionReceipt reviewWork(String title, boolean approve, String copyrightNumber) {
        return callContractFunction(() -> copyrightRegistryAndQuery.reviewWork(title, approve, copyrightNumber));
    }

    public Tuple10<BigInteger, String, String, String, String, Boolean, BigInteger, BigInteger, BigInteger, String> queryCopyrightByTitle(String title) {
        try {
            log.info("Querying copyright for title: {}", title);
            Tuple10<BigInteger, String, String, String, String, Boolean, BigInteger, BigInteger, BigInteger, String> result = copyrightRegistryAndQuery.queryCopyrightByTitle(title);
            log.info("Query result: {}", result);
            return result;
        } catch (ContractException e) {
            log.error("Error querying copyright information for title {}: {}", title, e.getMessage());
            return null;
        }
    }

    private TransactionReceipt callContractFunction(ContractFunctionCall call) {
        try {
            TransactionReceipt receipt = call.execute();
            System.out.println("Transaction hash: " + receipt.getTransactionHash());
            return receipt;
        } catch (ContractException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FunctionalInterface
    private interface ContractFunctionCall {
        TransactionReceipt execute() throws ContractException;
    }
}
