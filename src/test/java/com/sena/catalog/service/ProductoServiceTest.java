package com.sena.catalog.service;

import com.sena.catalog.domain.model.Categoria;
import com.sena.catalog.domain.model.Producto;
import com.sena.catalog.domain.dto.ProductoRequestDTO;
import com.sena.catalog.domain.dto.ProductoResponseDTO;
import com.sena.catalog.exception.ResourceNotFoundException;
import com.sena.catalog.repository.CategoriaRepository;
import com.sena.catalog.repository.ProductoRepository;
import com.sena.catalog.service.impl.ProductoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto productoPrueba;
    private Categoria categoriaPrueba;

    @BeforeEach
    void setUp() {
        categoriaPrueba = new Categoria(1L, "Tecnología");
        productoPrueba = new Producto(1L, "Teclado RGB", 150000.0, 10, categoriaPrueba);
    }

    @Test
    @DisplayName("Debería guardar un producto exitosamente")
    void deberiaGuardarProductoExitosamente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaPrueba));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoPrueba);

        ProductoResponseDTO resultado = productoService.guardar(solicitudProducto());
        ArgumentCaptor<Producto> productoCaptor = ArgumentCaptor.forClass(Producto.class);

        assertEquals(1L, resultado.getId());
        assertEquals("Teclado RGB", resultado.getNombre());
        assertEquals(150000.0, resultado.getPrecio());
        assertEquals(10, resultado.getStock());
        assertEquals("Tecnología", resultado.getNombreCategoria());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(productoCaptor.capture());
        assertEquals("Teclado RGB", productoCaptor.getValue().getNombre());
        assertEquals(categoriaPrueba, productoCaptor.getValue().getCategoria());
    }

    @Test
    @DisplayName("Debería retornar un producto cuando el ID existe")
    void deberiaRetornarProductoCuandoExisteId() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoPrueba));

        ProductoResponseDTO resultado = productoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Teclado RGB", resultado.getNombre());
        assertEquals("Tecnología", resultado.getNombreCategoria());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar vacío cuando el ID del producto no existe")
    void deberiaRetornarVacioCuandoNoExisteId() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.buscarPorId(99L));
        verify(productoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debería listar todos los productos")
    void deberiaListarTodosLosProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(productoPrueba));

        List<ProductoResponseDTO> lista = productoService.listarTodos();

        assertEquals(1, lista.size());
        assertEquals("Teclado RGB", lista.get(0).getNombre());
        assertEquals("Tecnología", lista.get(0).getNombreCategoria());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería fallar al guardar cuando la categoría no existe")
    void deberiaFallarAlGuardarCuandoNoExisteCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.guardar(solicitudProducto()));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    private ProductoRequestDTO solicitudProducto() {
        ProductoRequestDTO solicitud = new ProductoRequestDTO();
        solicitud.setNombre("Teclado RGB");
        solicitud.setPrecio(150000.0);
        solicitud.setStock(10);
        solicitud.setCategoriaId(1L);
        return solicitud;
    }
}