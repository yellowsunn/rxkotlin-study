package com.yellowsunn.rxkotlin.chap09

import io.reactivex.Observable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BlockingTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun blockingFirst_test() {
        val first: Long = Observable.interval(200, TimeUnit.MILLISECONDS)
            .take(5)
            .blockingFirst()

        assertEquals(first, 0L)
    }

    @Test
    fun blockingLast_test() {
        val last: Long = Observable.interval(200, TimeUnit.MILLISECONDS)
            .take(5)
            .blockingLast()

        assertEquals(last, 4L)
    }

    @Test
    fun blockingSingle_test() {
        // 1개를 통지하지 않으면 예외 발생
        val single = Observable.interval(200, TimeUnit.MILLISECONDS)
            .take(2)
            .filter { it > 0L }
            .blockingSingle()

        assertEquals(single, 1L)
    }

    @Test
    fun blockingGet_test() {
        // 생산자가 0개 또는 1개의 데이터를 통지하는 경우 (Maybe 타입)
        val getData: Long? = Observable.empty<Long>()
            .firstElement()
            .blockingGet()

        assertNull(getData)
    }

    @Test
    fun blockingIterable_test() {
        val iterable: Iterable<Long> = Observable.interval(200, TimeUnit.MILLISECONDS)
            .take(5)
            .blockingIterable()

        for (data in iterable) {
            log.info("{}", data)
            assertNotNull(data)
        }
    }

    @Test
    fun blockingForEach_test() {
        Observable.interval(200, TimeUnit.MILLISECONDS)
            .take(5)
            .blockingForEach {
                log.info("{}", it)
                assertNotNull(it)
            }
    }

    @Test
    fun blockSubscribe_Test() {
        Observable.interval(200, TimeUnit.MILLISECONDS)
            .take(5)
            .blockingSubscribe { it * 2 }
    }
}