package com.yellowsunn.rxkotlin.chap0506

import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ObservableErrorHandlingTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun general_error_handing_test() {
        Observable.just(5)
            .flatMap { num ->
                Observable.interval(200L, TimeUnit.MILLISECONDS)
                    .doOnNext { log.info("{}", it) }
                    .take(5)
                    .map { num / it }
            }
            .subscribe(
                { log.info("{}", it) },
                { e -> log.error("{}", e.message, e) }, // error handling
                { log.info("complete") }
            )

        TimeUnit.SECONDS.sleep(1L)
    }

    @Test
    fun onErrorReturn_test() {
        Observable.just(5)
            .flatMap { num ->
                Observable.interval(200L, TimeUnit.MILLISECONDS)
                    .doOnNext { log.info("{}", it) }
                    .take(5)
                    .map { num / it }
                    .onErrorReturn { e ->
                        if (e is ArithmeticException) {
                            log.warn("계산 처리 에러. message={}", e.message)
                        }
                        -1
                    }
            }
            .subscribe(
                { log.info("{}", it) },
                { e -> log.error("{}", e.message, e) },
                { log.info("complete") }
            )

        TimeUnit.SECONDS.sleep(1L)
    }

    @Test
    fun onErrorResumeNext_test() {
        Observable.just(5)
            .flatMap { num ->
                Observable.interval(200L, TimeUnit.MILLISECONDS)
                    .doOnNext { log.info("{}", it) }
                    .take(5)
                    .map { num / it }
                    .onErrorResumeNext { e: Throwable ->
                        log.warn("message={}", e.message, e)
                        Observable.interval(200L, TimeUnit.MILLISECONDS)
                            .take(5)
                            .skip(1)
                            .map { num * it }
                    }
            }
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(1L)
    }

    @Test
    fun retry_test() {
        Observable.just(5)
            .flatMap { num ->
                Observable.interval(200L, TimeUnit.MILLISECONDS)
                    .map { num / it }
                    .doOnError { e -> log.error("message={}", e.message, e) }
                    .retry(5)
                    .onErrorReturn { -1 }
            }
            .subscribe(
                { log.info("{}", it) },
                { e -> log.error("{}", e.message, e) },
                { log.info("complete") }
            )

        TimeUnit.SECONDS.sleep(5L)
    }
}