import org.example.SpringBootApplication;
import org.example.models.OrderItem;
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
 * Tests for the order-item endpoints.

 insert into order_items (order_id, product_id, quantity) values (1, 1, 1);
 insert into order_items (order_id, product_id, quantity) values (2, 2, 2);
 insert into order_items (order_id, product_id, quantity) values (3, 3, 3);
 insert into order_items (order_id, product_id, quantity) values (4, 4, 4);
 insert into order_items (order_id, product_id, quantity) values (5, 5, 5);
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootApplication.class)
@Import(FinalTestConfiguration.class)
public class OrderItemEndpointTests extends WebStoreTest {
    @Test
    @DisplayName("GET /api/orders-items should return all order items")
    public void getOrderItemsShouldReturnAllOrderItems() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items", HttpMethod.GET, requestEntity, OrderItem[].class);
        var responseCode = result.getStatusCode();
        var orderItems = result.getBody();

        if (orderItems == null) {
            orderItems = new OrderItem[0];
        }

        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(5, orderItems.length);
        assertEquals(1, orderItems[0].getOrderId());
        assertEquals(1, orderItems[0].getProductId());
        assertEquals(1, orderItems[0].getQuantity());
        assertEquals(2, orderItems[1].getOrderId());
        assertEquals(2, orderItems[1].getProductId());
        assertEquals(2, orderItems[1].getQuantity());
        assertEquals(3, orderItems[2].getOrderId());
        assertEquals(3, orderItems[2].getProductId());
        assertEquals(3, orderItems[2].getQuantity());
        assertEquals(4, orderItems[3].getOrderId());
        assertEquals(4, orderItems[3].getProductId());
        assertEquals(4, orderItems[3].getQuantity());
        assertEquals(5, orderItems[4].getOrderId());
        assertEquals(5, orderItems[4].getProductId());
        assertEquals(5, orderItems[4].getQuantity());
    }

    @Test
    @DisplayName("GET /api/orders-items/3 should return the third order item")
    public void getOrderItemShouldReturnThirdOrderItem() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items/3", HttpMethod.GET, requestEntity, OrderItem.class);
        var responseCode = result.getStatusCode();
        var orderItem = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(3, orderItem.getOrderId());
        assertEquals(3, orderItem.getProductId());
        assertEquals(3, orderItem.getQuantity());
    }

    @Test
    @DisplayName("GET /api/orders-items/6 should return 404")
    public void getOrderItemShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items/6", HttpMethod.GET, requestEntity, OrderItem.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }

    @Test
    @DisplayName("POST /api/orders-items should create a new order item")
    public void postOrderItemShouldCreateNewOrderItem() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new OrderItem(0, 1, 1, 1));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items", HttpMethod.POST, requestEntity, OrderItem.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.CREATED, responseCode);
        var orderItem = result.getBody();
        assertEquals(1, orderItem.getOrderId());
        assertEquals(1, orderItem.getProductId());
        assertEquals(1, orderItem.getQuantity());
    }

    @Test
    @DisplayName("PUT /api/orders-items/3 should update the third order item")
    public void putOrderItemShouldUpdateThirdOrderItem() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new OrderItem(3, 1, 1, 1));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items/3", HttpMethod.PUT, requestEntity, OrderItem.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.OK, responseCode);
        var orderItem = result.getBody();
        assertEquals(1, orderItem.getOrderId());
        assertEquals(1, orderItem.getProductId());
        assertEquals(1, orderItem.getQuantity());
    }

    @Test
    @DisplayName("PUT /api/orders-items/6 should return 404")
    public void putOrderItemShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin", new OrderItem(6, 1, 1, 1));
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items/6", HttpMethod.PUT, requestEntity, OrderItem.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }

    @Test
    @DisplayName("DELETE /api/orders-items/3 should delete the third order item")
    public void deleteOrderItemShouldDeleteThirdOrderItem() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items/3", HttpMethod.DELETE, requestEntity, Integer.class);
        var responseCode = result.getStatusCode();
        var affectedRows = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(1, affectedRows);
    }

    @Test
    @DisplayName("DELETE /api/orders-items/6 should return 404")
    public void deleteOrderItemShouldReturn404() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/order-items/6", HttpMethod.DELETE, requestEntity, HttpEntity.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, responseCode);
    }

}
