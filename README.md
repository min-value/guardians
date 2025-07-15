<p align="center">
  <img width="485" height="101" alt="guardians" src="https://github.com/user-attachments/assets/d3139b86-4d5e-4274-8a61-a3df0d6e6854" />
</p>
<br>
<h1>⚾ 스프링MVC 활용 야구예매사이트 구축 - MIN-VALUE ⚾</h1>

> 가상의 구단[신한 가디언즈]을 위한 올인원 플랫폼
> 
> 프로젝트 기간 : 6/16~7/11
<br>
<h2>Team</h2>
<tr>
<table border="1" cellpadding="10" cellspacing="0">
  <thead>
    <tr>
      <th>한주원</th>
      <th>오수빈</th>
      <th>황혜성</th>
      <th>신다운</th>
      <th>최세창</th>
    </tr>
  </thead>
  <tbody>
    <tr>
    <td><p align="center"><img src="https://github.com/user-attachments/assets/b79d75bb-6092-417d-8f17-0e90aebf7c10" height=150px></p></td>
     <td><p align="center"><img src="https://github.com/user-attachments/assets/699b9c1b-ce87-457a-9175-c83bf9714551" height=150px></p></td>
     <td><p align="center"><img src="https://github.com/user-attachments/assets/b12c9f01-1ca9-4953-b19a-bea74913c0c2" height=150px></p></td>
     <td><p align="center"><img src="https://github.com/user-attachments/assets/7d85ab39-1635-41f6-84ce-460e1ea857de" height=150px></p></td>
     <td><p align="center"><img src="https://github.com/user-attachments/assets/38c84cca-015f-4789-8ec4-28646802c88a" height=150px></p></td>
    </tr>
    <tr>
      <td align="center"><a href="https://github.com/chqkq" target="_blank">@chqkq</a></td>
      <td align="center"><a href="https://github.com/subin930" target="_blank">@subin930</a></td>
      <td align="center"><a href="https://github.com/hwang-hyesung" target="_blank">@hwang-hyesung</a></td>
      <td align="center"><a href="https://github.com/noowad-s" target="_blank">@noowad-s</a></td>
      <td align="center"><a href="https://github.com/Sechamm" target="_blank">@Sechamm</a></td>
    </tr>
    <tr>
      <td align="center">팀장, 기획, 프론트, 백</td>
      <td align="center">기획, 프론트, 백</td>
      <td align="center">기획, 프론트, 백</td>
      <td align="center">기획, 프론트, 백</td>
      <td align="center">기획, 프론트, 백</td>
    </tr>
    <tr>
      <td>
        <div>- 커뮤니티 게시판 개발 (게시글 CRUD, 검색, 페이징 구현)</div>
        <div>-글 내용 추천 시스템 개발(OpenAI api 활용)</div>
        <div>-뉴스, 영상 자동 크롤링 시스템 구축 (JSoup, YouTube Data api 활용)</div>
      </td>
      <td>
        <div>- 야구 애매 시스템 구현 (좌석 선택, 선점, 예매 완료 및 예외 처리)</div>
        <div>- redis 기반 좌석 선점 기능 구현</div>
        <div>- redis 기반 예매 대기열 시스템 구현</div>
      </td>
      <td>
        <div>- 관리자, 승리요정, 예매 목록 페이지 개발</div>
        <div>- KG 이니시스 결제 api 연동을 통한 결제 및 환불 프로세스 구현 </div>
        <div>- redis 기반 예매 대기열 시스템 구현</div>
      </td>
      <td>
        <div>- 자체 로그인 및 카카오 로그인 기능 구현 (Kakap api 연동)</div>
        <div>- 메인페이지, 마이페이지 유저 정보 연동, 숫자야구 구성 및 포인트 적립</div>
        <div>- 헤더, 푸터 공통 컴포넌트 개발</div>
      </td>
      <td>
        <div>- 경기 일정, 결과, 순위 데이터 자동 크롤링 시스템 구축 (Jsoup, Selenium 활용)</div>
        <div>- TEAMINFO 페이지 개발을 통한 팀별 상세 정보 시각화</div>
      </td>
    </tr>
  </tbody>
</table>
<br>


<h2>Technology Stack</h2>
<table border="1" cellspacing="0" cellpadding="10">
  <thead>
    <tr>
      <th>구분</th>
      <th>사용 기술</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>FE</td>
      <td>
        <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"/>
        <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"/>
        <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"/>
        <img src="https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=java&logoColor=white"/>
      </td>
    </tr>
    <tr>
      <td>BE</td>
      <td>
        <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
        <img src="https://img.shields.io/badge/Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black"/>
      </td>
    </tr>
    <tr>
      <td>DB & Caching</td>
      <td>
        <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"/>
        <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
      </td>
    </tr>
    <tr>
      <td>Crawling</td>
      <td>
        <img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white"/> 
        <img src="https://img.shields.io/badge/JSoup-2A8FBD?style=for-the-badge&logoColor=white" alt="Jsoup (custom)" />
      </td>
    </tr>
    <tr>
      <td>Infra</td>
      <td>
        <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"/>
      </td>
    </tr>
  </tbody>
</table>
<br>

