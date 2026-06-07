package com.componentbid.file.service;

import com.componentbid.file.dto.FileWrapper;
import com.componentbid.file.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IFileService {
    FileMetadata save(MultipartFile file) throws IOException;
    FileWrapper get(UUID id) throws IOException;
}
