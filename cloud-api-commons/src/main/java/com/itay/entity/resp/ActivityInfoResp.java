package com.itay.entity.resp;

import com.itay.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ActivityInfoResp {
    private Activity activity;

    private List<ActivityPrizeResp> prizes;

    private ActivityRestrictionResp restriction;
}
