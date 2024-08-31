| 최은소 (INFRA) | 오예린 (INFRA) | 권민정 (BE) | 선우예림 (BE) | 황지민 (BE) |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://avatars.githubusercontent.com/u/93801149?v=4" alt="최은소 프로필" width="180" height="180"> | <img src="https://avatars.githubusercontent.com/YelynnOh" alt="오예린 프로필" width="180" height="180"> | <img src="https://avatars.githubusercontent.com/u/145860909?v=4" alt="권민정 프로필" width="180" height="180"> | <img src="https://avatars.githubusercontent.com/u/54367532?v=4" alt="선우예림 프로필" width="180" height="180"> | <img src="https://avatars.githubusercontent.com/u/88023963?v=4" alt="황지민 프로필" width="180" height="180"> |
| [esc-beep](https://github.com/esc-beep) | [YelynnOh](https://github.com/YelynnOh) | [mjttong](https://github.com/mjttong) | [yerimsw](https://github.com/yerimsw) | [Jimin-Hwang00](https://github.com/Jimin-Hwang00) |

# 프로젝트 소개

## 파일 공유 시스템 **설계**

- 파일을 업로드하고 링크를 통해 공유할 수 있는 기능을 구현한다.
- 업로드된 파일의 메타데이터를 처리하여 별도로 저장한다.

**요구 조건**

- 매일 약 100-200개의 새로운 파일 업로드 예상
- 파일 당 평균 크기는 50MB 예상
- 최대 50GB의 파일 저장 용량 필요
- 파일 업/다운로드 지연 시간은 1분 이내

**심화 조건**

> 심화 조건은 기본 요구 조건을 모두 만족한 후 생각해볼만한 추가적인 구현에 대한 내용입니다.
> 
- 대용량 파일 전송: 최대 1GB 크기의 파일 공유 링크 생성 기능
- 접근 제어: 파일 및 폴더 단위의 세부적인 접근 권한 설정 기능
- 버전 관리: 주요 파일에 대한 최대 5개 버전 히스토리 관리
- 파일 검색: 파일명, 내용 기반 검색 기능 제공 (검색 결과 응답 시간 5초 이내)

# 유저 시나리오

![유저 시나리오 -Version5.drawio.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/388131ba-2bfe-46d3-bbef-cf6af069791b/%E1%84%8B%E1%85%B2%E1%84%8C%E1%85%A5_%E1%84%89%E1%85%B5%E1%84%82%E1%85%A1%E1%84%85%E1%85%B5%E1%84%8B%E1%85%A9_-Version5.drawio.png)

# 아키텍처 설계

## 발표 시점

![5차 다이어그램-페이지-1.drawio.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/23c71557-9399-4684-8a1b-24223c790e6c/5%EC%B0%A8_%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8-%ED%8E%98%EC%9D%B4%EC%A7%80-1.drawio.png)

1. 전체 아키텍처 설명
    1. 전체 VPC에서 Public 서브넷과 Private 서브넷을 분리하여 사용자가 접근할 수 있는 부분과 DB를 저장할 수 있는 부분으로 아키텍처를 분리
    2. WAS 역할을 하는 EC2 인스턴스를 Public 서브넷에 배치, DB 역할을 하는 RDS 인스턴스를 Private Subnet에 배치함
2. IGW
    1. 인터넷과 Public Subnet을 연결해주는 관문 역할을 하기 위해 배치
3. ALB
    1. WAS의 부하를 분산 시킬 수 있는 용도로 ALB를 Public 서브넷에 배치함
    2. HTTP 8080 포트로 들어오는 요청에 대해 지정된 2개의 EC2 인스턴스로 트래픽 분산
4. EC2
    1. 도커를 이용해 컨테이너화 시킨 SpringBoot 서버(WAS)를 띄우기 위한 인스턴스
    2. 총 2개의 인스턴스가 ALB에 연결되어 트래픽을 분산하여 담당하고 있음
5. RDS
    1. RDS Primary - RDS Read Replica 구조 확립
        1. 읽기 작업이 많은 프로젝트 요구 사항을 반영하기 위해 RDS Primary 이 외에 RDS Read Replica를 추가해 읽기 작업 퍼포먼스 향상
        2. RDS Primary와 RDS Read Replica를 서로 다른 가용 영역에 배치
6. Gateway endpoint
    1. EC2 인스턴스와 VPC 외부에 위치한 S3를 연결하기 위해 활용
    2. 비용 효율성과 S3와의 호환성을 고려해 해당 서비스 선정
7. S3
    1. 파일 원본 저장소
    2. EC2와 Gateway Endpoint로 연결 되어 있음
8. Monitoring
    - 서비스 모니터링을 위해 CloudWatch와 CloudTrail 활용
9. CI/CD
    1. CI/CD의 경우 아래의 단계를 거쳐 진행됨
    2. 개발자가 GitHub 레포지토리에 코드를 푸시함
    3. GitHub Action이 Docker Hub에 도커 이미지를 업로드하고 S3에 프로젝트 압축 파일을 업로드함
    4. 이후 Code Deploy는 S3에 게시된 압축 파일을 이용해 EC2 인스턴스에 배포 진행

## 추가 진행 시점

![6차 아키텍처-페이지-1의 복사본.drawio.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/4e9d85ad-8074-4f91-b465-1351e0948e32/6%EC%B0%A8_%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%ED%8E%98%EC%9D%B4%EC%A7%80-1%EC%9D%98_%EB%B3%B5%EC%82%AC%EB%B3%B8.drawio.png)

1. 전체 아키텍처 설명
    1. AWS VPC (10.0.0.0/16) 내에서 Public 서브넷과 Private 서브넷을 명확히 분리하여 보안성을 강화
    2. WAS 역할을 하는 EC2 인스턴스를 Private 서브넷에 배치하여 직접적인 외부 접근을 차단하며, DB 역할을 하는 RDS 인스턴스 역시 Private Subnet에 배치함
2. Route53
    1. `acc6-hotsix.shop` 도메인 사용
3. NAT
    1. Private 서브넷의 EC2 인스턴스들이 인터넷과 통신할 수 있도록 NAT Gateway 배치
4. RDS
    1. 해커톤 당시 Read Replica는 성능을 위한 서비스이지 가용성을 위한 것은 아니라는 피드백을 받음
    2. Multi-AZ 구성으로 설정하여 RDS의 고가용성 보장
    3. RDS Primary - RDS Read Replica 구조를 유지하여 읽기 작업 퍼포먼스 향상
5. S3
    1. S3 Glacier를 도입하여 장기 보관된 데이터를 비용 효율적으로 저장
6. EC2
    1. EC2 인스턴스를 private subnet으로 이동하여 보안 강화
7. Auto Scaling Group
    1. 트래픽 변화에 따라 EC2 인스턴스의 수를 자동으로 조절
    2. EC2 인스턴스는 시작 템플릿을 통해 생성됨
8. AWS Session Manager
    1. 관리자가 EC2 인스턴스에 SSH 키 없이 안전하게 접근할 수 있음
9. 네트워크 접근 제어
    1. Security Group을 통해 서브넷 안에서 인스턴스의 트래픽을 제어함
    2. NACL을 통해 서브넷에 오가는 트래픽을 제어함
10. CI/CD
    1. 개발자가 GitHub 레포지토리에 코드를 푸시함
    2. GitHub Action이 Docker 이미지를 ECR에 배포
    3. 배포 스크립트를 S3에 업로드
    4. CodeDeploy를 통해 애플리케이션 자동 배포

## CI/CD

- github action + Docker + ECR + Codedeploy를 사용한 방식
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/9367a398-1331-454b-b8c0-36c3c0b7cda9/image.png)
    
    - github action이 docker 이미지를 생성해 ECR에 업로드, codedeploy 스크립트를 생성해 S3에 업로드
    - S3에 업로드한 스크립트를 codedeploy가 실행하도록 githubaction에서 트리거 전송
    - codedeploy 스크립트 실행 후 배포 진행

