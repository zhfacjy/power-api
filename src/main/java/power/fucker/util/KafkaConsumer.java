package power.fucker.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import power.fucker.repository.DictRepository;

import java.util.Optional;

/**
 * Created by 浩发 on 2019/1/28 11:30
 * kafka消费者
 */
@Component
public class KafkaConsumer {

    @Autowired
    private DictRepository dictRepository;
    private Integer index = 0;

    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(id = "power", topics = VariableConfig.KafkaTopic)
    private void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
//            logger.info("kafka消息接收 Topic:" + topic);
//            logger.info("kafka接收消息内容 Message:" + message);
            index += 1;
//            Dict user = JSONObject.parseObject(String.valueOf(message)).toJavaObject(Dict.class);
//            dictRepository.save(user);
        }
        logger.info("接收>>>>>>>>>>>>>>>>>>>>>>>>>>:"+index+"个kafka消息");
    }
}
