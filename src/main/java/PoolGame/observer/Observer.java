package PoolGame.observer;

/**
 * Our Observer (as per the name :P)
 *
 * Note that is an interface; this is for reduced coupling,
 * we don't want the subject to have notions of what
 * the observers exactly are.
 */
public interface Observer {
    /**
    Void return, no parameters. We want
    to reduce coupling between subjects and observers
    as much as possible; we don't want to care about what
    they do, we don't want to enforce specific behaviour
    on them, we just let them do as they please when they
    are notified.
    */
    public String observe();
}
