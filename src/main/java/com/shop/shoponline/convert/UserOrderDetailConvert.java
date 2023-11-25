package com.shop.shoponline.convert;

import com.shop.shoponline.entity.UserOrder;
import com.shop.shoponline.vo.OrderDetailVO;
import org.mapstruct.factory.Mappers;

public class UserOrderDetailConvert {
    UserOrderDetailConvert INSTANCE = Mappers.getMapper(UserOrderDetailConvert.class);

    OrderDetailVO convertToOrderDetailVo(UserOrder userOrder);
}
