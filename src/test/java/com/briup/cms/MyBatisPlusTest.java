package com.briup.cms;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.common.model.entity.Slideshow;
import com.briup.cms.dao.SlideshowMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author YuYan
 * @date 2023-11-29 10:59:36
 */
@SpringBootTest
public class MyBatisPlusTest {

    @Autowired
    SlideshowMapper slideshowMapper;

    @Test
    void test0() {
        /* 根据主键查询 */
        // System.out.println(slideshowMapper.selectById(1));;

        /* 条件检索1 */
        /* 使用Map封装条件只能做等值判断 */
        // Map<String, Object> map = new HashMap<>();
        // map.put("status", "启用");
        // List<Slideshow> list = slideshowMapper.selectByMap(map);
        // list.forEach(System.out::println);

        /* 条件检索2 */
        /* 使用一个QueryWrapper，可以做很丰富的判断，例如大于、小于、大于等于、小于等于、between、int*/
        /*QueryWrapper<Slideshow> qw = new QueryWrapper<>();
        qw.eq("status", "启用");
        // selectList()：条件检索
        List<Slideshow> list = slideshowMapper.selectList(qw);
        list.forEach(System.out::println);*/

        /* 条件检索3 */
        /* LambdaQueryWrapper，使用Lambda表达式表示字段 */
        LambdaQueryWrapper<Slideshow> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Slideshow::getStatus, "启用");
        slideshowMapper.selectList(lqw)
                .forEach(System.out::println);
    }

    @Test
    void test1() {
        /* 分页 */
        IPage<Slideshow> page = new Page<>(1, 2);
        slideshowMapper.selectPage(page, new QueryWrapper<>());
        /* 从分页对象中取出数据 */
        page.getRecords()
            .forEach(System.out::println);
    }

    @Test
    void test2() {
        // 删除id为1的数据
        slideshowMapper.deleteById(4);

        // slideshowMapper.insert();
        // slideshowMapper.updateById(slideshow)
    }
}
