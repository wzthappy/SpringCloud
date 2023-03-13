package cn.itcast.user.web;

import cn.itcast.user.config.PatternProperties;
import cn.itcast.user.pojo.User;
import cn.itcast.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/user")
//@RefreshScope  // 属性刷新
public class UserController {

    @Autowired
    private UserService userService;

//    @Value("${pattern.dateformat}")
//    private String dateformat;

    @Autowired
    private PatternProperties patternProperties;

    @GetMapping("/prop")
    public PatternProperties properties () {
        return patternProperties;
    }

    @GetMapping("/now")
    public String now () {
        return LocalDateTime.now().format(DateTimeFormatter
            .ofPattern(patternProperties.getDateformat()));
    }

    /**
     * 路径： /user/110
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id) throws InterruptedException {
        if (id == 1) {
            // 休眠，触发断融
            Thread.sleep(60);
        } else if (id == 2) {
            throw new RuntimeException("异常，触发异常比例熔断");
        }
        return userService.queryById(id);
    }
}
