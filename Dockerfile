#Dockerfile


# 베이스 이미지를 설정합니다.
FROM openjdk:17-alpine

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# 호스트의 모든 파일을 이미지의 /app 디렉토리로 복사합니다.
COPY . /app

# 필요한 패키지 및 도구를 설치합니다.
RUN apk update && \
    apk add --no-cache curl && \
    apk add --no-cache bash && \
    apk add --no-cache unzip

# Gradle 버전을 지합니다.
ENV GRADLE_VERSION=8.7

# Gradle을 다운로드하고 설치합니다.
RUN curl -L https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip && \
    unzip gradle.zip && \
    rm gradle.zip && \
    mv gradle-${GRADLE_VERSION} /opt/gradle

# Gradle 실행 경로를 환경 변수에 추가합니다.
ENV GRADLE_HOME=/opt/gradle
ENV PATH=$PATH:$GRADLE_HOME/bin

# Gradle을 사용하여 프로젝트를 빌드합니다.
RUN gradle clean build --no-daemon

# 빌드된 JAR 파일을 실행합니다.
CMD ["java", "-jar", "build/libs/zicle.jar"]
