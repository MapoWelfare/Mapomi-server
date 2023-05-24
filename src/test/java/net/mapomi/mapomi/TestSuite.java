package net.mapomi.mapomi;

import net.mapomi.mapomi.service.AccompanyServiceTest;
import net.mapomi.mapomi.service.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserServiceTest.class,
        AccompanyServiceTest.class
})
@SpringBootTest
public class TestSuite {
    // 빈 내용
}
