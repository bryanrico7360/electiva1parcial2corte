
package com.example.demo.repository;

import com.example.demo.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Proporciona operaciones de acceso a datos para los pedidos.
 *
 * Permite guardar, consultar, actualizar y eliminar pedidos
 * mediante las operaciones heredadas de JpaRepository.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
