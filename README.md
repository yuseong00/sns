# :clapper: SNS 게시판서비스

## :pushpin: 프로젝트 목표 

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: 게시판 서비스 구현과 알람,좋아요 기능을 구현한 백엔드 API를 서버를 구축하는것이 목표입니다.

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: 실행만 되는 서비스가 아닌 대규모 트래픽을 처리할 수 있는 성능적으로 우수한 서비스 제작하는것이 목표입니다.

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: 객체지향 설게에 입각하여 기능 구현 및 대용량 트래픽 처리구현하는것이 목표입니다.

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: Redis와 Kafka를 활용하여 데이터 지연처리시간을 개선하는 것이 목표입니다.

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: Github Actions로 Heroku서버에 배포(CI/CD)하는 것이 목표입니다. 


## :pushpin: 기술적 issue 해결과정 
[#1]대규모 트래픽일시 현 architecture 문제점 분석 및 해결

https://hohojavis.tistory.com/56


[#2]In-Memory-DB 중 Redis를 선택한 이유

https://hohojavis.tistory.com/58
https://hohojavis.tistory.com/62

[#3]kafka를 통한 실시간 업데이트 

https://hohojavis.tistory.com/61

## :pushpin: 프로젝트 중점사항
&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: 버전관리

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: 확장성 있는 시스템을 구현하고자 노력했습니다.

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: 실시간 업데이트를 위해 Kafka를 통한 비동기 처리를 사용하여 페이지 새로 고침 메커니즘을 개선하였습니다.

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: Redis를 활용하여 낮은 지연시간과 빠른 데이터 엑세스와 처리를 높히기 위해 노력했습니다.  

&nbsp;&nbsp;&nbsp;&nbsp;:heavy_check_mark: CI/CD를 적용하고 자동화된 빌드와 배포를 통해 개발의 생산성을 높히기 위해 노력했습니다. 

