package com.example.sns.producer;

import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {

    private final KafkaTemplate<Integer, AlarmEvent> alarmEventKafkaTemplate;

    @Value("${spring.kafka.topic.notification}")
    private String topic;

    public void send(AlarmEvent event) {
        alarmEventKafkaTemplate.send(topic, event.getReceiverUserId(), event);

        log.info("send fin");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlarmEvent {
        private AlarmType type;
        private AlarmArgs args;
        private Integer receiverUserId = 1;
        //AlarmEntity의 of메소드에 필요한 파라메터값을 send 메소드의 파라메터로 받는다.
        //save 메소드시 필요한  AlarmEntity의 of메소드에 필요한 파라메터값을 send 메소드의 파라메터로 받는다.
        //public static AlarmEntity of(AlarmType alarmType, AlarmArgs args, UserEntity user) {
        //        AlarmEntity entity = new AlarmEntity();
        //        entity.setAlarmType(alarmType);
        //        entity.setArgs(args);
        //        entity.setUser(user);
        //        return entity;
        //    }
    }
}
