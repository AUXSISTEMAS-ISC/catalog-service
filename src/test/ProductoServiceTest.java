Java
package com.sena.catalog.service;

import com.sena.catalog.domain.dto.ProductoRequestDTO;
import com.sena.catalog.domain.dto.ProductoResponseDTO;
import com.sena.catalog.domain.entity.Categoria;
import com.sena.catalog.domain.entity.Producto;
import com.sena.catalog.exception.ResourceNotFoundException;
import com.sena.catalog.repository.ICategoriaRepository;
import com.sena.catalog.repository.IProductoRepository;
import com.sena.catalog.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private IProductoRepository productoRepository;

    @Mock
    private ICategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Categoria categoria;
    private Producto producto;
    private ProductoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Hardware");

        producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Procesador Intel i7");
        producto.setPrecio(new BigDecimal("1200000.00"));
        producto.setStock(15);
        producto.setCategoria(categoria);

        requestDTO = new ProductoRequestDTO();
        requestDTO.setNombre("Procesador Intel i7");
        requestDTO.setPrecio(new BigDecimal("1200000.00"));
        requestDTO.setStock(15);
        requestDTO.setCategoriaId(1L);
    }

    @Test
    @DisplayName("Debe crear un producto exitosamente cuando la categoría existe")
    void crearProducto_Exitoso() {
        // Arrange (Preparación)
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // Act (Ejecución)
        ProductoResponseDTO resultado = productoService.crearProducto(requestDTO);

        // Assert (Verificación)
        assertNotNull(resultado);
        assertEquals("Procesador Intel i7", resultado.getNombre());
        assertEquals("Hardware", resultado.getNombreCategoria());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la categoría no existe")
    void crearProducto_CategoriaNoEncontrada_LanzaExcepcion() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productoService.crearProducto(requestDTO);
        });

        verify(productoRepository, never()).save(any(Producto.class));
    }
}
