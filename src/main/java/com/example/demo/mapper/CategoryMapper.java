package com.example.demo.mapper;

import com.example.demo.dto.order.ProductDTO;
import com.example.demo.helper.CustomerMapper;
import com.example.demo.model.Category;
import com.example.demo.dto.category.Create;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper extends CustomerMapper<Create, Category> {

    Category toEntity(Create dto);
}
