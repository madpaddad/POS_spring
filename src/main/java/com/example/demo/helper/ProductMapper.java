package com.example.demo.helper;

import com.example.demo.dto.order.ProductDTO;
import com.example.demo.model.Product;

import org.mapstruct.Mapper;
//import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper extends CustomerMapper<ProductDTO, Product>{
}
