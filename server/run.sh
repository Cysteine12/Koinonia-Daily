export $(grep -v "^#" .env | xargs)
./mvnw spring-boot:run

# Command to re-install dependencies
# export $(grep -v "^#" .env | xargs)
# ./mvnw clean install