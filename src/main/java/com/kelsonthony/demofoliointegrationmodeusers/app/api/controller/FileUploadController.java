package com.kelsonthony.demofoliointegrationmodeusers.app.api.controller;

import com.kelsonthony.demofoliointegrationmodeusers.app.api.openapi.FileUploadControllerOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileUploadController implements FileUploadControllerOpenApi {

    @Value("${report.input.filepath}")
    private String filePath;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Define o caminho onde o arquivo será salvo
            Path path = Paths.get(filePath);

            // Verifica se o diretório existe, caso contrário, cria-o
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            // Salva o arquivo no diretório especificado com o nome correto
            Files.write(path, file.getBytes());

            return ResponseEntity.status(HttpStatus.OK).body("Arquivo enviado e renomeado com sucesso.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}
