/**
 * Business Logic Layer.
 * Contains all service classes that implement the core business logic of the application.
 * 
 * 주요 서비스 클래스:
 * 
 * 1. GameService
 *    - 게임의 전반적인 진행을 관리
 *    - 베팅 라운드 진행
 *    - 게임 상태 관리
 *    - 승자 결정 및 상금 분배
 * 
 * 2. GameRoomService
 *    - 게임방 생성 및 관리
 *    - 게임방 상태 관리
 *    - 게임방 설정 관리
 *    - 플레이어 입장/퇴장 처리
 * 
 * 3. PlayerService
 *    - 플레이어 생성 및 관리
 *    - 플레이어 상태 관리
 *    - 베팅 처리
 *    - 스택 관리
 * 
 * 4. HandEvaluator
 *    - 포커 핸드 평가
 *    - 핸드 순위 결정
 *    - 핸드 비교
 * 
 * 5. UserService
 *    - 사용자 관리
 *    - 포인트 관리
 *    - 사용자 정보 관리
 * 
 * 각 서비스는 트랜잭션 관리(@Transactional)를 통해 데이터 일관성을 보장합니다.
 */
package com.suited.service; 