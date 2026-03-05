package dev.shaikhmahad.template.utils;

import dev.shaikhmahad.template.dto.response.PageableResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class Helper {

    /**
     * Converts a Spring Data JPA 'Page' object into our standardized 'PageableResponse' DTO.
     * * @param page is The raw Page object returned straight from the database Repository
     * @param dtoList The list of mapped DTOs (converted using MapStruct)
     * @param <U>     The Entity type (e.g., User)
     * @param <V>     The DTO type (e.g., UserDto)
     * @return        A clean, standardized PageableResponse containing the DTOs and pagination math
     **/
    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, List<V> dtoList) {

        return PageableResponse.<V>builder()
                .content(dtoList)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLastPage(page.isLast())
                .build();
    }
}