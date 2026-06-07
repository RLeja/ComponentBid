package com.componentbid.file.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_metadata")
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID storageId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Builder
    public FileMetadata(UUID storageId, String name, String extension, long size) {
        this.storageId = storageId;
        this.name = name;
        this.extension = extension;
        this.size = size;
    }

    public String getFullName() {
        return name + extension;
    }
}
