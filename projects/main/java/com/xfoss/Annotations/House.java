package com.xfoss.Annotations;

public interface House {
    /**
     * @deprecated open 方法的使用是不鼓励的，请
     * 使用 openFrontDoor 或 openBackDoor 取而代之。
     */
    @Deprecated
    void open();
    void openFrontDoor();
    void openBackDoor();
}
