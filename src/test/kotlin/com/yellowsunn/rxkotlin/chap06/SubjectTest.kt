package com.yellowsunn.rxkotlin.chap06

import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class SubjectTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun publishSubject_test() {
        // 소비자가 구독한 시점 이후에 통지된 데이터만 소비자에게 전달되는 PublishSubject (Hot Publisher)
        val subject = PublishSubject.create<Int>()
        subject.subscribe { log.info("소비자 1 : {}", it) }
        subject.onNext(3500)
        subject.subscribe { log.info("소비자 2 : {}", it) }
        subject.onNext(3300)
        subject.subscribe { log.info("소비자 3 : {}", it) }
        subject.onNext(3400)

        subject.subscribe(
            { log.info("소비자 4 : {}", it) },
            { e -> log.error("{}", e.message, e) },
            { log.info("onComplete") }
        )

        subject.onComplete()
    }

    @Test
    fun asyncSubject_test() {
        // 구독 시점과 상관없이 모든 소비자들이 마지막으로 통지된 데이터만 전달 받음
        val subject = AsyncSubject.create<Int>()
        subject.subscribe { log.info("소비자 1 : {}", it) }
        subject.onNext(3500)
        subject.subscribe { log.info("소비자 2 : {}", it) }
        subject.onNext(3300)
        subject.subscribe { log.info("소비자 3 : {}", it) }
        subject.onNext(3400)

        subject.onComplete()

        subject.subscribe(
            { log.info("소비자 4 : {}", it) },
            { e -> log.error("{}", e.message, e) },
            { log.info("onComplete") }
        )
    }

    @Test
    fun behaviorSubject_test() {
        val subject = BehaviorSubject.create<Int>()
        subject.subscribe { log.info("소비자 1 : {}", it) }
        subject.onNext(3500)
        subject.subscribe { log.info("소비자 2 : {}", it) }
        subject.onNext(3300)
        subject.subscribe { log.info("소비자 3 : {}", it) }
        subject.onNext(3400)

        subject.onComplete()

        subject.subscribe(
            { log.info("소비자 4 : {}", it) }, // 처리가 완료된 이후에 구독하면 완료나 에러 통지만 전달 받는다
            { e -> log.error("{}", e.message, e) },
            { log.info("onComplete") }
        )
    }

    @Test
    fun replaySubject_test() {
        val subject = ReplaySubject.create<Int>()
        subject.subscribe { log.info("소비자 1 : {}", it) }
        subject.onNext(3500)
        subject.subscribe { log.info("소비자 2 : {}", it) }
        subject.onNext(3300)
        subject.subscribe { log.info("소비자 3 : {}", it) }
        subject.onNext(3400)

        subject.onComplete()

        subject.subscribe(
            { log.info("소비자 4 : {}", it) },
            { e -> log.error("{}", e.message, e) }, // 처리가 완료된 이후에도 통지된 데이터를 전달받는다
            { log.info("onComplete") }
        )
    }
}