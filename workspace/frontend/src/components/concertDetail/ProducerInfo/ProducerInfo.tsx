import React from 'react';
import './ProducerInfo.scss';

function ProducerInfo() {
  return (
    <div className="producer-info-container">
      <div className="info-section">
        <h2 className="section-title">상품 관련 정보</h2>
        <div className="info-content">
          <p>
            <span className="info-label">주최 / 주관:</span> ㈜큐브엔터테인먼트
          </p>
          <p>
            <span className="info-label">주최/기획:</span> (주) 씨제이이엔엠
          </p>
          <p>
            <span className="info-label">공연 시간:</span> 해당 없음
          </p>
          <p>
            <span className="info-label">관람 등급:</span> 만 7세 이상
          </p>
          <p>
            <span className="info-label">주연:</span> 해당 없음
          </p>
        </div>
      </div>

      <div className="cancel-info-section">
        <h2 className="section-title">예매 취소 조건</h2>
        <table className="cancel-info-table">
          <thead>
            <tr>
              <th>취소일</th>
              <th>취소 수수료</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>예매 후 7일 이내</td>
              <td>없음</td>
            </tr>
            <tr>
              <td>예매 후 8일~관람일 10일 전까지</td>
              <td>장당 4,000원 (티켓 금액의 10% 한도)</td>
            </tr>
            <tr>
              <td>관람일 9일 전~7일 전까지</td>
              <td>티켓 금액의 10%</td>
            </tr>
            <tr>
              <td>관람일 6일 전~3일 전까지</td>
              <td>티켓 금액의 20%</td>
            </tr>
            <tr>
              <td>관람일 2일 전~1일 전까지</td>
              <td>티켓 금액의 30%</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div className="cancel-method-section">
        <h2 className="section-title">취소 환불 방법</h2>
        <div className="cancel-method-content">
          <p>
            티켓이 배송된 이후에는 인터넷 취소가 안되며, 취소 마감 시간 이전에
            티켓이 Eticket 티켓 고객센터로 반송되어야 취소 가능합니다. 취소
            수수료는 도착 일자 기준으로 부과되며, 배송료는 환불되지 않습니다.
          </p>
        </div>
      </div>

      <div className="delivery-info-section">
        <h2 className="section-title">배송 티켓 안내</h2>
        <div className="delivery-info-content">
          <p>
            티켓 발급 (민팅 상태: 민팅 준비 중 이후) 후에는 지갑 주소 변경이
            불가합니다.
          </p>
        </div>
      </div>

      <div className="mobile-ticket-info-section">
        <h2 className="section-title">모바일 티켓 안내</h2>
        <div className="mobile-ticket-info-content">
          <p>예매하실 경우 티켓 거래는 불가합니다. </p>
          <p>
            Eticket 앱과 Eticket 티켓 앱에서 모바일 티켓을 이용할 수 있습니다.
          </p>
          <p>
            결제 완료(입금 완료) 후 발급받으신 블록체인 티켓을 확인할 수
            있습니다.
          </p>
        </div>
      </div>

      <div className="notice-info-section">
        <h2 className="section-title">예매 유의사항</h2>
        <div className="notice-info-content">
          <p>
            다른 이용자의 원활한 예매 및 취소에 지장을 초래할 정도로 반복적인
            행위를 지속하는 경우 회원의 서비스 이용을 제한할 수 있습니다.
          </p>
        </div>
      </div>
    </div>
  );
}

export default ProducerInfo;
