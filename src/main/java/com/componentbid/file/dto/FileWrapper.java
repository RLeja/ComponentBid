package com.componentbid.file.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.core.io.Resource;

@Getter
@Builder
@AllArgsConstructor
public class FileWrapper {
    private String name;
    private Long size;

    private Resource resource;
}