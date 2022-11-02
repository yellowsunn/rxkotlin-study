package com.yellowsunn.rxkotlin.chap0503

import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ObservableMapTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun map_test() {
        Observable.fromArray(1, 3, 5, 7)
            .map { it * 2 }
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun flatMap_test() {
        // 데이터 처리 순서를 보장하지 않음
        Observable.range(2, 1)
            .flatMap { num ->
                Observable.range(1, 9)
                    .map { "$num * $it = ${num * it}" }
            }
            .subscribe { log.info(it) }
    }

    @Test
    fun flatMap_test2() {
        Observable.range(2, 1)
            .flatMap({ Observable.range(1, 9) }) { sourceData, transformedData ->
                "$sourceData * $transformedData = ${sourceData * transformedData}"
            }
            .subscribe { log.info(it) }
    }

    @Test
    fun concatMap_test() {
        // 데이터 처리 순서를 보장함
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .take(4)
            .skip(2)
            .concatMap { num ->
//            .flatMap { num ->
                Observable.interval(200L, TimeUnit.MILLISECONDS)
                    .take(10)
                    .skip(1)
                    .map { "$num * $it = ${num * it}" }
            }.subscribe(
                { log.info(it) },
                { e -> log.error("{}", e.message, e) }
            )

        TimeUnit.SECONDS.sleep(5L)
    }

    @Test
    fun switchMap_test() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .take(4)
            .skip(2)
            .switchMap { num ->
                // 외부 observable이 새로 producing 되면 중단
                Observable.interval(30L, TimeUnit.MILLISECONDS)
                    .take(10)
                    .skip(1)
                    .map { "$num * $it = ${num * it}" }
            }.subscribe(
                { log.info(it) },
                { e -> log.error("{}", e.message, e) }
            )

        TimeUnit.SECONDS.sleep(5L)
    }
}