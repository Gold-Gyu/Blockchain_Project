import React, { useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
// import DetailImg from 'assets/emphisDetail.svg';
import ImgDetail from 'components/concertDetail/ImgDetail/ImgDetail';
import './DetailContent.scss';
// import { useParams } from 'react-router-dom';
import ProducerInfo from 'components/concertDetail/ProducerInfo/ProducerInfo';
import PriceInfo from '../PriceInfo/PriceInfo';

const tabContentStyles = {
  textAlign: 'center', // 가운데 정렬
  padding: '20px', // 내부 여백
  fontSize: '1.2rem', // 폰트 크기
};

/** params 공연정보 전체가져와서
 *  공연정보, 공연기간, 공연장, 관람시간
 *  좌석 레벨 and 좌석 가격
 *  + 상세정보 이미지
 */
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default function DetailContent({ info }: { info: any }) {
  // const { performanceId } = useParams();
  const [value, setValue] = React.useState(0);
  const [showAnswer, setShowAnswer] = useState(Array(4).fill(false)); // 각 질문마다 보이기 여부 배열

  const toggleAnswer = (index: number) => {
    const newShowAnswer = [...showAnswer];
    newShowAnswer[index] = !newShowAnswer[index];
    setShowAnswer(newShowAnswer);
  };

  const handleChange = (_event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Box
      sx={{
        // width: '100%', // 100% 너비로 설정
        // maxWidth: '450px', // 최대 너비 450px
        margin: '0 auto', // 가운데 정렬
        position: 'relative',
        // minHeight: '100vh', // 화면 높이에 맞게 조절
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'flex-start',
        alignItems: 'center',
        // padding: '50px 0px',
        gap: '20px',
      }}
    >
      <AppBar position="static" color="default">
        <Tabs
          value={value}
          onChange={handleChange}
          indicatorColor="primary"
          textColor="primary"
          variant="fullWidth"
          aria-label="action tabs example"
          sx={{
            // backgroundColor: 'lightgray', // 탭 바의 배경색
            borderRadius: '15px', // 탭 바의 둥근 모서리
          }}
        >
          <Tab
            label="공연정보"
            sx={{
              borderRadius: '15px 0 0 0', // 첫 번째 탭의 왼쪽 모서리만 둥글게
            }}
          />
          <Tab label="공연장정보" />
          <Tab
            label="QnA"
            sx={{
              borderRadius: '0 15px 0 0', // 마지막 탭의 오른쪽 모서리만 둥글게
            }}
          />
        </Tabs>
      </AppBar>
      <Box
        role="tabpanel"
        hidden={value !== 0}
        id="tabpanel-0"
        aria-labelledby="tab-0"
        sx={{ width: '100%' }} // 중괄호로 감싸기
      >
        {/* 공연정보 컨텐츠 */}
        <div>
          <div className="notice-text">NOTICE</div>
          <div>
            <ImgDetail descImgUrl={info.detailImagePath} />
            <PriceInfo />
          </div>
          {/* 여기에 공연정보 컨텐츠 내용을 추가 */}
        </div>
      </Box>
      <Box
        role="tabpanel"
        hidden={value !== 1}
        id="tabpanel-1"
        aria-labelledby="tab-1"
        sx={{ textalign: 'center' }}
      >
        <div>
          <ProducerInfo />
        </div>
      </Box>
      <Box
        role="tabpanel"
        hidden={value !== 2}
        id="tabpanel-2"
        aria-labelledby="tab-2"
        sx={tabContentStyles}
      >
        {/* QnA 컨텐츠 */}
        <div className="qna-container">
          <h2 className="section-title">QnA</h2>
          <div className="qna-item">
            <div
              className="qna-question"
              onClick={() => toggleAnswer(0)}
              aria-hidden
            >
              Q: 환불은 언제까지 가능한가요?
            </div>
            {showAnswer[0] && (
              <div className="qna-answer">
                A: 콘서트 시작 하루전까지 가능합니다.
              </div>
            )}
          </div>
          <div className="qna-item">
            <div
              className="qna-question"
              onClick={() => toggleAnswer(1)}
              aria-hidden
            >
              Q: 포토카드 거래가 가능한가요?
            </div>
            {showAnswer[1] && (
              <div className="qna-answer">
                A: 네! 포토카드는 언제 어디서든 거래가 가능합니다.
              </div>
            )}
          </div>
          <div className="qna-item">
            <div
              className="qna-question"
              onClick={() => toggleAnswer(2)}
              aria-hidden
            >
              Q: 티켓 거래가 가능한가요?
            </div>
            {showAnswer[2] && (
              <div className="qna-answer">
                A: 아니오. 거래는 불가능합니다. 콘서트 하루전까지 환불만
                가능합니다.
              </div>
            )}
          </div>
          <div className="qna-item">
            <div
              className="qna-question"
              onClick={() => toggleAnswer(3)}
              aria-hidden
            >
              Q: 포토카드는 언제 생성되나요?
            </div>
            {showAnswer[3] && (
              <div className="qna-answer">
                A: 콘서트 종료 후에 티켓이 NFT 포토카드로 바뀝니다.
              </div>
            )}
          </div>

          {/* 나머지 Q&A 항목들도 동일한 구조로 추가 */}
        </div>
      </Box>
    </Box>
  );
}
