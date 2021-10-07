package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.service.BlogService;
import com.markerhub.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2021-09-09
 */
@RestController
public class BlogController {
    @Autowired
    BlogService blogService;

    /**
     * 分页功能
     * @param currentPage
     * @return
     */
    @GetMapping("/blogs")
    public Result paging(@RequestParam(defaultValue = "1") Integer currentPage){//@RequestParam(defaultValue = "1")默认值为1
        Page page=new Page(currentPage,5);
        //翻页查询
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));//orderByDesc倒序
        return Result.succ(pageData);

    }
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id){//@PathVariable路径变量。

        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"该博客已被删除");
        return Result.succ(blog);

    }
    //RequiresAuthentication 告诉项目需要认证才能编辑
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){
//        Assert.isTrue(false, "公开版不能任意编辑！");
        Blog temp=null;
        if(blog.getId() !=null){
            temp=blogService.getById(blog.getId());
            //只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(),"没有权限编辑");

        }else {
            temp=new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);

        }
        BeanUtil.copyProperties(blog,temp,"id","userId","created","status");
        blogService.saveOrUpdate(temp);
        return Result.succ(null);

    }

}
