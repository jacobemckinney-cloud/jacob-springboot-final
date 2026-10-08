import org.example.SpringBootApplication;
import org.example.models.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import support.FinalTestConfiguration;
import support.WebStoreTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the order endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootApplication.class)
@Import(FinalTestConfiguration.class)
public class OrderEndpointTests extends WebStoreTest {
    @Test
    @DisplayName("GET /api/orders should return all orders")
    public void getOrdersShouldReturnAllOrders() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders", HttpMethod.GET, requestEntity, Order[].class);
        var responseCode = result.getStatusCode();
        var orders = result.getBody();

        if (orders == null) {
            orders = new Order[0];
        }

        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(5, orders.length);
        assertEquals("test-admin", orders[0].getUsername());
        assertEquals(1, orders[0].getId());
        assertEquals("test-admin", orders[1].getUsername());
        assertEquals(2, orders[1].getId());
        assertEquals("test-admin", orders[2].getUsername());
        assertEquals(3, orders[2].getId());
        assertEquals("test-admin", orders[3].getUsername());
        assertEquals(4, orders[3].getId());
        assertEquals("test-admin", orders[4].getUsername());
        assertEquals(5, orders[4].getId());
    }

    @Test
    @DisplayName("GET /api/orders/3 should return the third order")
    public void getOrderShouldReturnThirdOrder() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders/3", HttpMethod.GET, requestEntity, Order.class);
        var responseCode = result.getStatusCode();
        var order = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals("test-admin", order.getUsername());
        assertEquals(3, order.getId());
    }

    @Test
    @DisplayName("GET /api/orders/6 should return 404")
    public void getOrderShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders/6", HttpMethod.GET, requestEntity, Order.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }

    @Test
    @DisplayName("POST /api/orders should create a new order")
    public void postOrderShouldCreateNewOrder() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new Order(0, "test-admin"));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders", HttpMethod.POST, requestEntity, Order.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.CREATED, responseCode);
        var order = result.getBody();
        assertEquals("test-admin", order.getUsername());
        assertEquals(6, order.getId());
    }

    @Test
    @DisplayName("PUT /api/orders/3 should update the third order")
    public void putOrderShouldUpdateThirdOrder() {
        getJdbcTemplate().update("insert into users (username, password) values ('user', 'user')");
        var requestEntity = GetAuthEntity("test-admin", "admin", new Order(3, "user"));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders/3", HttpMethod.PUT, requestEntity, Order.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.OK, responseCode);
        var order = result.getBody();
        assertEquals("user", order.getUsername());
        assertEquals(3, order.getId());
    }

    @Test
    @DisplayName("PUT /api/orders/6 should return 404")
    public void putOrderShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new Order(6, "test-admin"));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders/6", HttpMethod.PUT, requestEntity, Order.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }

    @Test
    @DisplayName("DELETE /api/orders/3 should delete the third order")
    public void deleteOrderShouldDeleteThirdOrder() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders/3", HttpMethod.DELETE, requestEntity, Integer.class);
        var responseCode = result.getStatusCode();
        var affectedRows = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(1, affectedRows);
    }

    @Test
    @DisplayName("DELETE /api/orders/6 should return 404")
    public void deleteOrderShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/orders/6", HttpMethod.DELETE, requestEntity, HttpEntity.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }
}
