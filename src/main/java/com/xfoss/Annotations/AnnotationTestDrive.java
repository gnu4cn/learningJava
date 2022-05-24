package com.xfoss.Annotations;

public class AnnotationTestDrive {
    public static void main (String[] args) {
        new AnnotationTestDrive();
    }

    interface House {
        @Deprecated
        void open();
        void openFrontDoor();
        void openBackDoor();
    }

    public AnnotationTestDrive () {
        Building b = new Building("测试楼栋");
        System.out.format("楼栋：%s", b);
    }

    class Building {
        private String name;
        private String address;

        public String getName () {
            return name;
        }

        public String getAddress () {
            return address;
        }

        public void setName (String n) {
            name = n;
        }

        public void setAddress (String a) {
            address = a;
        }

        public Building (String n) {
            name = n;
        }

        public Building (String n, String a) {
            name = n;
            address = a;
        }

        @Override
        public String toString () {
            return String.format("名称：%s, 地址：%s", name, address);
        }
    }
}


