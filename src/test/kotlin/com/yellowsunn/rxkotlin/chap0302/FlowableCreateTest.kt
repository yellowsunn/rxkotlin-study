package com.yellowsunn.rxkotlin.chap0302

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class FlowableCreateTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun test() {
        val flowable: Flowable<String> = Flowable.create({ emitter ->
            val datas = listOf("Hello", "RxKotlin")
            datas.forEach {
                // 구독이 해지되면 처리 중단
                if (emitter.isCancelled) {
                    return@forEach
                }
                // 데이터 발행
                emitter.onNext(it)
            }
            // 데이터 발행완료를 알린다
            emitter.onComplete()
        }, BackpressureStrategy.BUFFER)

        flowable.observeOn(Schedulers.computation())
            .subscribe(
                { log.info("onNext {}", it) },
                { e -> log.error("onError. message={}", e.message, e) },
                { log.info("onComplete") },
                { subscription -> subscription.request(Long.MAX_VALUE) }
            )

        TimeUnit.MILLISECONDS.sleep(500L)
    }
}