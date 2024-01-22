package com.briup.cms;

import com.briup.cms.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BriupCmsApplicationTests {

    @Autowired
    IUserService userService;
    @Test
    void contextLoads() {
        System.out.println(userService.getById(1730424937507143682L));
    }


}
