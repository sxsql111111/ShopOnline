package com.shop.shoponline.service;

import com.shop.shoponline.entity.IndexCarousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
public interface IndexCarouselService extends IService<IndexCarousel> {
    //首页-广告区域
    List<IndexCarousel> getList(Integer distributionSite);

}
