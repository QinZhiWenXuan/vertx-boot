package xuan.wen.zhi.qin.impl;

import xuan.wen.zhi.qin.IService;

public class BService implements IService {
    @Override
    public String say(String message) {
        return this.getClass().getSimpleName() + message;
    }
}
