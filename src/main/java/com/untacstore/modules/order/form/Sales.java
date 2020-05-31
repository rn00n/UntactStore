package com.untacstore.modules.order.form;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Sales {
    private LocalDate date;
    private Integer sales;
}
