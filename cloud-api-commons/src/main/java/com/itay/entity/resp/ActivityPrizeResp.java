package com.itay.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ActivityPrizeResp {
    private Integer id;
    private String prizeName;
    private String totalStock;
    private Integer usedStock;
    private Double probability;
}
