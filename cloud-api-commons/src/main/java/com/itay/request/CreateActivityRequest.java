package com.itay.request;

import com.itay.entity.Activity;
import com.itay.entity.ActivityPrize;
import com.itay.entity.ActivityRestriction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityRequest {
    private Activity activity;
    private List<ActivityPrize> prizes;
    private ActivityRestriction activityRestriction;
}