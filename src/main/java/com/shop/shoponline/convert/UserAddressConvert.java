package com.shop.shoponline.convert;

import com.shop.shoponline.entity.UserShippingAddress;
import com.shop.shoponline.vo.UserAddressVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserAddressConvert {

    UserAddressConvert INSTANCE = Mappers.getMapper(UserAddressConvert.class);


    UserAddressVO convertToUserAddressVO(UserShippingAddress userShippingAddress);


    List<UserAddressVO> convertToUserAddressVOList(List<UserShippingAddress> list);
}
