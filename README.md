<p align="center">
  <img width="101" height="101" alt="image" src="https://github.com/user-attachments/assets/7006ff29-8837-4ea6-82ee-4142d17aff1f" />
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
    <td><p align="center"><img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/d778998c-da58-41b0-8823-068d7066926e" />
</p></td>
     <td><p align="center"><img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/d6cf4aee-687b-414a-bf6c-76513fdd947b" /></p></td>
     <td><p align="center"><img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/922ba9e6-2921-4061-82b3-bc01697bc21b" />
</p></td>
     <td><p align="center"><img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/cda8cf76-5d29-4d60-b282-142e77d70901" />
</p></td>
     <td><p align="center"><img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/237e83d4-d706-43c3-a49d-73a39ec5005f" />
</p></td>
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
  <tbody>
    <tr>
      <td>
        <img width="7000" height="2372" alt="main   login" src="https://github.com/user-attachments/assets/e61887e0-8f1e-462f-8fba-d0a07618d001" />
      </td>
    </tr>
    <tr>
      <td>
        <img width="7000" height="2586" alt="mypage" src="https://github.com/user-attachments/assets/f88ef1fa-d746-4cab-bcd7-7c6559211cdf" />
      </td>
    </tr>
    <tr>
      <td>
        <img width="7000" height="2654" alt="story" src="https://github.com/user-attachments/assets/801a6f96-b746-4db9-8ea9-8b6f708e1a3a" />
      </td>
    </tr>
    <tr>
      <td>
        <img width="7000" height="3055" alt="Game" src="https://github.com/user-attachments/assets/3e0689db-78d2-4747-9550-87592fed3167" />
      </td>
    </tr>
    <tr>
      <td>
        <img width="7000" height="2294" alt="community" src="https://github.com/user-attachments/assets/8d373162-0d11-4027-90a6-b74ea2352141" />
      </td>
    </tr>
    <tr>
      <td>
        <img width="7000" height="2650" alt="fairy   numball   errorPage" src="https://github.com/user-attachments/assets/9722c282-c191-4d77-862c-686cc2ae808a" />
      </td>
    </tr>
    <tr>
      <td>
        <img width="7000" height="3989" alt="reservation" src="https://github.com/user-attachments/assets/1c415824-c54d-4299-a636-f192b196a4da" />
      </td>
    </tr>
    <tr>
      <td>
        <img width="7000" height="2141" alt="admin" src="https://github.com/user-attachments/assets/d1c0fbe7-d5ee-41da-845b-ab040f36af97" />
      </td>
    </tr>
  </tbody>
</table>
<table>
