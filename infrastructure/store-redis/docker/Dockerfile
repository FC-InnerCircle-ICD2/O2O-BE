# Redis 이미지를 기반으로 생성
FROM redis:7.0.15

# 로컬 dump.rdb 파일을 컨테이너의 /data 디렉토리로 복사
COPY dump.rdb /data/dump.rdb

# Redis 데이터 디렉토리를 /data로 설정
VOLUME /data