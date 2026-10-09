package com.sena.catalog.service.impl;

import com.sena.catalog.domain.dto.ProductoRequestDTO;
import com.sena.catalog.domain.dto.ProductoResponseDTO;
import com.sena.catalog.domain.model.Categoria;
import com.sena.catalog.domain.model.Producto;
import com.sena.catalog.exception.ResourceNotFoundException;
import com.sena.catalog.repository.CategoriaRepository;
import com.sena.catalog.repository.ProductoRepository;
import com.sena.catalog.service.IProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El producto con ID " + id + " no fue encontrado."));
        return convertirADto(producto);
    }

    @Override
    @Transactional
    public ProductoResponseDTO guardar(ProductoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID " + dto.getCategoriaId() + " no existe."));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);

        Producto guardado = productoRepository.save(producto);
        return convertirADto(guardado);
    }

    private ProductoResponseDTO convertirADto(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : "Sin Categoría"
        );
    }
}