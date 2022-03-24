package com.offcn.sms;

import com.onffcn.sms.utils.SmsUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SmsUtil.class)
@RunWith(SpringRunner.class)
public class SMSTest {
    @Autowired
    private SmsUtil smsUtil;
    @Test
    public void fdx(){
     smsUtil.sendSms("17379635493","4556");
    }
}
