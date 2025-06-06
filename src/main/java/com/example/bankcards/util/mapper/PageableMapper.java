package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.common.PageableRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PageableMapper {

    public static Pageable mapPageable(PageableRequest page) {
        List<Sort.Order> sorts = page.getSorts().stream()
                .map(sort -> new Sort.Order(
                        Sort.Direction.valueOf(sort.getDirection()),
                        sort.getField()))
                .toList();

        return PageRequest.of(page.getNumber() - 1, page.getSize(), Sort.by(sorts));
    }
}
