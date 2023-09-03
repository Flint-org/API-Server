# 베이스 이미지 선택
FROM openjdk:17

# 작업 디렉토리 생성
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너 안의 'app.jar'로 복사
COPY build/libs/flint-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너가 실행될 명령어 설정
CMD ["java", "-jar", "app.jar"]