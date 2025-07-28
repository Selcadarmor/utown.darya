package com.example.Utown.controller;

import com.example.Utown.dto.fileInfoDTO.FileInfoDetailsDto;
import com.example.Utown.service.S3Service.FileInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
@Tag(name = "Files", description = "File upload, download, and metadata operations")
public class FileController {
    private final FileInfoService fileInfoService;

    @Operation(summary = "Get file metadata", description = "Returns metadata for a file by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File metadata retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FileInfoDetailsDto.class))),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    //получение метаданных по айди
    @GetMapping("/{id}")
    public ResponseEntity<FileInfoDetailsDto> getFileInfo(
            @Parameter(description = "File ID", required = true)
            @PathVariable Long id){
        FileInfoDetailsDto file = fileInfoService.getFileInfo(id);
        return ResponseEntity.ok(file);
    }

    @Operation(summary = "Download a file", description = "Downloads the file from S3 using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded successfully",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    //скачивание по ID возвращение сырых байтов
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(
            @Parameter(description = "File ID", required = true)
            @PathVariable Long id) {
        FileInfoDetailsDto file = fileInfoService.getFileInfo(id);
        byte[] data = fileInfoService.getFileBytes(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +file.getOriginalTitle() + "\"")
                .contentType(MediaType.parseMediaType(file.getType()))
                .body(data);
    }

    @Operation(summary = "Upload a file", description = "Uploads a file to S3 and stores metadata in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FileInfoDetailsDto.class)))
    })
    //загрузка файла
    @PostMapping
    public  ResponseEntity<FileInfoDetailsDto> uploadFile(
            @Parameter(description = "File to upload", required = true)
            @RequestParam("file")MultipartFile file){
        FileInfoDetailsDto savedFile = fileInfoService.saveFile(file);
        return ResponseEntity.ok(savedFile);
    }
}
