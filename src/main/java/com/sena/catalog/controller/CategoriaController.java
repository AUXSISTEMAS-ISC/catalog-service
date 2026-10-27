package com.sena.catalog.controller;

import com.sena.catalog.domain.model.Categoria;
import com.sena.catalog.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. Obtener todas las categorías reales
    @GetMapping
    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }

    // 2. Obtener categoría por ID
    @GetMapping("/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    // 3. Crear nueva categoría en MySQL
    @PostMapping
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // 4. Actualizar categoría
    @PutMapping("/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    // 5. Eliminar categoría
    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
    }
}