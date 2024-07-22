./mvnw clean package -DskipTests
docker build -t hospital_demo -f src/main/docker/Dockerfile.jvm .
