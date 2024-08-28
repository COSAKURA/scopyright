package lltw.scopyright;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ScopyrightApplicationTests {
    public final String configFile = "src/main/resources/config-example.toml";

    @Test
    public void add() throws Exception {
        BcosSDK sdk = BcosSDK.build(configFile);

        Client client = sdk.getClient(1);

        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();

        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "");

        String _title = "l";
        String _description = "test";
        String _username = "sakura";
        List<Object> params = new ArrayList<>();
        params.add(_title);
        params.add(_description);
        params.add(_username);

        // 调用智能合约
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                "CopyrightRegistryAndQuery","0x47947ab1b549eed03c9b80d8be90e13f0474de11","registerWork",params);
        List<Object> result = transactionResponse.getReturnObject();
        if(result != null){
            for(Object value : result){
                System.out.println("上链的返回值:"+value.toString());
            }
        }
    }

    @Test
    public void info() throws Exception {
        BcosSDK sdk = BcosSDK.build(configFile);

        Client client = sdk.getClient(1);

        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();

        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "");

        String _title = "a";
        List<Object> params = new ArrayList<>();
        params.add(_title);

        // 调用智能合约
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                "CopyrightRegistryAndQuery","0x47947ab1b549eed03c9b80d8be90e13f0474de11","queryCopyrightByTitle",params);
        List<Object> result = transactionResponse.getReturnObject();
        System.out.println("上链的返回值:"+result);

        if(result != null){
            for(Object value : result){
                System.out.println("上链的返回值:"+value.toString());
            }
        }
    }

    @Test
    public void re() throws Exception {
        BcosSDK sdk = BcosSDK.build(configFile);

        Client client = sdk.getClient(1);

        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();

        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(
                client, keyPair, "src/main/resources/abi/", ""
        );

        String _title = "a";
        boolean _approve = true;
        String _copyrightNumber = "4asdas854asfasfas685asdasd46";
        List<Object> params = new ArrayList<>();
        params.add(_title);
        params.add(_approve);
        params.add(_copyrightNumber);

        // 调用智能合约
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader(
                "CopyrightRegistryAndQuery", "0x47947ab1b549eed03c9b80d8be90e13f0474de11", "reviewWork", params
        );


        List<Object> result = transactionResponse.getReturnObject();
        System.out.println("上链的返回值: " + result);

        if (result != null) {
            for (Object value : result) {
                System.out.println("上链的返回值: " + value.toString());
            }
        } else {
            System.out.println("没有返回值，可能是交易执行失败。");
        }
    }

}
