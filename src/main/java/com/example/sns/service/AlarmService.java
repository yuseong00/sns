package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import com.example.sns.model.entity.AlarmEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.AlarmEntityRepository;
import com.example.sns.repository.EmitterRepository;
import com.example.sns.repository.UserEntityRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static String ALARM_NAME = "alarm";

    private final AlarmEntityRepository alarmEntityRepository;
    private final EmitterRepository emitterRepository;
    private final UserEntityRepository userEntityRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;  // 60분



    public void send(AlarmType type, AlarmArgs args, Integer receiverId) {
        UserEntity userEntity = userEntityRepository.findById(receiverId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND));
        AlarmEntity entity = AlarmEntity.of(type, args, userEntity);
        alarmEntityRepository.save(entity);
        emitterRepository.get(receiverId).ifPresentOrElse(it -> {
                    try {
                        it.send(SseEmitter.event()
                                .id(entity.getId().toString())
                                .name(ALARM_NAME)
                                .data(new AlarmNotification()));
                    } catch (IOException exception) {
                        emitterRepository.delete(receiverId);
                        throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
                    }
                },
                () -> log.info("No emitter founded")
        );
    }


    public SseEmitter connectNotification(Integer userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);
        emitter.onCompletion(() -> emitterRepository.delete(userId));
        emitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            log.info("send");
            emitter.send(SseEmitter.event()
                    .id("id")
                    .name(ALARM_NAME)
                    .data("connect completed"));
        } catch (IOException exception) {
            throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
        }
        return emitter;
    }


    @Data
    @AllArgsConstructor
    public static class AlarmNotification {
    }

}
