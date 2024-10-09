# This is installing the pgvector extension for postgres
FROM postgres:latest

RUN apt-get update
RUN apt-get install -y build-essential git postgresql-server-dev-all
RUN rm -rf /var/lib/apt/lists/*

WORKDIR /tmp
RUN git clone https://github.com/pgvector/pgvector.git

WORKDIR /tmp/pgvector
RUN make
RUN make install
