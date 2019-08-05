insert into user(id, username, password, otp_secret_key) values(1, 'username', 'password', 'otp-secret-key');
-- 비밀번호는 PasswordEncoderTest 클래스를 이용해서 생성한 것을 저장
-- otp-secret-key 는 UserServiceTest 통합테스트를 통해서 발송된 QR 코드의 secret 을 입력