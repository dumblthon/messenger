### Easy PostgreSQL 10 and pgAdmin 4 Setup with Docker
https://info.crunchydata.com/blog/easy-postgresql-10-and-pgadmin-4-setup-with-docker

`docker network create --driver bridge pgnetwork`

`docker run -p 5432:5432 --name=postgres --hostname=postgres --network=pgnetwork \
 -e 'POSTGRES_PASSWORD=testpswd' -d postgres:12`

`docker run -p 8080:80 -p 5050:5050 --name=pgadmin4 --hostname=pgadmin4 --network=pgnetwork \
 -e 'PGADMIN_DEFAULT_EMAIL=web4rabbit@gmail.com' -e 'PGADMIN_DEFAULT_PASSWORD=testpswd' -d dpage/pgadmin4`