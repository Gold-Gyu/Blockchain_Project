import { useEffect, useState } from 'react';

interface MetaMaskInpageProvider {
  request(options: { method: string }): Promise<string[]>;
  isMetaMask: boolean;
}

const useMetaMask = () => {
  const [provider, setProvider] = useState<MetaMaskInpageProvider | null>(null);
  const [accounts, setAccounts] = useState<string[]>([]);

  const detectMetaMask = () => {
    if (window.ethereum) {
      if ('isMetaMask' in window.ethereum && window.ethereum.isMetaMask) {
        setProvider(window.ethereum);
      }
    }
  };

  const requestAccounts = async () => {
    try {
      const accounts = await provider?.request({
        method: 'eth_requestAccounts',
      });
      if (accounts) {
        setAccounts([...accounts]);
      }
    } catch (error) {
      console.error('err', error);
    }
  };

  useEffect(() => {
    detectMetaMask();
  }, []);

  return { provider, accounts, detectMetaMask, requestAccounts };
};

export default useMetaMask;
