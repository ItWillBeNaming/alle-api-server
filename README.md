
### Deploy
#### Docker Image Build
- docker build --platform linux/amd64 -t minxhvk/alle-api-server:latest .
- docker push minxhvk/alle-api-server:latest
- docker pull minxhvk/alle-api-server:latest

<br/>

### Local
#### MySQL Docker run
- docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.3.0
- docker exec -it mysql mysql -uroot -p

<br/>

### ETC
**Docker Cache Clean**
- docker system prune -a