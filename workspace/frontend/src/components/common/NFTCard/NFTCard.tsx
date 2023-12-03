import './NFTCard.scss';
import React, { useState } from 'react';
import useMovePage from 'hooks/useMovePage';

function NFTCard(props: any) {
  const tokenURI = props.uri;
  const { movePage } = useMovePage();
  const [nftImage, setNFTImage] = useState('');
  const [nftName, setNFTName] = useState('');
  const [jsonData, setJsonData] = useState(null);

  fetch(tokenURI)
    .then(response => {
      if (!response.ok) {
        throw new Error('네트워크 에러: ' + response.status);
      }
      return response.json();
    })
    .then(data => {
      setNFTImage(data.image);
      setNFTName(data.name);
      setJsonData(data);
    })
    .catch(error => {
      console.error('데이터 가져오기 실패:', error);
    });

  return (
    <div className="NFTContainer">
      <div className="NFTCard" onClick={() => movePage('/nftdetail', jsonData)}>
        <img src={nftImage} alt="your NFT" />
        <div className="NFTInfo">
          <h4>{nftName}</h4>
        </div>
      </div>
    </div>
  );
}

export default NFTCard;
