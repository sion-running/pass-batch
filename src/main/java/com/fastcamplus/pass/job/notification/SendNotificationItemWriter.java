package com.fastcamplus.pass.job.notification;

import com.fastcamplus.pass.adaptor.message.KakaoTalkMessageAdapter;
import com.fastcamplus.pass.repository.notification.NotificationEntity;
import com.fastcamplus.pass.repository.notification.NotificationRepository;
import com.fastcamplus.pass.util.LocalDateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class SendNotificationItemWriter implements ItemWriter<NotificationEntity> {
    private final NotificationRepository notificationRepository;
    private final KakaoTalkMessageAdapter kakaoTalkMessageAdapter;

    public SendNotificationItemWriter(NotificationRepository notificationRepository, KakaoTalkMessageAdapter kakaoTalkMessageAdapter) {
        this.notificationRepository = notificationRepository;
        this.kakaoTalkMessageAdapter = kakaoTalkMessageAdapter;
    }

    @Override
    public void write(List<? extends NotificationEntity> notificationEntities) throws Exception {
        int count = 0;

        for (NotificationEntity notificationEntity : notificationEntities) { // chunck 단위로 받아오기 때문에 List로 받아옴
            boolean successful = kakaoTalkMessageAdapter.sendKakaoTalkMessage(notificationEntity.getUuid(), notificationEntity.getText());
            if (successful) {
                notificationEntity.setSent(true);
                notificationEntity.setSentAt(LocalDateTime.now());

                notificationRepository.save(notificationEntity);
                count++;
            }
        }

        log.info("SendNotificationItemWriter - write: 수업 전 알람 {}/{}건 전송 성공", count, notificationEntities.size());
    }
}
