
package com.example.demo.repository;

import com.example.demo.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para consultar y administrar los detalles de los pedidos.
 *
 * <p>Proporciona las operaciones CRUD para la entidad DetallePedido
 * y consultas adicionales relacionadas con los productos.</p>
 *
 * @author
 * @version 1.0
 */
@Repository
public interface DetallePedidoRepository
        extends JpaRepository<DetallePedido, Long> {

    /**
     * Comprueba si existe algún detalle de pedido asociado al producto
     * indicado.
     *
     * @param productoId identificador del producto que se desea comprobar
     * @return true si existe un detalle asociado; false en caso contrario
     */
    boolean existsByProductoId(Long productoId);
}
