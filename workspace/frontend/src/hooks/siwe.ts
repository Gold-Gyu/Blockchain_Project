import axios, { AxiosRequestConfig } from 'axios';
import { useNavigate } from 'react-router-dom';
import instance from 'apis/utils/instance';
import { SiweMessage } from 'siwe';
import { BrowserProvider, ethers } from 'ethers';
import useMetaMask from './metamask';

const siwe = () => {
  const navigate = useNavigate();
  const metaMask = useMetaMask();

  const domain = 'localhost';
  const origin = 'https://localhost/login';

  const getSIWE = async () => {
    const challenge = await instance.post('/api/auth/challenge');

    const nonce = challenge.data.challengeWord;

    const browserProvider = new BrowserProvider(metaMask.provider as any);
    const signer = await browserProvider?.getSigner(metaMask.accounts[0]);
    const address = await signer.getAddress();
    const siweMessage = new SiweMessage({
      domain,
      address,
      uri: origin,
      version: '1',
      chainId: 12345,
      nonce,
    }).prepareMessage();

    const signature = await signer?.signMessage(siweMessage);

    const requestData = {
      account: address.toLowerCase(),
      challenge: challenge.data.challengeWordId,
      siweMessage: ethers.hexlify(ethers.toUtf8Bytes(siweMessage)),
      personalSign: signature,
    };

    const config: AxiosRequestConfig = {
      method: 'post',
      url: '/api/auth/signin',
      headers: {
        'X-Authentication-Strategy': 'personal-sign',
      },
      data: requestData,
    };

    await axios(config)
      .then(response => {
        localStorage.setItem('accesstoken', response.data.accessToken);
        localStorage.setItem('refreshtoken', response.data.refreshToken);
        navigate('/');
      })
      .catch(error => {
        console.error('err', error);
      });
  };
  return { getSIWE };
};

export default siwe;
