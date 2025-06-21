package com.javaricci.ApiRestFulWeb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaricci.ApiRestFulWeb.Entity.Fornecedor;
import com.javaricci.ApiRestFulWeb.Repository.FornecedorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    public List<Fornecedor> listarTodos() {
        return repository.findAll();
    }

    public Optional<Fornecedor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Fornecedor salvar(Fornecedor fornecedor) {
        return repository.save(fornecedor);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}