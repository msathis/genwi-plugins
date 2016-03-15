package com.genwi.plugin.plugins.model;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Author: sathis
 * Last Updated: 08/03/16.
 */

public class RxEventBus {

    private final Subject<ActionModel, ActionModel> subject = new SerializedSubject<ActionModel, ActionModel>(PublishSubject.<ActionModel>create());

    private static RxEventBus instance;

    private Action1 mGlobalObserver;

    RxEventBus(Action1 globalObserver) {
        mGlobalObserver = globalObserver;
    }

    public static RxEventBus getInstance(Action1 globalObserver) {
        if (instance == null)
            instance = new RxEventBus(globalObserver);
        return instance;
    }

    /**
     * <p>Posts an {@link E} to subscribed handlers.</p>
     * <p>If no handlers have been subscribed for event's class, unhandled will be called with unhandled event.</p>
     *
     * @param <E>       Type of event.
     * @param event     An event to post.
     * @param unhandled Will be called if event is not handled.
     *                  Note: If handler subscribed by using async, it can't guarantee event is actually handled.
     */
    public <E extends ActionModel> void post(E event, Action1<E> unhandled) {
        subject.onNext(event);
        if (event.handledCount == 0) {
            unhandled.call(event);
        }
    }

    /**
     * <p>An overload method of {@link #post(E, Action1)} that do nothing on unhandled.</p>
     *
     * @see #post(E, Action1)
     */
    public <E extends ActionModel> void post(E event) {
        post(event, new Action1<E>() {
            @Override
            public void call(E e) {
            }
        });
    }

    /**
     * <p>Subscribes handler to receive events type of specified class.</p>
     * <p>You should call {@link Subscription#unsubscribe()} if you want to stop receiving events.</p>
     *
     * @param <E>       Type of event.
     * @param type     Type of plugin.
     * @param action     Type of event that you want to receive.
     * @param handler   An event handler function that called if an event is posted.
     * @param scheduler handler will dispatched on this.
     * @return A {@link Subscription} which can stop observing.
     */
    private <E extends ActionModel> void subscribe(final String type, final String action, Func1 handler, Scheduler scheduler) {
        Class<E> clazz = (Class<E>) ActionModel.class;
        subject
            .filter(new Func1<ActionModel, Boolean>() {
                @Override
                public Boolean call(ActionModel actionModel) {
                    return type.equalsIgnoreCase(actionModel.type) && action.equalsIgnoreCase(actionModel.action);
                }
            })
            .cast(clazz)
            .doOnNext(new Action1<E>() {
                @Override
                public void call(E e) {
                    e.handledCount++;
                }
            })
            .observeOn(scheduler)
            .flatMap(handler)
            .subscribe(mGlobalObserver);
    }

    /**
     * An overload method of {@link #subscribe(String type, String action, Func1 handler, Scheduler)} that scheduler specified by {@link Schedulers#immediate()}
     *
     * @see #subscribe(String, String, Func1, Scheduler)
     */
    public <E extends ActionModel> void subscribe(String type, String action, Func1 handler) {
        subscribe(type, action, handler, Schedulers.immediate());
    }

}