package com.bethena.learningrxjava2;

import android.util.Log;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJava2Test {

    final String TAG = getClass().getSimpleName();

    final CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void observableTest() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                System.out.println("----subscribe----" + Thread.currentThread().getName());


                emitter.onNext(1);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        System.out.println("----accept----" + Thread.currentThread().getName());
                    }
                }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("----onSubscribe----" + Thread.currentThread().getName());


            }

            @Override
            public void onNext(Object o) {
                System.out.println("----onNext----" + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("----onError----" + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                System.out.println("----onComplete----" + Thread.currentThread().getName());
            }
        });
    }

}
