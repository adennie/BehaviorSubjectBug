package com.andydennie.behaviorsubjectbug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Foo foo = new Foo();

        Log.i("BehaviorSubjectBug", "subscribing");
        Subscription sub = foo.getObservable()
                // works if observeOn not used
                //.observeOn(Schedulers.immediate()) // also works
                //.observeOn(AndroidSchedulers.mainThread()) // fails
                //.observeOn(Schedulers.io()) // fails
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i("BehaviorSubjectBug", "received item: " + integer);
                    }
                });

        sub.unsubscribe();
    }

    private static class Foo {
        private Subject<Integer, Integer> subject = BehaviorSubject.create((Integer) 1);

        private Observable<Integer> getObservable() {
            return subject;
        }
    }
}
