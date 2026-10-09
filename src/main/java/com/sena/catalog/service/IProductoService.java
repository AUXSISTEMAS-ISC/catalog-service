package com.sena.catalog.service;

import com.sena.catalog.domain.dto.ProductoRequestDTO;
import com.sena.catalog.domain.dto.ProductoResponseDTO;

import java.util.List;

public interface IProductoService {
    List<ProductoResponseDTO> listarTodos();
    ProductoResponseDTO buscarPorId(Long id);
    ProductoResponseDTO guardar(ProductoRequestDTO dto);
}