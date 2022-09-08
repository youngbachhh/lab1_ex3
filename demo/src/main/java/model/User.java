package model;

public class User {
        protected int id;
        protected String name;
        protected String phoneNum;
        protected String address;

        public User() {}

        public User(int id, String name, String phoneNum, String address) {
            super();
            this.id = id;
            this.name = name;
            this.phoneNum = phoneNum;
            this.address = address;
        }

        public User(String name, String phoneNum, String address) {
            super();
            this.name = name;
            this.phoneNum = phoneNum;
            this.address = address;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
}
