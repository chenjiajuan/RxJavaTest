package com.chenjiajuan.commom.router;

/**
 * create by chenjiajuan on 2018/10/4
 */
public class MappingNotFoundException extends  IllegalStateException{
    MappingNotFoundException() {
    }

    MappingNotFoundException(String s) {
        super(s);
    }
}
