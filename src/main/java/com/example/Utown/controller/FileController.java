package com.example.Utown.controller;

import com.example.Utown.model.FileInfo;
import com.example.Utown.service.FileInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileController {
    private final FileInfoService fileInfoService;

    @GetMapping("/{id}")
    public ResponseEntity<FileInfo> getFileInfoById(@PathVariable Long id) {
        FileInfo fileInfo = fileInfoService.getFileInfoById(id);
        if (fileInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileInfo);
    }
}
