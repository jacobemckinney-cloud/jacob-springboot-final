import org.example.SpringBootApplication;
import org.example.models.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import support.FinalTestConfiguration;
import support.WebStoreTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the product endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootApplication.class)
@Import(FinalTestConfiguration.class)
public class ProductEndpointTests extends WebStoreTest {
    @Test
    @DisplayName("GET /api/products should return all products")
    public void getProductsShouldReturnAllProducts() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products", HttpMethod.GET, requestEntity, Product[].class);
        var responseCode = result.getStatusCode();
        var products = result.getBody();

        if (products == null) {
            products = new Product[0];
        }

        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(5, products.length);
        assertEquals("Apple", products[0].getName());
        assertEquals(new BigDecimal("0.99"), products[0].getPrice());
        assertEquals("Banana", products[1].getName());
        assertEquals(new BigDecimal("0.59"), products[1].getPrice());
        assertEquals("Cherry", products[2].getName());
        assertEquals(new BigDecimal("1.99"), products[2].getPrice());
        assertEquals("Date", products[3].getName());
        assertEquals(new BigDecimal("2.99"), products[3].getPrice());
        assertEquals("Elderberry", products[4].getName());
        assertEquals(new BigDecimal("3.99"), products[4].getPrice());
    }

    @Test
    @DisplayName("GET /api/products/3 should return the cherry product")
    public void getProductShouldReturnCherryProduct() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products/3", HttpMethod.GET, requestEntity, Product.class);
        var responseCode = result.getStatusCode();
        var product = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals("Cherry", product.getName());
        assertEquals(new BigDecimal("1.99"), product.getPrice());
    }

    @Test
    @DisplayName("GET /api/products/6 should return 404")
    public void getProductShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products/6", HttpMethod.GET, requestEntity, Product.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }

    @Test
    @DisplayName("POST /api/products should create a new product")
    public void postProductShouldCreateNewProduct() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new Product(0, "Fig", new BigDecimal("4.99")));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products", HttpMethod.POST, requestEntity, Product.class);
        var responseCode = result.getStatusCode();
        var createdProduct = result.getBody();
        assertEquals(HttpStatus.CREATED, responseCode);
        assertEquals("Fig", createdProduct.getName());
        assertEquals(new BigDecimal("4.99"), createdProduct.getPrice());
    }

    @Test
    @DisplayName("PUT /api/products/3 should update the cherry product")
    public void putProductShouldUpdateCherryProduct() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new Product(3, "Cherry", new BigDecimal("2.99")));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products/3", HttpMethod.PUT, requestEntity, Product.class);
        var responseCode = result.getStatusCode();
        var updatedProduct = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals("Cherry", updatedProduct.getName());
        assertEquals(new BigDecimal("2.99"), updatedProduct.getPrice());
    }

    @Test
    @DisplayName("PUT /api/products/6 should return 404")
    public void putProductShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new Product(6, "Fig", new BigDecimal("4.99")));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products/6", HttpMethod.PUT, requestEntity, Product.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }

    @Test
    @DisplayName("DELETE /api/products/3 should delete the cherry product")
    public void deleteProductShouldDeleteCherryProduct() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products/3", HttpMethod.DELETE, requestEntity, Integer.class);
        var responseCode = result.getStatusCode();
        var affectedRows = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(1, affectedRows);
    }

    @Test
    @DisplayName("DELETE /api/products/6 should return 404")
    public void deleteProductShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/products/6", HttpMethod.DELETE, requestEntity, HttpEntity.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }
}
