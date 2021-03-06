/**
 * Copyright: Copyright 2016-2020 JD.COM All Right Reserved
 * FileName: test.com.jd.blockchain.sdk.test.SDK_GateWay_InsertData_Test
 * Author: shaozhuguang
 * Department: 区块链研发部
 * Date: 2018/9/4 上午11:06
 * Description: 插入数据测试
 */
package test.com.jd.blockchain.sdk.test;

import org.junit.Before;
import org.junit.Test;

import com.jd.blockchain.binaryproto.DataContractRegistry;
import com.jd.blockchain.crypto.AsymmetricKeypair;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.crypto.HashFunction;
import com.jd.blockchain.crypto.SignatureFunction;
import com.jd.blockchain.ledger.BlockchainKeyGenerator;
import com.jd.blockchain.ledger.BlockchainKeypair;
import com.jd.blockchain.ledger.EndpointRequest;
import com.jd.blockchain.ledger.NodeRequest;
import com.jd.blockchain.ledger.PreparedTransaction;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionContentBody;
import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionResponse;
import com.jd.blockchain.ledger.TransactionState;
import com.jd.blockchain.ledger.TransactionTemplate;
import com.jd.blockchain.ledger.data.TxResponseMessage;
import com.jd.blockchain.sdk.BlockchainService;
import com.jd.blockchain.sdk.client.GatewayServiceFactory;

/**
 * 插入数据测试
 * @author shaozhuguang
 * @create 2018/9/4
 * @since 1.0.0
 */

public class SDK_GateWay_DataAccount_Test_ {

    private BlockchainKeypair CLIENT_CERT = null;

    private String GATEWAY_IPADDR = null;

    private int GATEWAY_PORT;

    private boolean SECURE;

    private BlockchainService service;

    @Before
    public void init() {
        CLIENT_CERT = new BlockchainKeypair(SDK_GateWay_KeyPair_Para.pubKey0, SDK_GateWay_KeyPair_Para.privkey0);
        GATEWAY_IPADDR = "127.0.0.1";
        GATEWAY_PORT = 8081;
        SECURE = false;
        GatewayServiceFactory serviceFactory = GatewayServiceFactory.connect(GATEWAY_IPADDR, GATEWAY_PORT, SECURE,
                CLIENT_CERT);
        service = serviceFactory.getBlockchainService();

        DataContractRegistry.register(TransactionContent.class);
        DataContractRegistry.register(TransactionContentBody.class);
        DataContractRegistry.register(TransactionRequest.class);
        DataContractRegistry.register(NodeRequest.class);
        DataContractRegistry.register(EndpointRequest.class);
        DataContractRegistry.register(TransactionResponse.class);
    }

    @Test
    public void registerDataAccount_Test() {
//        HashDigest ledgerHash = getLedgerHash();
        HashDigest[] ledgerHashs = service.getLedgerHashs();
        // 在本地定义注册账号的 TX；
        TransactionTemplate txTemp = service.newTransaction(ledgerHashs[0]);

//        SignatureFunction signatureFunction = getSignatureFunction();
//
//        CryptoKeyPair cryptoKeyPair = signatureFunction.generateKeyPair();

        //existed signer
        AsymmetricKeypair keyPair = new BlockchainKeypair(SDK_GateWay_KeyPair_Para.pubKey1, SDK_GateWay_KeyPair_Para.privkey1);

        BlockchainKeypair dataAcount = BlockchainKeyGenerator.getInstance().generate();

        // 注册
        txTemp.dataAccounts().register(dataAcount.getIdentity());

        // TX 准备就绪；
        PreparedTransaction prepTx = txTemp.prepare();

        prepTx.sign(keyPair);

        // 提交交易；
        TransactionResponse transactionResponse = prepTx.commit();

//        // 期望返回结果
//        TransactionResponse expectResp = initResponse();
//
//        System.out.println("---------- assert start ----------");
//        assertEquals(expectResp.isSuccess(), transactionResponse.isSuccess());
//        assertEquals(expectResp.getExecutionState(), transactionResponse.getExecutionState());
//        assertEquals(expectResp.getContentHash(), transactionResponse.getContentHash());
//        assertEquals(expectResp.getBlockHeight(), transactionResponse.getBlockHeight());
//        assertEquals(expectResp.getBlockHash(), transactionResponse.getBlockHash());
//        System.out.println("---------- assert OK ----------");
    }

    private HashDigest getLedgerHash() {
    	HashFunction hashFunc = Crypto.getHashFunction("SHA256");;
        HashDigest ledgerHash =hashFunc.hash("jd-gateway".getBytes());
        return ledgerHash;
    }

    private SignatureFunction getSignatureFunction() {
        return Crypto.getSignatureFunction("ED25519");
    }

    private AsymmetricKeypair getSponsorKey() {
		return getSignatureFunction().generateKeypair();
	}

    private TransactionResponse initResponse() {
    	HashFunction hashFunc = Crypto.getHashFunction("SHA256");;
        HashDigest contentHash = hashFunc.hash("contentHash".getBytes());
        HashDigest blockHash =  hashFunc.hash("blockHash".getBytes());
        long blockHeight = 9998L;

        TxResponseMessage resp = new TxResponseMessage(contentHash);
        resp.setBlockHash(blockHash);
        resp.setBlockHeight(blockHeight);
        resp.setExecutionState(TransactionState.SUCCESS);
        return resp;
    }
}