package com.ly.maker.model;

import java.text.MessageFormat;

/**
 * The type Root node.
 *
 * @author zhangliang
 * @version Id : RootNode, v 0.1 2018/5/3 15:41 zhangliang Exp $
 */
public class RootNode extends Node {
    @Override
    public String format() {
        return MessageFormat.format("{0}", this.getName());
    }
}
