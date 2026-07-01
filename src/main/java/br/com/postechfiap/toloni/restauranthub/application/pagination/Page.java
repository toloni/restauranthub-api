package br.com.postechfiap.toloni.restauranthub.application.pagination;

import java.util.List;

public record Page<T>(List<T> content, int pageNumber, int pageSize, long totalElements) {

    public Page {
        content = content != null ? List.copyOf(content) : List.of();
    }

    public static <T> Page<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        return new Page<>(content, pageNumber, pageSize, totalElements);
    }

    public static <T> Page<T> empty() {
        return new Page<>(List.of(), 0, 0, 0L);
    }

    public int getTotalPages() {
        return pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public boolean isFirst() {
        return pageNumber == 0;
    }

    public boolean isLast() {
        return pageNumber >= getTotalPages() - 1;
    }

    public boolean hasNext() {
        return pageNumber < getTotalPages() - 1;
    }

    public boolean hasPrevious() {
        return pageNumber > 0;
    }
}
