package com.yellowsunn.rxkotlin.chap0504

import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class ObservableCollectionTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun groupBy_test() {
        Observable.range(1, 59)
            .groupBy { it % 2 }
            .subscribe { group ->
                group.subscribe { log.info("key={}, value={}", group.key, it) }
            }
    }

    @Test
    fun toList_test() {
        Observable.range(1, 50)
            .toList()
            .subscribe { nums -> log.info("{}", nums.toString()) }
    }

    @Test
    fun toMap_test() {
        Observable.range(1, 50)
            .toMap(
                { "key$it" },
                { "value$it" }
            )
            .subscribe { maps -> log.info("{}", maps.toString()) }
    }
}