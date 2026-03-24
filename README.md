# Youngwon's Portfolio
> Backend/Full-stack Developer **Youngwon**  
> Portfolio url: http://youngwon.me  
> Youngwon's server configuration: https://github.com/yw7148/server

Welcome to my Backend developer portfolio repository! This project showcases my skills and projects as a backend developer, including various technologies and tools I've mastered.

![youngwon](https://github.com/yw7148/Portfolio_Backend/assets/71220342/18b6f1a7-82c1-4f8c-a779-350cfef28dea)

## 🚀 Features

- **User Authentication**: Secure login and registration system using JWT.
- **RESTful API**: Efficient and scalable APIs.
- **Database Integration**: Oracle Database for robust data storage.
- **Testing**: Comprehensive unit and integration tests.
- **Documentation**: Well-documented API using Swagger/OpenAPI.
- **Deployment**: Continuous integration and deployment using GitHub Actions and Docker.

## 🛠️ Technologies Used
> for DevOps information, please refer [Server Repository](https://github.com/yw7148/Server)
- **Backend**: Spring boot 3.2.1 (Java 17)
- **Database**: Mariadb 11 (Local Development), Oracle Database 19c (Production)
- **Authentication**: JWT, OAuth (Preparing...)
- **Testing**: Spring Test (JUnit)
- **Containerization**: Docker
- **CI/CD**: GitHub Actions
- **Others**: Thymeleaf/HTML/css/js

## 📂 Project Structure

```
.
├── src
|   ├── main
│   |   ├── java
|   |   |   ├── controllers
|   |   |   ├── dto
|   |   |   ├── entity
|   |   |   ├── repository
|   │   |   └── services
|   |   └── resources
│   └── tests
├── Dockerfile
├── build.gradle
├── .github/workflows
│   ├── ci.yml
│   └── release.yml
├── LICENSE
└── README.md
```

## 📖 Getting Started

### Prerequisites

- Docker

### Build docker image
> In my case, *--platform linux/amd64,linux/arm64* is added to support multi-platform (my production environment is *linux/arm64*)

> to build multi-platform image, docker buildx builder is required in *docker build* environment:  
> *docker buildx create --name mybuilder --bootstrap --use*  
> for more information: [Multi-Platform Images | Docker Docs](https://docs.docker.com/build/building/multi-platform/)
```bash
DOCKER_BUILDKIT=1 docker buildx build \
    --platform linux/amd64,linux/arm64 \ #option to support multi-platform
    --build-arg DB_URL=${TEST_DB_URL} \
    --build-arg DB_USER=${TEST_DB_USER} \
    --build-arg DB_PW=${TEST_DB_PASSWORD} \
    --build-arg JWT_SECRET=${TEST_JWT_SECRET} \
    -t yw7148/portfolio:latest .'
```

## CI/CD

GitHub Actions workflows now handle the repository CI/CD flow.

- Push to `main`: runs `clean test bootJar`
- Push a Git tag: runs build/test, creates a GitHub Release, then publishes Docker images to Docker Hub

### Required GitHub repository secrets

- `DB_URL`
- `DB_USER`
- `DB_PW`
- `JWT_SECRET`
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`

### Docker image tags published on release

- `yw7148/portfolio:<git-tag>`
- `yw7148/portfolio:latest`

## Deploy

#### Before deploy with docker, env file is required
.env file
```
 DB_URL=
 DB_USER=
 DB_PW=
 JWT_SECRET=
```
#### Run docker container
```bash
docker run --pull always -d --name portfolio -p 9001:9001 \
    -e PROFILE=prod \
    --env-file ${path_to_envfile} \
    yw7148/portfolio:latest
```

## 📄 API Documentation (Preparing ... )

## 📬 Contact

- **Email**: yw7148@naver.com
- **Portfolio**: [Home](http://youngwon.me), [CV Page](http://youngwon.me/portfolio/cv)

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.
