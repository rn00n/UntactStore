# UntactStore
Un + Contact Store system

https://github.com/rn00n/UntactStore

비대면 예약, 주문 시스템

team : read a dream

# 임시 도메인
http://112.148.5.57:8080/

sample page : 맥도날드 확인

목차
-
개요

환경 / 사용기술 / 도구

주요기능

개요
-
프로젝트 진행 시기에 코로나19 이슈로, 바이러스 확산방지를 위한 비 대면 서비스에 관심이 생겼습니다.

비 대면/비 접촉 서비스를 초점으로 최근 다양한 곳에서 도입되고 있는 키오스크 시스템에 접목시켜서
가게 줄서기부터 입장, 주문 까지의 기능을 가진 웹 앱 시스템을 주제로 선정하고 
프로젝트를 진행하게 되었습니다.

환경 / 사용기술 / 도구
-
java jdk 11

Spring framework

Spring boot

Spring data jpa

PostgreSQL

주요기능 5가지+
-
1. 사이트, QR코드를 이용하여 가게(상점)에 접근 기능
- 직접 방문하지 않아도 가게(상점)페이지에 접근하면 메뉴/평점/리뷰 정보와 ‘현재 대기자 수’를 확인할 수 있어서 시간적인 절약을 할 수 있습니다.
  
2. 대기 줄서기(순번기) 기능
- 만약 가고 싶은 가게(상점)에 자리가 없다면 미리 대기표를 발급받아 가게 앞에 줄을 서있지 않아도 됩니다.

3. 테이블 좌석 착석 요청 기능
- 주문을 하기위해 내가 앉은 테이블 좌석의 권한을 요청해야 합니다.
- 가게 직원이 요청을 수락/취소할 수 있습니다.

4. 주문 기능
- 테이블 권한을 갖은 사용자가 가게에 등록되어 있는 메뉴를 확인하고 
메뉴 또는 요청사항 등을 작성하여 장바구니에 담아 주문할 수 있습니다.

5. 그 외 사용자 편의와 커뮤니케이션, 정보공유를 위한 기능
- 사용자들이 가게(상점)에 대한 객관적인 평가정보를 확인할 수 있도록
  별점(평점)기능과 즐겨찾기, 리뷰게시판 등을 통해 정보를 획득할 수 있습니다.
 - 검색, 알림 기능을 만들어서 편의성을 높였습니다.

