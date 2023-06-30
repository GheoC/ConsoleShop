package com.coherent.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable
{
    private int id;
    private int product_id;
    private OrderStatus orderStatus;
    private LocalDateTime issuedDateTime;
    private LocalDateTime confirmedDateTime;
    private LocalDateTime completedDateTime;

    @Override
    public String toString() {
        return "\nOrder{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", orderStatus=" + orderStatus +
                ", issuedDateTime='" + issuedDateTime + '\'' +
                ", confirmedDateTime='" + confirmedDateTime + '\'' +
                ", completedDateTime='" + completedDateTime + '\'' +
                '}';
    }
}
