import { useState } from 'react';
import Web3 from 'web3';
import EticketJSON from '../contracts/Eticket.json';

const useMetaData = () => {
  const [metadata, setMetaData] = useState<any>([]);
  const web3 = new Web3(window.ethereum);
  const eticketJSON: any = EticketJSON;

  const connectIPFS = async (address:string, tokenId: string) => {
    // 트랜젝션 해쉬값 입력 필요
    const _transactionHash: string = "0xd4f03d4a344ee5845e7a0d151f7fed26790c9de8db294e195820c800743e956c"

    const block = await window.ethereum.request({
      "method": "eth_getTransactionReceipt",
      "params": [
        _transactionHash
      ]
    })
    try {
      const contract = new web3.eth.Contract(eticketJSON.abi, block.contractAddress);
      contract.methods
        .tokenURI(tokenId)
        .call({
          from: address,
        })
        .then(async (res: any) => {
          let copy = [...metadata]
          copy.push(res)
          setMetaData(copy)
        });
    } catch (e) {
      console.error(e);
    }
  };

  return {
    metadata,
    connectIPFS,
  };
};

export default useMetaData;