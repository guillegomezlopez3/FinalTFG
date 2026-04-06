package com.tfgfitapp.tfgfitapp.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Wrapper generico para respuestas paginadas.
 * Evita exponer el objeto Page de Spring directamente en la API.
 */
@Data
@Builder
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    /**
     * Convierte un Page de Spring en un PageResponse limpio.
     */
    public static <T> PageResponse<T> from(Page<T> springPage) {
        return PageResponse.<T>builder()
                .content(springPage.getContent())
                .page(springPage.getNumber())
                .size(springPage.getSize())
                .totalElements(springPage.getTotalElements())
                .totalPages(springPage.getTotalPages())
                .first(springPage.isFirst())
                .last(springPage.isLast())
                .build();
    }
}

