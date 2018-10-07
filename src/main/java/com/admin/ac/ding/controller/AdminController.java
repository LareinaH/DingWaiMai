package com.admin.ac.ding.controller;

import com.admin.ac.ding.mapper.CategoryMapper;
import com.admin.ac.ding.mapper.CommodityMapper;
import com.admin.ac.ding.model.Category;
import com.admin.ac.ding.model.Commodity;
import com.admin.ac.ding.model.CommodityDetailVO;
import com.admin.ac.ding.model.RestResponse;
import com.admin.ac.ding.service.CacheService;
import com.admin.ac.ding.service.DingService;
import com.admin.ac.ding.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.admin.ac.ding.constants.Constants.RcError;

@RestController
@RequestMapping(value = "/admin", produces = "application/json; charset=UTF-8")
public class AdminController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    DingService dingService;

    @Autowired
    CacheService cacheService;

    @Value("${ding.app.meetingbook.agentid}")
    Long meetingBookAppAgentId;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CommodityMapper commodityMapper;

    @RequestMapping(value = "/addCategory", method = {RequestMethod.POST})
    public RestResponse<Void> addCategory(
            String categoryName
    ) {
        Category category = new Category();
        category.setCategoryId(Utils.getUUIDString());
        category.setCategoryName(categoryName);
        categoryMapper.insert(category);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/updateCategory", method = {RequestMethod.POST})
    public RestResponse<Void> addCategory(
            Long id,
            String categoryName
    ) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null) {
            return RestResponse.getFailedResponse(RcError, "商品类别不存在");
        }

        if (StringUtils.isBlank(categoryName)) {
            return RestResponse.getFailedResponse(RcError, "商品类别为空");
        }

        category.setCategoryName(categoryName);
        categoryMapper.updateByPrimaryKey(category);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delCategory", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<Void> delCategory(
            String ids
    ) {
        List<String> idList = Arrays.asList(ids.split(",", -1))
                .stream().filter(x -> StringUtils.isNotBlank(x)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return RestResponse.getFailedResponse(RcError, "没有要删除的商品类别");
        }
        // 取得所有商品类别的编码
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", false);
        criteria.andIn("id", idList);
        List<Category> categoryList = categoryMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(categoryList)) {
            return RestResponse.getFailedResponse(RcError, "未查询到要删除的商品类别");
        }

        // 查找对应大类下是否有商品
        for (Category category : categoryList) {
            Example example2 = new Example(Commodity.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("isDeleted", false);
            criteria2.andEqualTo("categoryId", category.getCategoryId());
            List<Commodity> commodityList = commodityMapper.selectByExample(example2);
            if (!CollectionUtils.isEmpty(commodityList)) {
                return RestResponse.getFailedResponse(
                        RcError,
                        String.format(
                                "商品类别: %s 下仍有以下商品关联:%s,请将商品从该类别下删除再重试",
                                category.getCategoryName(),
                                commodityList.stream()
                                        .map(x -> String.format("%s(%s)", x.getCommodityName(), x.getCommodityId()))
                                        .collect(Collectors.joining(","))
                        )
                );
            }
        }

        for (Category category : categoryList) {
            categoryMapper.deleteByPrimaryKey(category.getId());
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/addCommodity", method = {RequestMethod.POST})
    public RestResponse<Void> addCommodity(
            Commodity commodity
    ) {
        commodity.setCommodityId(Utils.getUUIDString());
        commodityMapper.insert(commodity);
        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/updateCommodity", method = {RequestMethod.POST})
    public RestResponse<Void> updateCommodity(
            Commodity c
    ) {
        Commodity commodity = commodityMapper.selectByPrimaryKey(c.getId());
        if (commodity == null) {
            return RestResponse.getFailedResponse(RcError, "未查询到商品");
        }

        // check
        if (StringUtils.isBlank(c.getCommodityName())) {
            return RestResponse.getFailedResponse(RcError, "商品名称为空");
        }

        if (StringUtils.isBlank(c.getCommoditySpec())) {
            return RestResponse.getFailedResponse(RcError, "商品规格为空");
        }

        if (c.getCommodityPrice() == null) {
            return RestResponse.getFailedResponse(RcError, "商品价格为空");
        }

        if (StringUtils.isBlank(c.getCategoryId())) {
            return RestResponse.getFailedResponse(RcError, "商品类别为空");
        }

        if (c.getHotSales() == null) {
            return RestResponse.getFailedResponse(RcError, "商品热卖属性为空");
        }

        commodity.setCommodityName(c.getCommodityName());
        commodity.setCommodityPrice(c.getCommodityPrice());
        commodity.setCommoditySpec(c.getCommoditySpec());
        commodity.setCommodityPics(c.getCommodityPics());
        commodity.setCategoryId(c.getCategoryId());
        commodity.setHotSales(c.getHotSales());

        commodityMapper.updateByPrimaryKey(commodity);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delCommodity", method = {RequestMethod.POST})
    public RestResponse<Void> delCommodity(
            String ids
    ) {
        List<Long> idList = Arrays.asList(ids.split(",", -1))
                .stream().filter(x -> StringUtils.isNotBlank(x) && StringUtils.isNumeric(x))
                .map(x -> Long.valueOf(x))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return RestResponse.getFailedResponse(RcError, "没有要删除的商品");
        }

        idList.forEach(x -> commodityMapper.delCommodityById(x));

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/getCommodityList", method = {RequestMethod.POST})
    public RestResponse<List<CommodityDetailVO>> getCommodityList() {
        // 查所有大类
        List<Category> categoryList = categoryMapper.select(new Category());

        // 查所有商品
        List<Commodity> commodityList = commodityMapper.select(new Commodity());

        List<CommodityDetailVO> commodityDetailVOList = categoryList.stream().map(x -> {
            CommodityDetailVO commodityDetailVO = new CommodityDetailVO();
            commodityDetailVO.setCategory(x);
            commodityDetailVO.setCommodityList(
                    commodityList.stream()
                            .filter(y -> y.getCategoryId().equalsIgnoreCase(x.getCategoryId()))
                            .collect(Collectors.toList())
            );
            return commodityDetailVO;
        }).collect(Collectors.toList());

        // 增加热销
        CommodityDetailVO commodityDetailVO = new CommodityDetailVO();
        Category category = new Category();
        category.setCategoryName("热销");
        commodityDetailVO.setCategory(category);
        commodityDetailVO.setCommodityList(
                commodityList.stream().filter(x -> !x.getHotSales().equals(Byte.valueOf("0")))
                .collect(Collectors.toList())
        );

        commodityDetailVOList.add(commodityDetailVO);

        return RestResponse.getSuccesseResponse(commodityDetailVOList);
    }
}
