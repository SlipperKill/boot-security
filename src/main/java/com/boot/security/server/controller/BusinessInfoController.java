package com.boot.security.server.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.boot.security.server.utils.ColumnProUtil;
import com.boot.security.server.utils.UUIDUtil;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.security.server.page.table.PageTableRequest;
import com.boot.security.server.page.table.PageTableHandler;
import com.boot.security.server.page.table.PageTableResponse;
import com.boot.security.server.page.table.PageTableHandler.CountHandler;
import com.boot.security.server.page.table.PageTableHandler.ListHandler;
import com.boot.security.server.dao.BusinessInfoDao;
import com.boot.security.server.model.BusinessInfo;

import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

@RestController
@RequestMapping("/businessInfos")
public class BusinessInfoController {

    @Resource
    private BusinessInfoDao businessInfoDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public BusinessInfo save(@RequestBody BusinessInfo businessInfo) {
        businessInfo.setId(UUIDUtil.getUUID());
        businessInfoDao.save(businessInfo);

        return businessInfo;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public BusinessInfo get(@PathVariable String id) {
        return businessInfoDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public BusinessInfo update(@RequestBody BusinessInfo businessInfo) {
        businessInfoDao.update(businessInfo);

        return businessInfo;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return businessInfoDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<BusinessInfo> list(PageTableRequest request) {
                return businessInfoDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }, new PageTableHandler.OrderHandler() {
            @Override
            public PageTableRequest order(PageTableRequest request) {
                Map<String,String> map = ColumnProUtil.getColumnPro(BusinessInfoDao.class,"BaseResultMap");
                String orderBy = ColumnProUtil.pro2Column(request,map);
                request.getParams().put("orderBy",orderBy);
                return request;
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        businessInfoDao.delete(id);
    }
}
