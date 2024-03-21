package com.fastcamplus.pass.job.pass;


import com.fastcamplus.pass.config.TestBatchConfig;
import com.fastcamplus.pass.repository.pass.PassEntity;
import com.fastcamplus.pass.repository.pass.PassRepository;
import com.fastcamplus.pass.repository.pass.PassStatus;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpirePassesJobConfig.class, TestBatchConfig.class})
public class ExpirePassesJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PassRepository passRepository;

    @Test
    public void test_expirePassesStep() throws Exception {
        // given
        addPassEntities(10);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(); // 잡을 가져오고
        JobInstance jobInstance = jobExecution.getJobInstance(); // 인스턴스도 가져오기

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("expirePassesJob", jobInstance.getJobName());
    }

    private void addPassEntities(int size) {
        final LocalDateTime now = LocalDateTime.now();
        final Random random = new Random();

        List<PassEntity> passEntities = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            PassEntity pass = new PassEntity();
            pass.setPackageSeq(1);
            pass.setUserId("A" + 1000000 + i);
            pass.setStatus(PassStatus.PROGRESSED);
            pass.setRemainingCount(random.nextInt(11));
            pass.setStartedAt(now.minusDays(60));
            pass.setEndedAt(now.minusDays(1));
            passEntities.add(pass);

        }

        passRepository.saveAll(passEntities);

    }

}
