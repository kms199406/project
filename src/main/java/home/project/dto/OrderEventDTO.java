package home.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import home.project.domain.Member;
import home.project.domain.ProductOrder;
import home.project.domain.Shipping;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderEventDTO {
    private String eventType;
    private LocalDateTime orderDate;
    private Long memberId;
    private Long shippingId;
    private List<Long> productOrderIds;

    @JsonCreator
    public OrderEventDTO(
            @JsonProperty("eventType") String eventType,
            @JsonProperty("orderDate") LocalDateTime orderDate,
            @JsonProperty("memberId") Long memberId,
            @JsonProperty("shippingId") Long shippingId,
            @JsonProperty("productOrderIds") List<Long> productOrderIds) {
        this.eventType = eventType;
        this.orderDate = orderDate;
        this.memberId = memberId;
        this.shippingId = shippingId;
        this.productOrderIds = productOrderIds;
    }
}
