package com.boot.security.server.controller;

import java.util.List;
import java.util.Map;

import com.boot.security.server.dao.BusinessInfoDao;
import com.boot.security.server.utils.ColumnProUtil;
import com.boot.security.server.utils.UUIDUtil;
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
import com.boot.security.server.dao.CodeManDao;
import com.boot.security.server.model.CodeMan;

import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

@RestController
@RequestMapping("/codeMans")
public class CodeManController {

    @Resource
    private CodeManDao codeManDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public CodeMan save(@RequestBody CodeMan codeMan) {
        codeMan.setId(UUIDUtil.getUUID());
        codeManDao.save(codeMan);

        return codeMan;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public CodeMan get(@PathVariable String id) {
        return codeManDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public CodeMan update(@RequestBody CodeMan codeMan) {
        codeManDao.update(codeMan);

        return codeMan;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return codeManDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<CodeMan> list(PageTableRequest request) {
                return codeManDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }, new PageTableHandler.OrderHandler() {
            @Override
            public PageTableRequest order(PageTableRequest request) {
                Map<String,String> map = ColumnProUtil.getColumnPro(CodeManDao.class,"BaseResultMap");
                String orderBy = ColumnProUtil.pro2Column(request,map);
                request.getParams().put("orderBy",orderBy);
                return request;
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable String id) {
        codeManDao.delete(id);
    }
}
