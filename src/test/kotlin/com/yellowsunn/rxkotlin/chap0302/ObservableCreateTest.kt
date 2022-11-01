package com.yellowsunn.rxkotlin.chap0302

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ObservableCreateTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun test() {
        val observable: Observable<String> = Observable.create { emitter ->
            val datas = listOf("Hello", "RxKotlin")
            datas.forEach {
                // 구독이 해지되면 처리 중단
                if (emitter.isDisposed) {
                    return@forEach
                }
                // 데이터 발행
                emitter.onNext(it)
            }
            // 데이터 발행완료를 알린다
            emitter.onComplete()
        }

        observable.observeOn(Schedulers.computation())
            .subscribe(
                { log.info("onNext {}", it) },
                { e -> log.error("onError. message={}", e.message, e) },
                { log.info("onComplete") }
            )

        TimeUnit.MILLISECONDS.sleep(500L)
    }
}