export $(grep -v "^#" .env | xargs)
./mvnw spring-boot:run

# export $(grep -v "^#" .env | xargs)
# ./mvnw clean install