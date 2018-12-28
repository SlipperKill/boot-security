package com.boot.security.server.controller;

import java.util.List;
import java.util.Map;

import com.boot.security.server.dao.BusinessInfoDao;
import com.boot.security.server.utils.ColumnProUtil;
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
import com.boot.security.server.dao.PayAccountDao;
import com.boot.security.server.model.PayAccount;

import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

@RestController
@RequestMapping("/payAccounts")
public class PayAccountController {

    @Resource
    private PayAccountDao payAccountDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public PayAccount save(@RequestBody PayAccount payAccount) {
        payAccountDao.save(payAccount);

        return payAccount;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public PayAccount get(@PathVariable String id) {
        return payAccountDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public PayAccount update(@RequestBody PayAccount payAccount) {
        payAccountDao.update(payAccount);

        return payAccount;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return payAccountDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<PayAccount> list(PageTableRequest request) {
                return payAccountDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }, new PageTableHandler.OrderHandler() {
            @Override
            public PageTableRequest order(PageTableRequest request) {
                Map<String, String> map = ColumnProUtil.getColumnPro(PayAccountDao.class, "BaseResultMap");
                String orderBy = ColumnProUtil.pro2Column(request, map);
                request.getParams().put("orderBy", orderBy);
                return request;
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        payAccountDao.delete(id);
    }
}
