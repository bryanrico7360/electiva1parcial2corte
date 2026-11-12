// by from ia
package com.example.demo.domain.port.in;
import com.example.demo.domain.model.Producto;
import com.example.demo.infrastructure.adapter.input.rest.dto.ProductoResponse;
import java.util.List;
public interface GestionProductoUseCase {
 List<ProductoResponse> getProducts(); ProductoResponse getProductById(Long id); ProductoResponse createProduct(Producto p);
 ProductoResponse updateProduct(Long id,Producto p); void deleteProduct(Long id);
}
