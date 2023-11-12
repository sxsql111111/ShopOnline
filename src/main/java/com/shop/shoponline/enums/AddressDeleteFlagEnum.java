package com.shop.shoponline.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressDeleteFlagEnum {


    DELETE_ADDRESS(0,"删除");

    private final Integer value;
    private final String name;
}
