
package com.example.demo.repository;

import com.example.demo.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder y administrar los pedidos almacenados
 * en la base de datos.
 *
 * <p>Hereda de JpaRepository las operaciones CRUD para la entidad
 * Pedido, utilizando Long como tipo de identificador.</p>
 *
 * @author
 * @version 1.0
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