# 서버 설계

## 사용한 기술 스택

- spring boot
- mysql

## ERD

- 파일 메타데이터 저장
- 로그 저장

## 발표 이후 변화

- SDK v1에서 v2로 버전 업
- 기존의 multipart 및 정적 S3 URL를 사용한 업로드, 다운로드를 모두 presigned URL 방식으로 변경
- CI/CD 재구축
- 예외 처리 전략 변경

## 기능 명세서

| 기능 | 설명 | 예외처리 |
| --- | --- | --- |
| 파일 업로드 | - 업로드 불가능한 파일 타입: 실행 파일, 셸 스크립트, 자바 아카이브 파일 제외

- 파일 경로: ’/’로 시작, 알파벳, 숫자, 밑줄만 포함 | - 유효하지 않은 비밀번호
- 파일 중복: 같은 경로, 동일한 파일명 존재 |
| 파일 다운로드 | - 파일 다운로드 | - 유효하지 않은 비밀번호
- 파일 없음 |
| 파일 수정 | - 기존 파일과 동일한 파일 타입을 가진 파일로 수정 가능

- 수정 범위: 파일명, 파일 경로, 파일 | - 유효하지 않은 비밀번호
- 파일 없음
- 파일 중복: 같은 경로, 동일한 파일명 존재
- 파일 타입 변화: 기존 파일과 수정 파일의 타입이 다름 |
| 파일 조회
(메타 데이터) | - 조회 결과: 파일 ID, 파일 이름, 파일 생성시간, 파일 확장자, 파일 경로, 수정일자, 원본 리소스 위치 | - 유효하지 않은 비밀번호
- 파일 없음 |
| 파일 조회
(파일) | - 조회 결과: 파일 미리보기 | - 유효하지 않은 비밀번호
- 파일 없음 |
| 파일 공유링크 생성 | - 공유 링크 생성: GET Presigned URL 생성 | - 유효하지 않은 비밀번호
- 파일 없음 |
| 파일 삭제 | - 파일 삭제 | - 유효하지 않은 비밀번호
- 파일 없음 |
| 파일 검색 | - 조회 결과: 파일 ID, 파일 이름, 파일 생성시간, 파일 확장자, 파일 경로 | - 조회 가능한 값 없음
- 검색조건 없음 |
| 파일 조회
(페이지) | - 조회 결과: 파일 ID, 파일 이름, 파일 생성시간, 파일 확장자, 파일 경로 | - 조회 가능한 값 없음
- name, time 파라미터에 asc, desc 외의 다른 값 입력 |
| 파일 조회
(전체) | - 조회 결과: 파일 ID, 파일 이름, 파일 생성시간, 파일 확장자, 파일 경로

