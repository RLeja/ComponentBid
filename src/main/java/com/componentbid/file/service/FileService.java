package com.componentbid.file.service;

import com.componentbid.file.dto.FileWrapper;
import com.componentbid.file.entity.FileMetadata;
import com.componentbid.file.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {

    private final FileMetadataRepository fileMetadataRepository;
    private final Path uploadPath  = Paths.get("files");

    public FileMetadata save(MultipartFile file) throws IOException {

        Files.createDirectories(uploadPath);

        var storageId = UUID.randomUUID();

        Path filePath = uploadPath.resolve(storageId.toString());
        Files.copy(file.getInputStream(), filePath);

        var metadata = FileMetadata.builder()
                .storageId(storageId)
                .name(file.getOriginalFilename())
                .extension(parseExtension(file.getOriginalFilename()))
                .size(file.getSize())
                .build();

        return fileMetadataRepository.save(metadata);
    }

    public FileWrapper get(UUID id) throws IOException {

        FileMetadata metadata = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File metadata not found"));

        Path filePath = uploadPath.resolve(metadata.getStorageId().toString());

        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found");
        }

        return FileWrapper.builder()
                .name(metadata.getFullName())
                .resource(new UrlResource(filePath.toUri()))
                .build();
    }

    private String parseExtension(String fileName) {

        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf('.'));
        }

        return "";
    }
}
