package com.example.demo.Helper.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface Pagination<T,ID>{
    Iterable<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

}
