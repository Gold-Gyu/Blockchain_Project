import { useEffect, useState } from 'react';
import Web3 from 'web3';

const useAccount = () => {
  const [account, setAccount] = useState<string>('');
  const [active, setActive] = useState<boolean>(false);

  const loginMetaMask = () => {
    setActive(!active);
  };

  const getAccount = async () => {
    if (active && account === '') {
      const chainId = '12345';
      try {
        if (!window.ethereum) throw new Error('no metamask here');

        await window.ethereum.request({
          method: 'wallet_addEthereumChain',
          params: [
            {
              chainId: Web3.utils.toHex(parseInt(chainId, 10)),
              chainName: 'Awesome chain',
              rpcUrls: ['https://j9c203.p.ssafy.io/services/blockchain/rpc'],
              iconUrls: [
                'https://xdaichain.com/fake/example/url/xdai.svg',
                'https://xdaichain.com/fake/example/url/xdai.png',
              ],
              nativeCurrency: {
                name: '오영재',
                symbol: '오영재',
                decimals: 18,
              },
              blockExplorerUrls: [
                'https://j9c203.p.ssafy.io/services/blockchain/explorer',
              ],
            },
          ],
        });

        const accounts = await window.ethereum.request({
          method: 'eth_requestAccounts',
        });

        if (accounts && Array.isArray(accounts)) {
          setAccount(accounts[0]);
        }
      } catch (err: any) {
        console.error('err', err.code);
      }
    }
  };

  useEffect(() => {
    getAccount();
  }, [active]);
  return { account, loginMetaMask };
};

export default useAccount;
