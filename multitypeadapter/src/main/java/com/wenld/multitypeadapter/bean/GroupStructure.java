package com.wenld.multitypeadapter.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenld on 2017/10/3.
 */

public class GroupStructure<P extends Object,C extends Object> implements Serializable {
    public P parent;
    public List<C> children;

    public boolean hasHeader() {
        if (parent != null) {
            return true;
        }
        return false;
    }

    public int getChildrenCount() {
        if (children != null) {
            return children.size();
        }
        return 0;
    }

    public boolean equalParent(Object parent){
        return this.parent==parent;
    }

    public int count() {
        return hasHeader() ? 1 + getChildrenCount() : getChildrenCount();
    }
}
