package com.shop.shoponline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.shoponline.convert.GoodsConvert;
import com.shop.shoponline.entity.Category;
import com.shop.shoponline.entity.Goods;
import com.shop.shoponline.enums.CategoryRecommendEnum;
import com.shop.shoponline.mapper.CategoryMapper;
import com.shop.shoponline.mapper.GoodsMapper;
import com.shop.shoponline.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.shoponline.vo.CategoryChildrenGoodsVO;

import com.shop.shoponline.vo.CategoryVO;
import com.shop.shoponline.vo.RecommendGoodsVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sx
 * @since 2023-11-07
 */
@Service
@AllArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final GoodsMapper goodsMapper;

    @Override
    public List<Category> getIndexCategoryList() {
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();

        wrapper.eq(Category::getIsRecommend, CategoryRecommendEnum.ALL_RECOMMEND.getValue());
        wrapper.orderByDesc(Category::getCreateTime);
        List<Category> list =baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<CategoryVO> getCategoryList() {
        List<CategoryVO> list=new ArrayList<>();
        //查询配置在分类tan分页上的辅机分类
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Category::getIsRecommend,CategoryRecommendEnum.ALL_RECOMMEND.getValue()).or();
        List<Category> categories=baseMapper.selectList(wrapper);
        //2.查询该分类下自己的分类
        LambdaQueryWrapper<Goods> queryWrapper=new LambdaQueryWrapper<>();
        for (Category category:categories){
            CategoryVO categoryVO=new CategoryVO();
            categoryVO.setId(category.getId());
            categoryVO.setName(categoryVO.getName());
            categoryVO.setIcon(categoryVO.getIcon());
            wrapper.clear();
            wrapper.eq(Category::getParentId,category.getId());
            List<Category> childCategories=baseMapper.selectList(wrapper);
            List<CategoryChildrenGoodsVO> categoryChildrenGoodsVOList=new ArrayList<>();

            for (Category item:childCategories){
                CategoryChildrenGoodsVO childrenGoodsVO=new CategoryChildrenGoodsVO();
                childrenGoodsVO.setId(item.getId());
                childrenGoodsVO.setName(item.getName());
                childrenGoodsVO.setIcon(item.getIcon());
                childrenGoodsVO.setParentId(category.getId());
                childrenGoodsVO.setParentName(category.getName());
                queryWrapper.clear();
                List<Goods> goodsList = goodsMapper.selectList(queryWrapper.eq(Goods::getCategoryId,
                        item.getId()));
                List<RecommendGoodsVO> goodsVOList= GoodsConvert.INSTANCE.convertToRecommendGoodsVOList(goodsList);
                childrenGoodsVO.setGoods(goodsVOList);
                categoryChildrenGoodsVOList.add(childrenGoodsVO);
            }
            categoryVO.setChildren(categoryChildrenGoodsVOList);
            list.add(categoryVO);
        }
        return list;
    }

}
