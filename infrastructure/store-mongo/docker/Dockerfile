# Use MongoDB base image
FROM mongo:latest

ENV MONGO_INITDB_ROOT_USERNAME root
ENV MONGO_INITDB_ROOT_PASSWORD root

# Copy the JSON data and init script into the container
COPY stores.json /tmp/stores.json
COPY importscript.sh /docker-entrypoint-initdb.d/

COPY init-mongo.js /docker-entrypoint-initdb.d/init-mongo.js
