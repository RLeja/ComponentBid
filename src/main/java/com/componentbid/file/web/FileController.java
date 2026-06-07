package com.componentbid.file.web;

import com.componentbid.file.dto.FileWrapper;
import com.componentbid.file.service.IFileService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    //TODO: Not needed now, but safe approach would include token authorization
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable UUID id) throws IOException {
        FileWrapper file = fileService.get(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getName() + "\""
                )
                .body(file.getResource());
    }
}
