FROM amazoncorretto:21.0.6-alpine

COPY ./target/bank-cards-1.0.0-RC*.jar ./bank-cards.jar

EXPOSE 8080

CMD ["java", "-jar", "./bank-cards.jar"]