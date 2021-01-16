package com.platon.browser.decoder;

import com.alaya.protocol.core.methods.response.Log;
import com.alaya.rlp.solidity.RlpEncoder;
import com.alaya.rlp.solidity.RlpList;
import com.alaya.rlp.solidity.RlpString;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-21 16:18:55
 **/
public class PPOSTxDecodeUtilTest {

    @Test
    public void test(){
        // REPORT
    	PPOSTxDecodeUtil.decode("0xf9065e83820bb801b90656b906537b227072657061726541223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861656238656262643035386466326364313962633235613737396534643862383337343338336236303530613464386163626238306232353461343839343030222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307832643764313433663531343262383665376535306137356465333234626163393438323333663862326537663063383966316139353430656333613533636661222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230783365626161633566643634636236363266623030353634656562326335376130666662303336383539663134326133653837653663373333663533656537373335316139353462363266313035643430623435306635633763346431323538653030303030303030303030303030303030303030303030303030303030303030227d2c227072657061726542223a7b2265706f6368223a302c22766965774e756d626572223a302c22626c6f636b48617368223a22307861616361323563663862386634373737373535626164333235363337653433386361393234633062316463323931623164666561363431376266303632323730222c22626c6f636b4e756d626572223a31383939302c22626c6f636b496e646578223a302c22626c6f636b44617461223a22307833646432353035343736643234386466633461656266613139653561623733373065636130383863616437323936393164386138373431313531386536363037222c2276616c69646174654e6f6465223a7b22696e646578223a302c2261646472657373223a22307863666535316438356639393635663664303331653465336363653638386561623763393565393430222c226e6f64654964223a226266633964363537386261623465353130373535353735653437623764313337666366306164306263663130656434643032333634306466623431623139376239663064383031346534376563626534643531663135646235313430303963626461313039656263663062376166653036363030643664343233626237666266222c22626c735075624b6579223a22623437313337393764323936633966653137343964323265623539623033643936393461623839366237313434396230653664616632643165636233613964336436653963323538623337616362326430376661383262636235356365643134346662346230353664366364313932613530393835393631356230393031323864366535363836653834646634373935316531373831363235363237393037303534393735663736653432376461386433326433663330623961353365363066227d2c227369676e6174757265223a2230786261316335363536386439376437323430393736303133383135663363333235613137356461373761393136323463333462386532646266616464643161636165313239386461316534303363396363336638303735346533376633336630653030303030303030303030303030303030303030303030303030303030303030227d7d",null);
        // STAKE_EXIT
    	PPOSTxDecodeUtil.decode("0xf848838203ebb842b840bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf",null);
        // STAKE_MODIFY
    	PPOSTxDecodeUtil.decode("0xf897838203e9959460ceca9c1290ee56b98d4e160ef0453f7c40d219b842b8400aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7838201f4919035464436384236393030313036333242888763646d2d3030348c8b5757572e4343432e434f4d8c8b4e6f6465206f662043444d",null);
        // PROPOSAL_VOTE
    	PPOSTxDecodeUtil.decode("0xf8b4838207d3b842b840459d199acb83bfe08c26d5c484cbe36755b53b7ae2ea5f7a5f0a8f4c08e843b51c4661f3faa57b03b710b48a9e17118c2659c5307af0cc5329726c13119a6b85a1a0f9fb1d9f64ec2573cad0cec662ec1ffcf7207d8c2ebac56d13b5e1ef0a2555f30183820703b843b841c943f49857fa2912c4bafd5217bf9e1ccb646a821cb516da014d4b38913a69dd786343926ce377686914e01760bd546478f63828559ef1124dbf297aff2124e401",null);
        // RESTRICTING_CREATE
    	PPOSTxDecodeUtil.decode("0xf683820fa0959460ceca9c1290ee56b98d4e160ef0453f7c40d2199bdacc8203e8884563918244f40000cc8207d0880853a0d2313c0000",null);
        // PROPOSAL_VOTE
    	PPOSTxDecodeUtil.decode("0xf8b4838207d3b842b840459d199acb83bfe08c26d5c484cbe36755b53b7ae2ea5f7a5f0a8f4c08e843b51c4661f3faa57b03b710b48a9e17118c2659c5307af0cc5329726c13119a6b85a1a009ffb5916c2f40f86ab3d395957fb6b0d5881be5e61fe20c408b4300a811f2320183820703b843b841c943f49857fa2912c4bafd5217bf9e1ccb646a821cb516da014d4b38913a69dd786343926ce377686914e01760bd546478f63828559ef1124dbf297aff2124e401",null);
        // STAKE_CREATE
    	PPOSTxDecodeUtil.decode("0xf901a1838203e88180959460ceca9c1290ee56b98d4e160ef0453f7c40d219b842b840bfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf9190354644363842363930303130363332428a897a726a2d6e6f6465318e8d7777772e62616964752e636f6d96956368656e6461692d6e6f6465312d64657461696c738c8b108b2a2c2802909400000083820703b843b841db3b6fc83e683dd3ce915e691f6095ebfef951c0828250ca2c7a5eebc6eed92a6531123b17f8d987460890a470a009aab522ac7ceb311f2367dae9aec6466baf00b862b860b4713797d296c9fe1749d22eb59b03d9694ab896b71449b0e6daf2d1ecb3a9d3d6e9c258b37acb2d07fa82bcb55ced144fb4b056d6cd192a509859615b090128d6e5686e84df47951e1781625627907054975f76e427da8d32d3f30b9a53e60fb842b8409b40eb38ee734640d38e433fde9c075ae123d6238e450c0b8437ef3ece9e4e6378c0f54db1105ee0045baecf4a9cad4e8c1d4d5e8c91c42c4bbd61fd87ec4d52",null);
        // STAKE_INCREASE
    	PPOSTxDecodeUtil.decode("0xf857838203eab842b8400aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e781808c8b0422ca8b0a00a425000000",null);
        // DELEGATE_CREATE
        PPOSTxDecodeUtil.decode("0xf854838203ec8180b842b84077fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050898898a7d9b8314c0000",null);
        // DELEGATE_EXIT
        PPOSTxDecodeUtil.decode("0xf858838203ed83820d70b842b84077fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c820554990508b8a69e10de76676d0800000",null);
        // PROPOSAL_CANCEL
        PPOSTxDecodeUtil.decode("0xf86f838207d5b842b8400aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e78382313101a1a0d62f8b78e95841edddd69970f5f8a6fefd837923af6ed41f8526991eb5f56297",null);
        // PROPOSAL_TEXT
        PPOSTxDecodeUtil.decode("0xf84c838207d0b842b840ff40ac420279ddbe58e1bf1cfe19f4b5978f86e7c483223be26e80ac9790e855cb5d7bd743d94b9bd72be79f01ee068bc1fefe79c06ba9cd49fa96f52c7bdce083827334",null);
        // PROPOSAL_UPGRADE
        PPOSTxDecodeUtil.decode("0xf84f838207d1b842b8404cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b20838848301000014",null);
        // VERSION_DECLARE
        PPOSTxDecodeUtil.decode("0xf891838207d4b842b8400aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e783820703b843b8414a04fd81a170cf4e3c5c6876b73907fe51eb82275dd3f112ce1487ecbab9e43a285e00301e6ece412edcd3efaec0566486afc3315e70ad84ccb2001a32c3c36d00",null);
        // PROPOSAL_PARAMETER
        PPOSTxDecodeUtil.decode("0xf88f838207d2b842b8404cc7be9ec01466fc4f14365f6700da36f3eb157473047f32bded7b1c0c00955979a07a8914895f7ee59af9cb1e6b638aa57c91a918f7a84633a92074f286b2089291313537353336363034352e36303934393988877374616b696e678f8e7374616b655468726573686f6c649a9931353030303030303030303030303030303030303030303030",null);
        // CLAIM_REWARD
        byte[] data = RlpEncoder.encode(new RlpList(RlpString.create(RlpEncoder.encode(RlpString.create(5000)))));
        String txInput = Hex.toHexString(data);

        List<Log> logs = new ArrayList<>();
        Log log = new Log();
        log.setData("0xf84e30b84bf849f847b840362003c50ed3a523cdede37a001803b8f0fed27cb402b3d6127a1a96661ec202318f68f4c76d9b0bfbabfd551a178d4335eaeaa9b7981a4df30dfc8c0bfe3384830f424064");
        logs.add(log);
        PPOSTxDecodeUtil.decode(txInput,logs);
        
        TxInputDecodeResult g = TxInputDecodeUtil.decode("0x0061736d1");
        assertEquals(g.getTypeEnum(), Transaction.TypeEnum.WASM_CONTRACT_CREATE);
        g.getParam().toJSONString();
        TxInputDecodeUtil.decode("0x0060808");
//
//        String inputData = "0x60c0604052600460808190527f48302e310000000000000000000000000000000000000000000000000000000060a090815261003e91600691906100d0565b5034801561004b57600080fd5b506040516109ab3803806109ab8339810160409081528151602080840151838501516060860151336000908152600185529586208590559484905590850180519395909491939101916100a3916003918601906100d0565b506004805460ff191660ff841617905580516100c69060059060208401906100d0565b505050505061016b565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061011157805160ff191683800117855561013e565b8280016001018555821561013e579182015b8281111561013e578251825591602001919060010190610123565b5061014a92915061014e565b5090565b61016891905b8082111561014a5760008155600101610154565b90565b6108318061017a6000396000f3006080604052600436106100955763ffffffff60e060020a60003504166306fdde0381146100a7578063095ea7b31461013157806318160ddd1461016957806323b872dd14610190578063313ce567146101ba57806354fd4d50146101e557806370a08231146101fa57806395d89b411461021b578063a9059cbb14610230578063cae9ca5114610254578063dd62ed3e146102bd575b3480156100a157600080fd5b50600080fd5b3480156100b357600080fd5b506100bc6102e4565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100f65781810151838201526020016100de565b50505050905090810190601f1680156101235780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561013d57600080fd5b50610155600160a060020a0360043516602435610372565b604080519115158252519081900360200190f35b34801561017557600080fd5b5061017e6103d9565b60408051918252519081900360200190f35b34801561019c57600080fd5b50610155600160a060020a03600435811690602435166044356103df565b3480156101c657600080fd5b506101cf6104cc565b6040805160ff9092168252519081900360200190f35b3480156101f157600080fd5b506100bc6104d5565b34801561020657600080fd5b5061017e600160a060020a0360043516610530565b34801561022757600080fd5b506100bc61054b565b34801561023c57600080fd5b50610155600160a060020a03600435166024356105a6565b34801561026057600080fd5b50604080516020600460443581810135601f8101849004840285018401909552848452610155948235600160a060020a031694602480359536959460649492019190819084018382808284375094975061063f9650505050505050565b3480156102c957600080fd5b5061017e600160a060020a03600435811690602435166107da565b6003805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b820191906000526020600020905b81548152906001019060200180831161034d57829003601f168201915b505050505081565b336000818152600260209081526040808320600160a060020a038716808552908352818420869055815186815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a35060015b92915050565b60005481565b600160a060020a038316600090815260016020526040812054821180159061042a5750600160a060020a03841660009081526002602090815260408083203384529091529020548211155b80156104365750600082115b156104c157600160a060020a03808416600081815260016020908152604080832080548801905593881680835284832080548890039055600282528483203384528252918490208054879003905583518681529351929391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a35060016104c5565b5060005b9392505050565b60045460ff1681565b6006805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b600160a060020a031660009081526001602052604090205490565b6005805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b3360009081526001602052604081205482118015906105c55750600082115b156106375733600081815260016020908152604080832080548790039055600160a060020a03871680845292819020805487019055805186815290519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060016103d3565b5060006103d3565b336000818152600260209081526040808320600160a060020a038816808552908352818420879055815187815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a383600160a060020a031660405180807f72656365697665417070726f76616c28616464726573732c75696e743235362c81526020017f616464726573732c627974657329000000000000000000000000000000000000815250602e019050604051809103902060e060020a9004338530866040518563ffffffff1660e060020a0281526004018085600160a060020a0316600160a060020a0316815260200184815260200183600160a060020a0316600160a060020a03168152602001828051906020019080838360005b8381101561077f578181015183820152602001610767565b50505050905090810190601f1680156107ac5780820380516001836020036101000a031916815260200191505b509450505050506000604051808303816000875af19250505015156107d057600080fd5b5060019392505050565b600160a060020a039182166000908152600260209081526040808320939094168252919091522054905600a165627a7a723058206e603c634e6d2ac6f989b2e2055cfbca683a30ca87aac8e0a9a6bb65e858050e0029000000000000000000000000000000000000000000000000000009184e72a0000000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000c000000000000000000000000000000000000000000000000000000000000000084d7920546f6b656e00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000024d54000000000000000000000000000000000000000000000000000000000000";
//        InnerContractDecodeUtil.decode(inputData,null);

        assertTrue(true);
    }

}
