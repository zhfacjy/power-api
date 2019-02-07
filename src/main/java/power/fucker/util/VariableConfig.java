package power.fucker.util;

import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 * Created by 浩发 on 2019/2/4 13:54
 * 各种变量配置
 */
public class VariableConfig {

    // 订阅emq的主题
    public static final Topic[] Topics = {
            new Topic("mqtt/test2", QoS.EXACTLY_ONCE), //  2 只有一次
            new Topic("mqtt/test3", QoS.AT_LEAST_ONCE),  // 1 至少一次
            new Topic("mqtt/test1", QoS.AT_MOST_ONCE)  // 0 至多一次
    };

    // kafka的主题
    public static final String KafkaTopic = "kafka.power";
}
