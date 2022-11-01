package com.yellowsunn.rxkotlin.chap0302

import io.reactivex.BackpressureOverflowStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class BackpressureBufferTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun drop_latest_test() {
        log.info("# start: ${ZonedDateTime.now()}")

        Flowable.interval(300L, TimeUnit.MILLISECONDS)
            .doOnNext { log.info("#interval doOnNext $it") }
            .onBackpressureBuffer(
                2,
                { log.info("overflow!") },
                BackpressureOverflowStrategy.DROP_LATEST
            )
            .doOnNext { log.info("#onBackpressureBuffer doOnNext $it") }
            .observeOn(Schedulers.computation(), false, 1)
            .subscribe(
                {
                    TimeUnit.SECONDS.sleep(1L)
                    log.info("$it")
                },
                { e -> log.error("message=${e.message}", e) },
            )

        TimeUnit.SECONDS.sleep(5L)
    }

    @Test
    fun drop_oldest_test() {
        log.info("# start: ${ZonedDateTime.now()}")

        Flowable.interval(300L, TimeUnit.MILLISECONDS)
            .doOnNext { log.info("#interval doOnNext $it") }
            .onBackpressureBuffer(
                2,
                { log.info("overflow!") },
                BackpressureOverflowStrategy.DROP_OLDEST
            )
            .doOnNext { log.info("#onBackpressureBuffer doOnNext $it") }
            .observeOn(Schedulers.computation(), false, 1)
            .subscribe(
                {
                    TimeUnit.SECONDS.sleep(1L)
                    log.info("$it")
                },
                { e -> log.error("message=${e.message}", e) },
            )

        TimeUnit.SECONDS.sleep(5L)
    }

    @Test
    fun backpressure_drop_test() {
        // 소비자가 처리 중이면 생산자의 모든 요청은 drop
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
            .doOnNext { log.info("#interval doOnNext $it") }
            .onBackpressureDrop() { log.warn("Drop data $it") }
            .observeOn(Schedulers.computation(), false, 2)
            .subscribe(
                {
                    TimeUnit.SECONDS.sleep(1L)
                    log.info("$it")
                },
                { e -> log.error("message=${e.message}", e) },
            )

        TimeUnit.SECONDS.sleep(5L)
    }

    @Test
    fun latest_test() {
        // 소비자의 처리가 모두 완료되면 버퍼 밖에서 대기중인 데이터 중에서 가장 나중에 통지된 데이터 부터 다시 버퍼에 채운다
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
            .doOnNext { log.info("#interval doOnNext $it") }
            .onBackpressureLatest()
            .observeOn(Schedulers.computation(), false, 2)
            .subscribe(
                {
                    TimeUnit.SECONDS.sleep(1L)
                    log.info("$it")
                },
                { e -> log.error("message=${e.message}", e) },
            )

        TimeUnit.SECONDS.sleep(5L)
    }
}