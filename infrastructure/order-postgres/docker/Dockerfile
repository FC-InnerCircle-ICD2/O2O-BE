FROM postgres:15

ENV POSTGRES_USER=root
ENV POSTGRES_PASSWORD=root
ENV POSTGRES_DB=postgres

COPY init-script/ /docker-entrypoint-initdb.d/