<h2>Main Functions</h2>
<table>
  <tr>
    <td>
      <h3>01. Reservation</h3>
      <ul>
        <li>포인트 사용</li>
        <li>예매하기 (좌석 선택, 이전 내역 저장)</li>
        <li>승부 예측 (결과에 따른 적립)</li>
        <li>결제 및 예약 취소</li>
      </ul>
    </td>
    <td>
      <h3>02. Community</h3>
      <ul>
        <li>글 쓰기 (AI 내용 추천)</li>
        <li>댓글 쓰기</li>
        <li>글/댓글 수정 및 삭제</li>
        <li>내가 쓴 글 확인</li>
      </ul>
    </td>
    <td>
      <h3>03. News</h3>
      <ul>
        <li>경기 결과 및 일정 확인</li>
        <li>리그 순위 확인</li>
        <li>구단 관련 최근 뉴스</li>
        <li>구단 하이라이트 영상</li>
      </ul>
    </td>
    <td>
      <h3>04. Fairy of Winnings</h3>
      <ul>
        <li>본인 승률 확인</li>
        <li>라플라스 변형 적용</li>
        <li>(승리직관수 + 1) / (총직관수 + 2)</li>
        <li>직관 승률 순위 확인</li>
      </ul>
    </td>
  </tr>
</table>
<br>


<h2>화면 구성</h2>
<table>
  <thead>
    <tr>
      <td align="center">메인 화면</td>
      <td align="center">로그인</td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/30b79b29-a7b3-4d2d-8904-bfb9666b41bc" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/a706a38e-81f9-4ee5-a0d7-8d1f148a7e9d" />
      </td>
    </tr>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">회원가입 페이지</td>
      <td align="center">카카오 추가 정보 페이지</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/8a2d5037-ebe0-4742-8733-507a16edbfe1" />
      </td>
      <td>
        <img src="https://github.com/user-attachments/assets/5c26fa25-e0d3-431a-b61b-79139e1e6182" width=400px>
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">하이라이트</td>
      <td align="center">뉴스 스크랩</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/84d68be2-c34b-4cb8-afb7-bc907d75a3d9" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/696531a7-d9c6-4f18-b5fe-35531e9ee32d" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">구단 소개</td>
      <td align="center">경기장 소개</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/873d47ae-f41e-4ba0-a11f-6cc9ad9e1139" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/56f149cc-1bb3-4293-a8a5-9b8ab1b12a85" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">스폰서</td>
      <td align="center">선수 정보</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/f18238cb-fd89-492c-8b34-08ee4766146b" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/9a0c5778-aebb-41a1-9e92-3b529e7583c7" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">경기 일정</td>
      <td align="center">경기 결과</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/de4eda02-bf8e-436a-bcf4-24983f218b39" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/5e8afa08-859f-43db-85f1-b8bf122e9536" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">순위</td>
      <td align="center">승리 요정</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/7d2a65f7-522e-469d-a85f-ad00200553cc" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/803b6757-2fa9-4243-9a4d-87bc662dd1e9" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">커뮤니티-목록</td>
      <td align="center">커뮤니티-글 상세</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/aba5ccad-1860-41ed-8bb7-344820ee1c39" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/2737beea-4dac-4cf0-b75e-62ed3e464cb9" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">커뮤니티-글 작성</td>
      <td align="center">커뮤니티-글 수정</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/b560ece1-c47e-4028-a77f-67da2e019a40" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/c6c2e87c-6781-4f0e-a51d-284cd781274e" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">예매-목록</td>
      <td align="center">예매-대기열</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/4859c120-7fef-4c25-99ec-9cc021ef07db" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/21ca6755-4101-429e-ad00-0052784792e5" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">예매-등급/좌석 선택(1)</td>
      <td align="center">예매-등급/좌석 선택(2)</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/654e6981-ac69-4f3b-97e1-986b1a6b87d1" />
      </td>
      <td>
        <img width="450"alt="image" src="https://github.com/user-attachments/assets/08e81da3-45f1-4502-9c89-480ae2940b2b" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">예매-권종/할인 선택</td>
      <td align="center">예매-예매 확인</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/e2d046ad-2842-4222-9f97-e20e784cfdbd" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/f729761c-1440-4296-994d-7da0fc859159" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">예매-승부 예측</td>
      <td align="center">마이페이지-내정보</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/6b64a4b5-ebdc-4eda-8290-fe78c2e37544" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/e7ef4e4b-e135-4454-9c9c-97e769507d7d" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">마이페이지-내정보(수정)</td>
      <td align="center">마이페이지-회원탈퇴</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/c010e67c-9595-49ef-b59f-a392ed619be1" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/10ab5afe-9b18-4940-acfc-3e16968e116f" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">마이페이지-예매내역</td>
      <td align="center">마이페이지-포인트내역</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/2ac62492-6642-4a73-848b-8ed423206692" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/1f2bca90-3f31-45d6-806d-406b31fbecbc" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">숫자야구</td>
      <td align="center">마이페이지-승리요정</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/10905aef-e786-48e8-817c-6f68e407551d" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/1fc8b258-cf8b-48bd-9dac-7e4bab94e796" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">관리자페이지-메인</td>
      <td align="center">관리자페이지-예매목록조회</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/ac42e9a1-f867-4f89-9291-80142732d48f" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/19af18bc-47fc-4efa-b8f6-33b61c90b6fa" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">관리자페이지-경기목록조회</td>
      <td align="center">관리자페이지-경기예매등록</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/117a8fb4-994b-4e25-bc16-f0e0ff8beb9b" />
      </td>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/394001fa-24d8-4b18-ab89-5d9648b5dcd6" />
      </td>
  </tbody>
</table>
<table>
  <thead>
    <tr>
      <td align="center">관리자페이지-경기예매등록(상세)</td>
    </tr>
  </thead>
  <tbody>
      <td>
        <img width="450" alt="image" src="https://github.com/user-attachments/assets/b0810203-3328-4eeb-b4d1-90bdaa83e7ae" />
      </td>
  </tbody>
</table>
