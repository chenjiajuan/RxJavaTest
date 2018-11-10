package com.chenjiajuan.commom.router;

import android.net.Uri;

/**
 * create by chenjiajuan on 2018/10/4
 */
 class Mapping<T> {
    protected final Uri matcher;
    private final T target;

    Mapping(Uri matcher, T target) {
        this.matcher = matcher;
        this.target = target;
    }

    T getTarget() {
        return target;
    }

    boolean match(Uri uri) {
        if (uri == null) {
            return false;
        }
        return Navigator.isMatch(uri, matcher);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Mapping
                && match(((Mapping) obj).matcher);
    }

    @Override
    public String toString() {
        return String.format("%s->%s", matcher.toString(), target);
    }

}
