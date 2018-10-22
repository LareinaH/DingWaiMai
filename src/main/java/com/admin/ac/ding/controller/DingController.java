package com.admin.ac.ding.controller;

import com.admin.ac.ding.enums.OrderStatus;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.CacheService;
import com.admin.ac.ding.service.DingService;
import com.admin.ac.ding.utils.Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.admin.ac.ding.constants.Constants.RcError;

@RestController
@RequestMapping(value = "/ding", produces = "application/json; charset=UTF-8")
public class DingController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(DingController.class);

    @Autowired
    DingService dingService;

    @Autowired
    AdminController adminController;

    @Autowired
    CacheService cacheService;

    @Value("${ding.app.meetingbook.agentid}")
    Long meetingBookAppAgentId;

    @Value("${ding.app.repair.agentid}")
    Long repairAppAgentId;

    @Value("${ding.app.suggest.agentid}")
    Long suggestAppAgentId;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CommodityMapper commodityMapper;

    @Autowired
    CartsMapper cartsMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @RequestMapping(value = "/getCommodityList", method = {RequestMethod.GET})
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

        commodityDetailVOList.add(0, commodityDetailVO);

        return RestResponse.getSuccesseResponse(commodityDetailVOList);
    }

    @RequestMapping(value = "/getCartsList", method = {RequestMethod.GET})
    public RestResponse<List<CartsDetailVO>> getCartsList(
            @RequestParam(value = "userId") String userId
    ) {
        // 查所有商品
        List<Commodity> commodityList = commodityMapper.select(new Commodity());
        Map<String, Commodity> commodityMap = commodityList.stream()
                .collect(Collectors.toMap(Commodity::getCommodityId, Function.identity()));

        Example example = new Example(Carts.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", false);
        criteria.andEqualTo("userId", userId);
        criteria.andGreaterThan("commodityAmount", 0);
        List<Carts> cartsList = cartsMapper.selectByExample(example).stream().collect(Collectors.toList());
        List<CartsDetailVO> cartsDetailVOList = cartsList.stream().map(x -> {
            if (commodityMap.containsKey(x.getCommodityId())) {
                Commodity commodity = commodityMap.get(x.getCommodityId());
                CartsDetailVO cartsDetailVO = new CartsDetailVO();
                BeanUtils.copyProperties(x , cartsDetailVO);
                cartsDetailVO.setCommodity(commodity);

                return cartsDetailVO;
            } else {
                return null;
            }

        }).filter(x -> x != null).collect(Collectors.toList());

        return RestResponse.getSuccesseResponse(cartsDetailVOList);
    }

    @RequestMapping(value = "/updateCarts", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<List<CartsDetailVO>> updateCarts(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "commodityId") String commodityId,
            @RequestParam(value = "increase") Integer increase
    ) {
        // 检查商品是否存在
        Commodity c = new Commodity();
        c.setCommodityId(commodityId);
        Commodity commodity = commodityMapper.selectOne(c);
        if (commodity == null) {
            return RestResponse.getFailedResponse(
                    RcError,
                    String.format("商品:%s不存在", commodityId)
            );
        }

        // 查找用户对应的购物车商品列表
        Carts paramCarts = new Carts();
        paramCarts.setUserId(userId);
        paramCarts.setCommodityId(commodityId);
        Carts carts = cartsMapper.selectOne(paramCarts);
        if (carts == null) {
            // 增加之
            Carts newCarts = new Carts();
            newCarts.setUserId(userId);
            newCarts.setCommodityId(commodityId);
            newCarts.setCommodityAmount(increase);
            cartsMapper.insert(newCarts);
        } else {
            Integer amount = carts.getCommodityAmount() + increase;
            if (amount < 0) {
                carts.setCommodityAmount(0);
            } else {
                carts.setCommodityAmount(amount);
            }
            carts.setGmtModify(new Date());
            cartsMapper.updateByPrimaryKey(carts);
        }

        return getCartsList(userId);
    }

    @RequestMapping(value = "/getOrderList", method = {RequestMethod.GET})
    @Transactional
    public RestResponse<PageInfo<OrderDetailVO>> getOrderList(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "orderStatus", required = false) String orderStatus,
            @RequestParam(value = "gmtStart", required = false) String gmtStart,
            @RequestParam(value = "gmtEnd", required = false) String gmtEnd,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        Example example1 = new Example(Order.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("isDeleted", false);
        if (StringUtils.isNotBlank(userId)) {
            criteria1.andEqualTo("userId", userId);
        }
        
        if (StringUtils.isNotBlank(orderStatus)) {
            criteria1.andIn("orderStatus", Arrays.asList(orderStatus.split(",", -1)));
        }

        if (StringUtils.isNotBlank(gmtStart) && StringUtils.isNotBlank(gmtEnd)) {
            criteria1.andBetween("gmtCreate", gmtStart, gmtEnd);
        }

        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("gmt_create DESC");
        List<Order> orderList = orderMapper.selectByExample(example1);
        if (CollectionUtils.isEmpty(orderList)) {
            return RestResponse.getSuccesseResponse(
                    new PageInfo<>(new ArrayList<>())
            );
        }

        PageInfo pageInfo = new PageInfo(orderList);

        // 查找所有的订单商品列表
        Example example = new Example(OrderDetail.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", false);
        criteria.andIn("orderId", orderList.stream().map(x -> x.getOrderId()).collect(Collectors.toList()));
        Map<String, List<OrderDetail>> ordersMap = orderDetailMapper.selectByExample(example).stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));

        List<OrderDetailVO> orderDetailVOList = orderList.stream().map(x -> {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(x, orderDetailVO);
            if (ordersMap.containsKey(x.getOrderId())) {
                orderDetailVO.setOrderDetailList(ordersMap.get(x.getOrderId()));
            } else {
                orderDetailVO.setOrderDetailList(new ArrayList<>());
            }

            try {
                orderDetailVO.setUserDetail(cacheService.getUserDetail(x.getUserId()));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (DingServiceException e) {
                e.printStackTrace();
            }

            return orderDetailVO;
        }).collect(Collectors.toList());

        pageInfo.setList(orderDetailVOList);

        return RestResponse.getSuccesseResponse(pageInfo);
    }

    @RequestMapping(value = "/submitOrder", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<PageInfo<OrderDetailVO>> submitOrder(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "orderTimeType") String orderTimeType
    ) {
        // 查找用户购物车中的所有商品
        Example example = new Example(Carts.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", false);
        criteria.andEqualTo("userId", userId);
        criteria.andGreaterThan("commodityAmount", 0);
        List<Carts> cartsList = cartsMapper.selectByExample(example).stream().collect(Collectors.toList());

        // 查所有商品
        List<Commodity> commodityList = commodityMapper.select(new Commodity());
        Map<String, Commodity> commodityMap = commodityList.stream()
                .collect(Collectors.toMap(Commodity::getCommodityId, Function.identity()));

        // 将购物车中失效商品排除
        cartsList = cartsList.stream()
                .filter(x -> commodityMap.containsKey(x.getCommodityId())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartsList)) {
            return RestResponse.getFailedResponse(RcError, "尚未添加任何商品到购物车");
        }

        // 生成订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(Utils.getUUIDString());
        order.setOrderStatus(OrderStatus.SUBMITTED.name());
        order.setOrderTimeType(orderTimeType);
        order.setOrderPrice(
                cartsList.stream().map(x -> {
                    if (commodityMap.containsKey(x.getCommodityId())) {
                        // 商品单价 * 购物中的数量
                        return commodityMap.get(x.getCommodityId()).getCommodityPrice().multiply(
                                new BigDecimal(x.getCommodityAmount())
                        );
                    } else {
                        return BigDecimal.ZERO;
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        orderMapper.insert(order);

        // 写入order detail
        List<OrderDetail> orderDetailList = cartsList.stream().map(x -> {
            Commodity commodity = commodityMap.get(x.getCommodityId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setCommodityId(commodity.getCommodityId());
            orderDetail.setCommodityPrice(commodity.getCommodityPrice());
            orderDetail.setCommoditySpec(commodity.getCommoditySpec());
            orderDetail.setCommodityAmount(x.getCommodityAmount());
            orderDetail.setCommodityPics(commodity.getCommodityPics());
            orderDetail.setCommodityName(commodity.getCommodityName());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailMapper.insertList(orderDetailList);

        // 清除购物车
        cartsMapper.delCartsForUser(userId);

        return getOrderList(userId,null,null,1, 10);
    }

    @RequestMapping(value = "/userCancelOrder", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<Void> userCancelOrder(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "orderId") String orderId
    ) {
        // 查询一下订单
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(userId);
        Order theOrder = orderMapper.selectOne(order);
        if (theOrder == null) {
            return RestResponse.getFailedResponse(RcError, "未查找到用户名下该订单");
        }

        if (!OrderStatus.SUBMITTED.name().equals(theOrder.getOrderStatus())) {
            return RestResponse.getFailedResponse(RcError, "仅已提交状态的订单可以取消");
        }

        LocalDateTime orderGmtCreate
                = LocalDateTime.ofInstant(theOrder.getGmtCreate().toInstant(), ZoneId.systemDefault());

        LocalDateTime now = LocalDateTime.now();
        // 昨天0点
        LocalDateTime lastDayZero = now.minusDays(1)
                .with(ChronoField.HOUR_OF_DAY, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MILLI_OF_SECOND, 0);

        // 今天0点
        LocalDateTime lastDayEnd = now
                .with(ChronoField.HOUR_OF_DAY, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MILLI_OF_SECOND, 0);

        // 今天10点
        LocalDateTime todayPoint = now
                .with(ChronoField.HOUR_OF_DAY, 10)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MILLI_OF_SECOND, 0);

        if (
                (
                        orderGmtCreate.compareTo(lastDayZero) >= 0
                                && orderGmtCreate.compareTo(lastDayEnd) < 0
                                && now.compareTo(todayPoint) < 0
                                && now.compareTo(lastDayEnd) >= 0
                        )
                || (orderGmtCreate.compareTo(lastDayEnd) >= 0)
                ) {
            // 今天10点前取消昨天的订单
            // 今天的订单取消
            theOrder.setOrderStatus(OrderStatus.USER_CANCEL.name());
            orderMapper.updateByPrimaryKey(theOrder);
            return RestResponse.getSuccesseResponse();
        }

        return RestResponse.getFailedResponse(RcError, "当天10点前仅可取消昨日订单");
    }

    @RequestMapping(value = "/getOrderCommoditySummary", method = {RequestMethod.GET})
    @Transactional
    public RestResponse<List<Map<String, Object>>> getOrderCommoditySummary(
            @RequestParam(value = "date", required = false) String date
    ) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        if (StringUtils.isNotBlank(date)) {
            now = LocalDateTime.of(LocalDate.parse(date, df), LocalDateTime.MIN.toLocalTime());
        }

        // 今天0点
        LocalDateTime lastDayZero = now
                .with(ChronoField.HOUR_OF_DAY, 0)
                .with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0)
                .with(ChronoField.MILLI_OF_SECOND, 0);

        // 今天最后一秒
        LocalDateTime lastDayEnd = now
                .with(ChronoField.HOUR_OF_DAY, 23)
                .with(ChronoField.MINUTE_OF_HOUR, 59)
                .with(ChronoField.SECOND_OF_MINUTE, 59)
                .with(ChronoField.MILLI_OF_SECOND, 999);

        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return RestResponse.getSuccesseResponse(
                orderMapper.getOrderCommoditySummary(
                        df.format(lastDayZero),
                        df.format(lastDayEnd)
                )
        );
    }
}
