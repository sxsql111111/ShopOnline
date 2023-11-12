package com.shop.shoponline.convert;

import com.shop.shoponline.entity.UserShippingAddress;
import com.shop.shoponline.vo.AddressVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
//convert:实体类与视图之间的转换与映射
@Mapper
public interface AddressConvert {
    AddressConvert INSTANCE = Mappers.getMapper(AddressConvert.class);


    UserShippingAddress convert(AddressVO addressVO);


    List<AddressVO> convertToAddressVOList(List<UserShippingAddress> addressList);


    AddressVO convertToAddressVO(UserShippingAddress userShippingAddress);
}
