package power.api.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import power.api.model.MeterRecord;
import power.api.repository.DictRepository;
import power.api.repository.MeterRecordRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by 浩发 on 2019/1/28 11:30
 * kafka消费者
 */
@Component
public class KafkaConsumer {

    @Autowired
    private DictRepository dictRepository;

    @Autowired
    private MeterRecordRepository meterRecordRepository;

    private final int MAX_ITEM_SIZE = 30;

    private List<MeterRecord> meterRecordList = new ArrayList<>(MAX_ITEM_SIZE);

    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(id = "power", topics = MessageQueueConfig.KAFKA_TOPIC)
    private void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = kafkaMessage.get().toString();
            // 非58表示结尾
            if (message.length() == 58) {
                MeterRecord meterRecord = parseMessage(message);
//                System.out.println(meterRecord);
                meterRecordList.add(meterRecord);
//                logger.info("缓存条数（未写入）：" + meterRecordList.size());
            } else if (meterRecordList.size() > 0) {
                meterRecordRepository.saveAll(meterRecordList);
//                logger.info("写入条数：" + meterRecordList.size());
                meterRecordList.clear();
            }
        }
    }

    private MeterRecord parseMessage(String message) {
        MeterRecord meterRecord = new MeterRecord();
        String centralNode = message.substring(0, 2);
        String meter = message.substring(2, 4);
        String command = message.substring(4, 6);
        String earlyWarning = message.substring(6, 8);
        meterRecord.setCentralNode(centralNode);
        meterRecord.setMeter(meter);
        meterRecord.setCommand(command);
        meterRecord.setEarlyWarning(earlyWarning);
        float va = (float) (Integer.parseInt(message.substring(8, 12), 16) / 10.0);
        float vb = (float) (Integer.parseInt(message.substring(12, 16), 16) / 10.0);
        float vc = (float) (Integer.parseInt(message.substring(16, 20), 16) / 10.0);
        meterRecord.setVa(va);
        meterRecord.setVb(vb);
        meterRecord.setVc(vc);
        float ia = (float) (Integer.parseInt(message.substring(20, 24), 16) / 10.0);
        float ib = (float) (Integer.parseInt(message.substring(24, 28), 16) / 10.0);
        float ic = (float) (Integer.parseInt(message.substring(28, 32), 16) / 10.0);
        meterRecord.setIa(ia);
        meterRecord.setIb(ib);
        meterRecord.setIc(ic);
        double power = Float.intBitsToFloat(Integer.valueOf(message.substring(32, 40), 16));
        double electricEnergy = Float.intBitsToFloat(Integer.valueOf(message.substring(40, 48), 16));
        int temperature = Integer.parseInt(message.substring(48, 50), 16);
        int currentLimit = Integer.parseInt(message.substring(50, 54), 16);
        String crc = message.substring(54, 58);
        meterRecord.setPower(power);
        meterRecord.setElectricEnergy(electricEnergy);
        meterRecord.setTemperature(temperature);
        meterRecord.setCurrentLimit(currentLimit);
        meterRecord.setCrc(crc);
        return meterRecord;
    }
}
