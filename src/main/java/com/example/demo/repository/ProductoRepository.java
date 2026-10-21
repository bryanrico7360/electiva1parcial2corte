
package com.example.demo.repository;

import com.example.demo.model.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para consultar y administrar los productos de la tienda.
 *
 * <p>Hereda las operaciones CRUD de Spring Data JPA y define consultas
 * adicionales para buscar productos y bloquearlos durante operaciones
 * que requieren proteger el inventario.</p>
 *
 * @author
 * @version 1.0
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Comprueba si existe un producto con el nombre indicado,
     * ignorando las diferencias entre mayúsculas y minúsculas.
     *
     * @param nombre nombre que se desea comprobar
     * @return true si existe un producto con ese nombre; false en caso contrario
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Busca productos cuyo nombre contenga el texto indicado,
     * sin distinguir entre mayúsculas y minúsculas.
     *
     * @param nombre texto que se buscará en el nombre del producto
     * @return lista de productos coincidentes
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca un producto por su identificador y aplica un bloqueo
     * pesimista de escritura durante la transacción.
     *
     * @param id identificador del producto
     * @return producto encontrado, o vacío si no existe
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> buscarPorIdConBloqueo(@Param("id") Long id);
}
