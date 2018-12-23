package com.bethena.learningrxjava2;

import java.util.concurrent.CountDownLatch;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxObserver<T> implements Observer<T> {

    CountDownLatch latch;

    RxObserver(CountDownLatch latch){
        this.latch = latch;
    }

    @Override
    public void onSubscribe(Disposable d) {
        System.out.println("----onSubscribe----" + Thread.currentThread().getName());
    }
    @Override
    public void onNext(Object o) {
        System.out.println("----onNext----" + Thread.currentThread().getName());
        System.out.println("onNext----" + o);
    }

    @Override
    public void onError(Throwable e) {
        System.out.println("----onError----" + Thread.currentThread().getName() + "----" + e.getMessage());
        latch.countDown();
    }

    @Override
    public void onComplete() {
        System.out.println("----onComplete----" + Thread.currentThread().getName());
        latch.countDown();
    }
}