- 파일 정렬: 파일 이름, 파일 생성 시간 오름차순 정렬 | - 조회 가능한 값 없음 |
| 로그 조회 | - 로그 조회 | - 파일 없음 |

## API

| 기능명 | URL | Http Method | body |
| --- | --- | --- | --- |
| 파일 업로드 | /files | `POST` | file
directory
password |
| 파일 다운로드  | /files/download/{file_id} | `POST` | password |
| 파일 수정 | /files/{file_id} | `PATCH` | file
directory
password |
| 파일 상세 조회(메타데이터) | /files/detail-meta/{file_id} | `POST` | password |
| 파일 상세 조회(파일) | /files/detail-file/{file_id} | `POST` | password |
| 파일 공유링크 | /files/share/{file_id} | `POST` | password |
| 삭제 | /files/delete/{file_id} | `POST` | password |
- presigned URL 방식으로 변경한 API
- 모두 비밀번호가 필요한 API이며, 비밀번호를 form-data로 전달

| 기능명 | URL | Http Method | params |
| --- | --- | --- | --- |
| 전체 조회 | /files/all | `GET` |  |
| 조회(페이지 단위) | /files | `GET` | page
name
time |
| 검색 | /files/search | `GET` | name
path
before, after
type
page |
| 로그 조회 | /files/{file_id}/logs | `DELETE` | type
page
size |
- 비밀번호가 필요없으며, 수정하지 않은 API

# 부하 테스트

## 파일 업로드 테스트

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/59622d38-796d-4b10-9b32-9e607fefedac/image.png)

