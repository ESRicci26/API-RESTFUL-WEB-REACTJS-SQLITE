package com.javaricci.ApiRestFulWeb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaricci.ApiRestFulWeb.Entity.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
}