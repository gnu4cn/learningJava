package com.xfoss.Annotations;

public class DeprecatedTest {
    /**
     * Javadoc 启用标签测试消息。
     * @deprecated Display() 方法已被启用。
     * 这是另一行文档。
     */
    @Deprecated
    public void Display()
    {
        System.out.println("Deprecatedtest display()");
    }

    public static void main(String args[])
    {
        DeprecatedTest d1 = new DeprecatedTest();
        d1.Display();
    }
}
