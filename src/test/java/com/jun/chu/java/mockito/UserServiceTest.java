package com.jun.chu.java.mockito;

import com.jun.chu.java.mockito.domain.User;
import com.jun.chu.java.mockito.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

/**
 * mockito官网文档以javadoc形式存在,IDE离线可见，使用教程可参见核心类Mockito
 * @author chujun
 * @link https://github.com/mockito/mockito/wiki
 * @date 2020-10-23
 */
public class UserServiceTest {
    @Test
    public void test() {
        //1.mock具体接口和类
        UserService userService = Mockito.mock(UserService.class);
        //2.在执行之前stub
        User stubUser = new User("1", "12");
        Mockito.when(userService.findByName("a")).thenReturn(stubUser);
        //3.方法执行
        Assert.assertEquals(stubUser.toString(), userService.findByName("a").toString());
        Assert.assertNull(userService.findByName("b"));
        //4.验证
        Mockito.verify(userService).findByName("a");
        Mockito.verify(userService).findByName("b");
        Mockito.verify(userService, Mockito.times(0))
            .findByName("c");
        //4.2调用次数验证
        Assert.assertNull(userService.findByName("b"));
        Mockito.verify(userService, Mockito.times(2))
            .findByName("b");
    }

    @Test
    public void testBDD() {
        //采用BDD测试方式Behavior-Driven Development
        //1.mock具体接口/类
        UserService userService = BDDMockito.mock(UserService.class);
        //2.在执行之前stub
        User stubUser = new User("1", "12");
        BDDMockito.given(userService.findByName("1")).willReturn(stubUser);
        //3.执行
        Assert.assertEquals(stubUser.toString(), userService.findByName("1").toString());
        Assert.assertNull(userService.findByName("b"));
        //4.验证
        BDDMockito.then(userService).should().findByName("1");
        BDDMockito.then(userService).should().findByName("b");
        BDDMockito.then(userService).should(BDDMockito.times(0))
            .findByName("c");
        //4.2调用次数验证
        Assert.assertNull(userService.findByName("b"));
        BDDMockito.then(userService).should(BDDMockito.times(2))
            .findByName("b");

    }

    @Test
    public void testDefaultMethod(){
        DM dm = BDDMockito.mock(DM.class);
        BDDMockito.given(dm.contract()).willReturn(2);
        BDDMockito.given(dm.default_contract()).willCallRealMethod();
        //TODO:cj to be done
        //assertThat(dm.default_contract()).isEqualTo(3);
    }

    interface DM {
        int contract();
        default int default_contract() { return contract() + 1; }
    }

}
