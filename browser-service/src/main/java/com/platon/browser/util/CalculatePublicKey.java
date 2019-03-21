package com.platon.browser.util;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 10:52
 */
public class CalculatePublicKey {

    public static String getPublicKey(EthBlock ethBlock) throws Exception {
        String publicKey = testBlock(ethBlock).toString(16);
        // 不足128前面补0
        if(publicKey.length()<128) for (int i=0;i<(128-publicKey.length());i++) publicKey ="0"+publicKey;
        return publicKey;
    }

    public static BigInteger testBlock ( EthBlock tBlock ) throws Exception{

        String extraData = tBlock.getBlock().getExtraData();

        String signature = extraData.substring(extraData.length() - 130 - 64, extraData.length() - 64);

        byte[] msgHash = getMsgHash(tBlock.getBlock());


        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        byte[] r = (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64);


        BigInteger publicKey = Sign.recoverFromSignature((byte) v, new ECDSASignature(new BigInteger(1, r), new BigInteger(1, s)), msgHash);
        return publicKey;
    }

    private static byte[] getMsgHash ( EthBlock.Block block ) {
        byte[] signData = encode(block);
        return Hash.sha3(signData);
    }

    private static byte[] encode ( EthBlock.Block block ) {
        List <RlpType> values = asRlpValues(block);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List <RlpType> asRlpValues ( EthBlock.Block block ) {
        List <RlpType> result = new ArrayList <>();
        //ParentHash  common.Hash    `json:"parentHash"       gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getParentHash())));
        //UncleHash   common.Hash    `json:"sha3Uncles"       gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getSha3Uncles())));
        //Coinbase    common.Address `json:"miner"            gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getMiner())));
        //Root        common.Hash    `json:"stateRoot"        gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getStateRoot())));
        //TxHash      common.Hash    `json:"transactionsRoot" gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getTransactionsRoot())));
        //ReceiptHash common.Hash    `json:"receiptsRoot"     gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getReceiptsRoot())));
        //Bloom       Bloom          `json:"logsBloom"        gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getLogsBloom())));
        //Difficulty  *big.Int       `json:"difficulty"       gencodec:"required"`
        //TODO:0.4.0链去除难度值得解析
        //result.add(RlpString.create(block.getDifficulty()));
        //Number      *big.Int       `json:"number"           gencodec:"required"`
        result.add(RlpString.create(block.getNumber()));
        //GasLimit    uint64         `json:"gasLimit"         gencodec:"required"`
        result.add(RlpString.create(block.getGasLimit()));
        //GasUsed     uint64         `json:"gasUsed"          gencodec:"required"`
        result.add(RlpString.create(block.getGasUsed()));
        //Time        *big.Int       `json:"timestamp"        gencodec:"required"`
        result.add(RlpString.create(block.getTimestamp()));
        //Extra       []byte         `json:"extraData"        gencodec:"required"`
        result.add(RlpString.create(decodeHash(block.getExtraData().substring(0, 66))));

        //MixDigest   common.Hash    `json:"mixHash"`
        result.add(RlpString.create(decodeHash(block.getMixHash())));
        //Nonce       BlockNonce     `json:"nonce"`
        result.add(RlpString.create(decodeHash(block.getNonceRaw())));

        return result;
    }

    static byte[] decodeHash ( String hex ) {
        byte[] result = Hex.decode(Numeric.cleanHexPrefix(hex));
        return result;
    }

}