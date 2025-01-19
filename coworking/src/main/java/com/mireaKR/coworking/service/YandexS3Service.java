package com.mireaKR.coworking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.RequestBody;

import java.net.URI;

@Service
public class YandexS3Service {

    @Value("${yandex.s3.access.key}")
    private String yandexAccessKey;

    @Value("${yandex.s3.secret.key}")
    private String yandexSecretKey;

    @Value("${yandex.s3.endpoint}")
    private String yandexEndpoint;

    private final String bucketName = "lyubov"; // Укажите имя вашего бакета

    public String uploadFile(MultipartFile file) {
        try {
            // Настройка учетных данных
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(yandexAccessKey, yandexSecretKey);
            S3Client s3Client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .region(Region.of("ru-central1")) // Регион Яндекс Облака
                    .endpointOverride(URI.create(yandexEndpoint)) // Указываем кастомный endpoint
                    .build();

            // Создание объекта PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getOriginalFilename()) // Имя файла в бакете
                    .build();

            // Загрузка файла
            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()) // Передаем содержимое файла как массив байт
            );

            // Проверяем статус ответа (опционально)
            if (response.sdkHttpResponse().isSuccessful()) {
                return yandexEndpoint + "/" + bucketName + "/" + file.getOriginalFilename();
            } else {
                throw new RuntimeException("Ошибка загрузки файла: " + response.sdkHttpResponse().statusText().orElse("Неизвестная ошибка"));
            }

        } catch (S3Exception e) {
            // Обработка исключений S3
            throw new RuntimeException("Ошибка S3: " + e.awsErrorDetails().errorMessage(), e);
        } catch (Exception e) {
            // Обработка остальных исключений
            throw new RuntimeException("Ошибка загрузки файла в Yandex Object Storage", e);
        }
    }
}
