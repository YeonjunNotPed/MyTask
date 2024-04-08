로그인 
- jwt 로그인 방식으로 만든 소셜 로그인 및 로그인 화면
- 구글/카카오 로그인 적용
- jwt 토큰 암호화 및 keyStore로 저장, 토큰 조회,저장 시 암호화,복호화 로직
- TokenAuthenticator 를 통해 401 에러(토큰 만료) 일 때 refreshToken 교체 로직
- AuthenticationInterceptor 를 통해 request 요청 시 토큰 헤더에 추가 로직

스토어
- 안드로이드 인앱/구독 결제를 만들어보기 위한 스토어 화면
- GoogleBillingUtil 에서 인앱/구독 결제 및 구독 변경 처리
- StoreViewModel에서 구매 검증 통신 처리

WebRTC 통신
- webRTC 통신 적용하여 화상통화 개발
- LiveRoomViewModel, WebRTCContract, RoomRepository, WebSocketDataSource

ChatGpt Api를 사용한 채팅방
- ROOM Flow, ChatGpt Api를 사용하여 채팅 개발 => GptViewModel
- 이전 답변을 Assistant로 저장하여 사용하고, 의사,변호사 기타 등등의 역할을 선택하여 "당신은 이제부터 (직업)입니다" 와 같은 prefix와 조합 후 채팅

프로젝트단 - MVI, Hilt, Navigation, Compose, DataStore 사용
