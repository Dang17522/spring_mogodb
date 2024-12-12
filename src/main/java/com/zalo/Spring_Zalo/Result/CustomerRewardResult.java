package com.zalo.Spring_Zalo.Result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRewardResult {
    private String reward;
    private String phone;
    private String exchangeRewardDate;
    private String status;
}
