package com.platon.FilterTest;

import com.platon.browser.SpringbootApplication;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.filter.BlockFilter;
import com.platon.browser.filter.NodeFilter;
import com.platon.browser.filter.StompPushFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User: dongqile
 * Date: 2019/1/11
 * Time: 15:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= SpringbootApplication.class, value = "spring.profiles.active=test-1")
public class StompPushFilterTest {

    private static Logger logger = LoggerFactory.getLogger(StompPushFilterTest.class);

    @Autowired
    private BlockFilter blockFilter;

    @Autowired
    private StompPushFilter stompPushFilter;

    @Autowired
    private NodeFilter nodeFilter;

    @Autowired
    private Web3jClient web3jClient;

    @Test
    public void StompPushFilterTest(){
        try{
            Web3j web3j = web3jClient.getWeb3jClient();
            CandidateContract candidateContract = web3jClient.getCandidateContract();
            String nodeInfoList = candidateContract.CandidateList().send();
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(1073L)));
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
            List<EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
            List<Transaction> list1 = new ArrayList<>();
            List<TransactionReceipt> list2 = new ArrayList <>();
            for (EthBlock.TransactionResult transactionResult : list) {
                Transaction txList = (Transaction) transactionResult.get();
                EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txList.getHash()).send();
                Optional<Transaction> value = ethTransaction.getTransaction();
                Transaction transaction = value.get();
                list1.add(transaction);
                EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send();
                Optional <TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
                TransactionReceipt receipt = transactionReceipt.get();
                list2.add(receipt);
            }
            Block block = blockFilter.build(ethBlock,list2,list1);
            List<NodeRanking> list3 = nodeFilter.nodeAnalysis(nodeInfoList,block.getNumber(),ethBlock,block.getBlockReward());
            stompPushFilter.stompPush(block,list3);

        }catch (Exception e ){
            e.printStackTrace();
        }
    }


}