- 5명의 사용자가 동시 접속
- 각 사용자는 3분 동안 계속해서 파일 업로드를 반복 실행
- 각 반복 사이에는 1초의 휴식(`sleep(1)`)이 있음
- 파일은 .txt(1MB), .pdf(50MB), .zip(100MB)로 구성되었으며, 실제로 S3에 업로드를 진행
- 3분 동안 이루어진 45개의 요청에 대해 100% 성공

## 파일 다운로드 테스트

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/dc2fd199-f1f6-494d-8a87-5d18bcaa667b/image.png)

- 2명의 사용자가 동시 접속
- 각 사용자는 3분 동안 계속해서 파일 다운로드를 반복 실행
- 각 반복 사이에는 1초의 휴식(`sleep(1)`)이 있음
- 파일은 .txt(1MB), .pdf(50MB), .zip(100MB)로 구성
- 3분 동안 이루어진 37개의 요청에 대해 75.67% 성공

## 파일 업로드/다운로드 동시 테스트

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/bda4d7f4-1501-4860-8096-2722a272372d/image.png)

- 3명의 사용자가 동시 접속
- 각 사용자는 3분 동안 계속해서 파일 업로드/다운로드를 반복 실행
- 각 반복 사이에는 1초의 휴식(`sleep(1)`)이 있음
- 파일은 .txt(1MB), .pdf(50MB), .zip(100MB)로 구성
- 3분 동안 이루어진 27개의 요청에 대해 86.66% 성공

## 파일 검색 테스트

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/485a3e47-0f7c-4e76-a68e-5d5a9f3d26fc/image.png)

- 2명의 사용자가 동시 접속
- 각 사용자는 3분 동안 계속해서 검색 반복 실행
- 각 반복 사이에는 1초의 휴식(`sleep(1)`)이 있음
- 파일은 .txt(1MB), .pdf(50MB), .zip(100MB)로 구성
- 3분 동안 이루어진 351개의 요청에 대해 100% 성공

## 메타데이터 조회 테스트

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/ac76b844-b286-4e63-a704-74afb0a7c3d1/image.png)

- 1분 동안 사용자가 10명으로 증가 → 5분 동안 사용자가 10명으로 유지 → 1분 동안 사용자가 0명으로 감소
- 각 사용자는 계속해서 메타데이터 조회 반복 실행
- 각 반복 사이에는 1초의 휴식(`sleep(1)`)이 있음
- 파일은 .txt(1MB), .pdf(50MB), .zip(100MB)로 구성
- 7분 동안 이루어진 3133개의 요청에 대해 98.65% 성공

## 파일 조회 테스트

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/b782f691-dec6-485a-a6a8-2827dfef2dd5/c3e6970b-a82e-4359-b483-fa44fccf02b6/image.png)

- 1분 동안 사용자가 5명으로 증가 → 5분 동안 사용자가 5명으로 유지 → 1분 동안 사용자가 0명으로 감소
- 각 사용자는 계속해서 파일 조회 반복 실행
- 각 반복 사이에는 1초의 휴식(`sleep(1)`)이 있음
- 파일은 .txt(1MB), .pdf(50MB), .zip(100MB)로 구성
- 7분 동안 이루어진 3133개의 요청에 대해 95.45% 성공

## 부하 테스트 정리

|  | 파일
업로드 | 파일
다운로드 | 업로드/
다운로드 | 파일 검색 | 메타데이터 조회 | 파일 조회 |
| --- | --- | --- | --- | --- | --- | --- |
| 테스트
진행 시간 | 3분 | 3분 | 3분 | 3분 | 7분 | 7분 |
| 평균 사용자 | 5명 | 2명 | 3명 | 2명 | 10명 | 10명 |
| 총 발생 요청 | 45개 | 37개 | 27개 | 351개 | 3133개 | 3133개 |
| 성공률 | 100% | 75.67% | 86.66% | 100% | 98.65% | 95.45% |
- 매일 약 100-200개의 새로운 파일 업로드 예상
- 파일 당 평균 크기는 50MB 예상

상기된 조건을 충족시키는 것을 확인할 수 있음
