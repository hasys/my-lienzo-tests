package org.roger600.lienzo.client.toolboxNew.impl2;

import com.ait.lienzo.client.core.shape.Group;

public abstract class AbstractGroupItem<T extends AbstractGroupItem> extends AbstractItem<T, Group> {

    private final GroupItem groupItem;

    protected AbstractGroupItem(final GroupItem groupItem) {
        this.groupItem = groupItem;
    }

    @Override
    public T show() {
        groupItem.show();
        return cast();
    }

    @Override
    public T hide() {
        groupItem.hide();
        return cast();
    }

    @Override
    public void destroy() {
        groupItem.destroy();
    }

    @Override
    public Group asPrimitive() {
        return groupItem.asPrimitive();
    }

    GroupItem getGroupItem() {
        return groupItem;
    }

    private T cast() {
        return (T) this;
    }
}